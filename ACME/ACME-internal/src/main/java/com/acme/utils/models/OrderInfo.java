package com.acme.utils.models;

import java.io.Serializable;
import java.util.List;



public class OrderInfo implements Serializable{
    
    public int id;
    public String restaurant;
    public List<Dish> dishes;
    public Double price;
    public String oraCons;
    public String indCliente;
    public String rider;
    public Status status;
   
    public OrderInfo(int id,  String restaurant, List<Dish> dishes, Double price, String oraCons, String indCliente, String rider, Status status){
        
        this.id = id;
        this.restaurant = restaurant;
        this.dishes = dishes;
        this.price = price;
        this.oraCons = oraCons;
        this.indCliente = indCliente;
        this.rider = rider;
        this.status = status;
    }

    public int getId(){
        return id;
    }
    public Double getPrice(){
        return price;
    }

}
