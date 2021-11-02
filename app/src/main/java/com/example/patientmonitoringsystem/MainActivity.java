package com.example.patientmonitoringsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;
import com.example.patientmonitoringsystem.Background.BackGrountTask;
import com.example.patientmonitoringsystem.Database.SqliteDatabaseHandler;
import com.example.patientmonitoringsystem.Models.Patient;
import com.example.patientmonitoringsystem.databinding.ActivityMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ActivityMainBinding binding;
    int display = 0;
    ArrayList<Patient> patients;
    private SqliteDatabaseHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseApp.initializeApp(getApplicationContext());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("FirebaseIOT");
        db = new SqliteDatabaseHandler(getApplicationContext());
        patients = new ArrayList<>();
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String body_temp = snapshot.child("body_temp").getValue().toString();
                String pulse_rate = snapshot.child("pulse_rate").getValue().toString();
                String humidity = snapshot.child("humidity").getValue().toString();
                String surr_temp = snapshot.child("temperature").getValue().toString();

                Date date1 = new Date();
                Patient patient = new Patient(String.valueOf(date1.getTime()),date,body_temp,pulse_rate,humidity,surr_temp);
                patients.add(patient);
                if (display == 0){
                    binding.heartRate.setText(pulse_rate);
                    binding.bodytemperature.setText(body_temp);
                    binding.humidity.setText(humidity);
                    binding.surrtemperature.setText(surr_temp);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.patientName.setText("Patient Name");
        binding.date.setText(date);

        binding.chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,ChatActivity.class);
                startActivity(i);
            }
        });

        binding.switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    display = 1;
                    Toast.makeText(getApplicationContext(), "Connection Ended", Toast.LENGTH_SHORT).show();

                }else {
                    display = 0;
                    Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Intent intent = new Intent(MainActivity.this, BackGrountTask.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        }

        patients = db.getAllData();

        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(
                getTemp()
        );

        LineGraphSeries<DataPoint> series1 = new LineGraphSeries<DataPoint>(
                getBpm()
        );

        binding.graph.addSeries(series);
        series1.setColor(Color.RED);
        binding.graph.addSeries(series1);
        binding.graph.getViewport().setXAxisBoundsManual(true);
        binding.graph.getViewport().setMinX(0);
        binding.graph.getViewport().setMaxX(20);
        binding.graph.getViewport().setScrollable(true);
        binding.graph.getViewport().setScalable(true);

        binding.graph.getViewport().setYAxisBoundsManual(true);
        binding.graph.getViewport().setMinY(20);
        binding.graph.getViewport().setMaxY(120);
        binding.graph.getViewport().setScrollableY(true);
        binding.graph.getViewport().setScalableY(true);
    }

    private DataPoint[] getTemp(){
        DataPoint[] dataPoint = new DataPoint[patients.size()];
        for (int i =0; i < patients.size() ; i++){
            dataPoint[i] = new DataPoint(i, Double.parseDouble(patients.get(i).getTemp_prob()));
        }
        return dataPoint;
    };

    private DataPoint[] getBpm(){
        DataPoint[] dataPoint = new DataPoint[patients.size()];
        for (int i =0; i < patients.size() ; i++){
            dataPoint[i] = new DataPoint(i, Double.parseDouble(patients.get(i).getBpm()));
        }
        return dataPoint;
    }

}