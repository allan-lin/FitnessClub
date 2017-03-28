package com.example.install.fitnessclub;

/**
 * Created by Allan on 3/27/2017.
 */

public class Trip {
    private int id;
    private String date;
    private int location;

    public Trip(){

    }
    public Trip(String date, int location){
        this.date = date;
        this.location = location;
    }
    public Trip(int id, String date, int location){
        this.id = id;
        this.date = date;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }
}
