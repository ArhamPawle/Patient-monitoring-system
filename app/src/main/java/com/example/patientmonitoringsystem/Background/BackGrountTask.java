package com.example.patientmonitoringsystem.Background;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageItemInfo;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.patientmonitoringsystem.Database.SqliteDatabaseHandler;
import com.example.patientmonitoringsystem.MainActivity;
import com.example.patientmonitoringsystem.Models.Patient;
import com.example.patientmonitoringsystem.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BackGrountTask extends Service {

    private MediaPlayer mediaPlayer;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private int FOREGROUND_ID = 1;
    private int REQUEST_CODE = 0;
    private int FLAGS = 0;
    private String CHANNELID = "channel1";
    private SqliteDatabaseHandler db;
    String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

    public BackGrountTask(){
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        showNotification();
        Intent intent1 = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, REQUEST_CODE, intent1, FLAGS);
        Notification notification = new NotificationCompat.Builder(this,CHANNELID)
                .setContentTitle("HealthCare")
                .setContentText("Monitor")
                .setSmallIcon(R.drawable.ic_action_name)
                .setContentIntent(pendingIntent)
                .build();

        db = new SqliteDatabaseHandler(getApplicationContext());
        startForeground(1, notification);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("FirebaseIOT");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Date date1 = new Date();
                String body_temp = snapshot.child("body_temp").getValue().toString();
                String pulse_rate = snapshot.child("pulse_rate").getValue().toString();
                String humidity = snapshot.child("humidity").getValue().toString();
                String temperature = snapshot.child("temperature").getValue().toString();
                Patient patient = new Patient(String.valueOf(date1.getTime()),date,body_temp,pulse_rate,humidity,temperature);
                db.addPatient(patient);
                if (Float.parseFloat(body_temp) > 37 || Float.parseFloat(body_temp) < 25  || Integer.parseInt(pulse_rate) > 95 || Integer.parseInt(pulse_rate) < 50){
                    mediaPlayer = MediaPlayer.create(getBaseContext(),R.raw.bgtask);
                    mediaPlayer.start();
                    mediaPlayer.isLooping();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return START_STICKY;
    }

    private void showNotification(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(
                    CHANNELID,"Health Care", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
