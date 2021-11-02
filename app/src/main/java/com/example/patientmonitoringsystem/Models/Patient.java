package com.example.patientmonitoringsystem.Models;

public class Patient {
    String temp, date, bpm, time, humidity, temp_prob;

    public Patient() { }

    public Patient(String time, String date, String temp_prob, String bpm, String humidity, String temp)
    {
        this.time = time;
        this.date = date;
        this.temp_prob = temp_prob;
        this.bpm = bpm;
        this.humidity = humidity;
        this.temp = temp;
    }

    public String getTemperature() {
        return temp;
    }

    public void setTemperature(String temperature) {
        this.temp = temp;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBpm() {
        return bpm;
    }

    public void setBpm(String bpm) {
        this.bpm = bpm;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getTemp_prob() {
        return temp_prob;
    }

    public void setTemp_prob(String temp_prob) {
        this.temp_prob = temp_prob;
    }
}
