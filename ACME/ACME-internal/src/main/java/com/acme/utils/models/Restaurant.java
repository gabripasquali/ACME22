package com.acme.utils.models;

import java.io.Serializable;
import java.util.List;

public class Restaurant implements Serializable{
    public String name;
    public String city;
    public String address;
    public Double lat;
    public Double lng;

    public String url;
    public Boolean isOpen;
    public List<Dish> menu;


    public Restaurant(){
    }

    public Restaurant(String name, String city, String address, Double lat, Double lng , String url, Boolean isOpen, List<Dish> menu){
        this.isOpen = isOpen;
        this.name = name;
        this.menu = menu;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.url = url;
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return url;
    }

    public void updateMenu(List<Dish> menu) {
        this.menu = menu;
    }
}
