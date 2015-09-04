package com.example.john.oneway;

/**
 * Created by John on 01/09/2015.
 */
public class denoModel {

    String name;
    int value;

    denoModel(String name, int value) {
        this.name= name;
        this.value= value;
    }

    public String getName() {
        return name;
    }
    public int getValue(){
        return this.value;
    }
}
