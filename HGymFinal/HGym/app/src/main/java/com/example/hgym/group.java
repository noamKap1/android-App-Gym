package com.example.hgym;

import java.util.ArrayList;
//a class for the object group.
public class group {
    private String date;
    private String hour;
    private String minute;
    private int duration;
    private String emailOfTrainer;
    private ArrayList<user> participants;
    private String exp;
    //a builder for the object.
    public group(String date,String exp, String hour, String minute, int duration, String emailOfTrainer, ArrayList<user> participants) {
        this.date = date;
        this.hour = hour;
        this.exp = exp;
        this.participants=participants;
        this.minute = minute;
        this.duration = duration;
        this.emailOfTrainer = emailOfTrainer;
    }
    // a builder for the object.
    public group(String date,String exp, String hour, String minute, int duration, String emailOfTrainer) {
        this.date = date;
        this.hour = hour;
        this.exp = exp;
        this.participants = new ArrayList<>();
        this.minute = minute;
        this.duration = duration;
        this.emailOfTrainer = emailOfTrainer;
    }
    //get the participants.
    public ArrayList<user> getParticipants() {
        return participants;
    }
    //set the participants.
    public void setParticipants(ArrayList<user> participants) {
        this.participants = participants;
    }
    //get the explanation.
    public String getExp() {
        return exp;
    }
    //set the explanation.
    public void setExp(String exp) {
        this.exp = exp;
    }
    //an empty builder.
    public group(){
        date="";
        hour="";
        minute="";
        duration=0;
        emailOfTrainer="";
        exp = "";
        participants = new ArrayList<>();
    }
    //get the date.
    public String getDate() {
        return date;
    }
    //set the date.
    public void setDate(String date) {
        this.date = date;
    }
    //get the hour.
    public String getHour() {
        return hour;
    }
    //set the hour.
    public void setHour(String hour) {
        this.hour = hour;
    }
    //get the minute.
    public String getMinute() {
        return minute;
    }
    //set the minute.
    public void setMinute(String minute) {
        this.minute = minute;
    }
    //get the duration.
    public int getDuration() {
        return duration;
    }
    //set the duration.
    public void setDuration(int duration) {
        this.duration = duration;
    }
    //get the email.
    public String getEmailOfTrainer() {
        return emailOfTrainer;
    }
    //set the email.
    public void setEmailOfTrainer(String emailOfTrainer) {
        this.emailOfTrainer = emailOfTrainer;
    }
}
