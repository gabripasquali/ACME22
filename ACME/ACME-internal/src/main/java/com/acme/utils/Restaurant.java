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

    public String getName() {
        return name;
    }

    public String getSite() {
        return url;
    }
}
