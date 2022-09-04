package com.acme.utils;

import java.io.Serializable;
import java.util.List;

public class Restaurant implements Serializable{
    public String name;
    public String city;
    public String address;

    public String url;
    public Boolean isOpen;
    public List<Dish> menu;


    public Restaurant(){
    }

    public Restaurant(String name, String city, String address, String url, Boolean isOpen, List<Dish> menu){
        this.isOpen = isOpen;
        this.name = name;
        this.menu = menu;
        this.address = address;
        this.url = url;
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return url;
    }
}
