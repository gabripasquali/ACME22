package com.acme.utils;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.List;

public class OrderRestaurant  implements Serializable{
    public int id;
    public String restaurant;
    public Dish dish;
    public LocalTime oraCons;
   
    public OrderRestaurant(int id, String restaurant, Dish dish,LocalTime oraCons){
        this.id = id;
        this.restaurant = restaurant;
        this.dish = dish;
        this.oraCons = oraCons;
       
    }

    public OrderRestaurant(){

    }

    public int getId(){
        return id;
    }
}
