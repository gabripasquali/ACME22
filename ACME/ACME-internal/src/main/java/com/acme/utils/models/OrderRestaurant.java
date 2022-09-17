package com.acme.utils.models;

import java.io.Serializable;

public class OrderRestaurant  implements Serializable{
 
    public String restaurant;
    public Dish dish;
    public String oraCons;
   
    public OrderRestaurant( String restaurant, Dish dish,String oraCons){
        
        this.restaurant = restaurant;
        this.dish = dish;
        this.oraCons = oraCons;
       
    }

    public OrderRestaurant(){

    }
    
    public String getNameRisto() {
    	return restaurant;
    }
   
}
