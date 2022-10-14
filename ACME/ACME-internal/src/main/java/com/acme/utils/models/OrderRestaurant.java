package com.acme.utils.models;

import java.io.Serializable;

public class OrderRestaurant  implements Serializable{
 
    public String restaurant;
    public Dish dish;
    public String oraCons;
    public String indCliente;
   
    public OrderRestaurant( String restaurant, Dish dish,String oraCons, String indCliente){
        
        this.restaurant = restaurant;
        this.dish = dish;
        this.oraCons = oraCons;
        this.indCliente = indCliente;
       
    }

    public OrderRestaurant(){

    }
    
    public String getOraCons(){
        return oraCons;
    }

    public String getNameRisto() {
    	return restaurant;
    }
   
}
