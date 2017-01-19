package com.example.install.fitnessclub;

/**
 * Created by Allan on 1/19/2017.
 */

public class Exercise {
    private String name;
    private String description;

    public Exercise(String name, String description){
        this.name = name;
        this.description = description;
    }


    public String toString(){
        return getName();
    }

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
