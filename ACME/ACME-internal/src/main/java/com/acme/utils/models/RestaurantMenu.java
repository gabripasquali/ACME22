package com.acme.utils.models;

import java.io.Serializable;

import java.util.List;

public class RestaurantMenu  implements Serializable{
    
    public String name;

    public List<Dish> menu;
}
