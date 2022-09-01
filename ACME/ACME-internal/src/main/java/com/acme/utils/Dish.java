package com.acme.utils;
import java.io.Serializable;

public class Dish implements Serializable{
    public String name;
    public double price;

    public Dish( String name, double price){
        this.name = name;
        this.price = price;
    }


    public String getName() {
        return name;
    }

    public double getPrice(){
        return price;
    }
}
