package com.example.install.fitnessclub;

/**
 * Created by Allan on 1/19/2017.
 */

//This class create objects that represent exercise, responsible for the name and description
public class Exercise {
    private String name;
    private String description;

    public Exercise(String name, String description){
        this.name = name;
        this.description = description;
    }

    //toString method
    public String toString(){
        return getName();
    }

    //getters and setters for the name and description
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
