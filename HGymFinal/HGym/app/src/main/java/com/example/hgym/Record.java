package com.example.hgym;

import java.util.Date;
//a class for the object record.
public class Record {
    String pathName;
    long duration;
    String date;
    //get the path name.
    public String getPathName() {
        return pathName;
    }
    //set the path name.
    public void setPathName(String pathName) {
        this.pathName = pathName;
    }
    //get the duration.
    public long getDuration() {
        return duration;
    }
    //set the duration.
    public void setDuration(long duration) {
        this.duration = duration;
    }
    //set the date.
    public void setDate(String date) {
        this.date = date;
    }
    //get the date.
    public String getDate() {
        return date;
    }
    //builder for the object.
    public Record(String pathName, long duration) {
        this.pathName = pathName;
        this.duration = duration;
        date = (new Date()).toString();
    }
    //empty builder.
    public Record(){
        pathName = "";
        duration =0;
        date ="";
    }
}