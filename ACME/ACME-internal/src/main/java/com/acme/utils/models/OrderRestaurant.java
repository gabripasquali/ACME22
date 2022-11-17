package com.acme.utils.models;

import java.io.Serializable;
import java.util.List;

public class OrderRestaurant  implements Serializable{
 
    public String restaurant;
    public List<Dish> dishes;
    public String oraCons;
    public String indCliente;
    public String instanceId;
   
    public OrderRestaurant( String restaurant, List<Dish> dishes,String oraCons, String indCliente){
        
        this.restaurant = restaurant;
        this.dishes = dishes;
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

    public List<Dish> getDishes(){
        return dishes;
    }

    public String getIndCliente(){
        return indCliente;
    }
    public Double getTotalPrice(){
        Double price = 0.0;
        for(int i = 0; i<dishes.size();i++){
            price += dishes.get(i).getPrice();
        }
        return price;
    }
   
}
