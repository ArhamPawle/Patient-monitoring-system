package com.example.patientmonitoringsystem.Adapters;


import com.example.patientmonitoringsystem.Models.Message;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.patientmonitoringsystem.R;
import java.util.List;

public class ChatAdapters extends RecyclerView.Adapter {



    private List<Message> messageList;
    private Activity activity;

    public ChatAdapters(List<Message> messageList, Activity activity) {
        this.messageList = messageList;
        this.activity = activity;
    }

    @NonNull @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.adapter_message_one, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        TextView messageSend;
        TextView messageReceive;

        messageSend = holder.itemView.findViewById(R.id.message_send);
        messageReceive = holder.itemView.findViewById(R.id.message_receive);


        String message = messageList.get(position).getMessage();
        boolean isReceived = messageList.get(position).getIsReceived();
        if(isReceived){
            messageReceive.setVisibility(View.VISIBLE);
            messageSend.setVisibility(View.GONE);
            messageReceive.setText(message);
        }else {
            messageSend.setVisibility(View.VISIBLE);
            messageReceive.setVisibility(View.GONE);
            messageSend.setText(message);
        }
    }

    @Override public int getItemCount() {
        return messageList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView messageSend;
        TextView messageReceive;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            messageSend = itemView.findViewById(R.id.message_send);
            messageReceive = itemView.findViewById(R.id.message_receive);
        }
    }
}
