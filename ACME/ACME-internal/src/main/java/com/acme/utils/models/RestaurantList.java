package com.acme.utils.models;

import camundajar.impl.com.google.gson.annotations.Expose;


import java.util.ArrayList;

public class RestaurantList {

    @Expose
    private ArrayList<Restaurant> restaurants;
    private String instanceId;

    public boolean isEmpty() {
        return this.restaurants.isEmpty();
    }

    public ArrayList<Restaurant> getRestaurants() {
        return this.restaurants;
    }

    public Restaurant gRestaurant(int i){
        Restaurant rest = this.restaurants.get(i);
        return rest;
    }
    public void setRestaurants(ArrayList<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }
}
