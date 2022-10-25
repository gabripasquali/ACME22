package com.acme.utils.models;

import java.io.Serializable;
import java.util.List;

public class DB implements Serializable {
    public List<Rider> riderList;
    public List<Restaurant> restaurants;

    public DB(List<Restaurant> restaurants, List<Rider> riderList) {
        this.restaurants = restaurants;
        this.riderList = riderList;
    }
}