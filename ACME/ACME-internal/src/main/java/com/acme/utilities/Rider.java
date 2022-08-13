package com.acme.utilities;

import java.io.Serializable;

public class Rider implements Serializable {
    public String name;
    public String site;
    public boolean isInDistance;
    public double lat;
    public double lng;
    public double price;
    public Rider(String name, String site, double lng, double lat){
        this.lat = lat;
        this.lng = lng;
        this.site = site;
        this.name = name;
        this.isInDistance = false;
    }
    
    public void setPrice(String p){
        price = Double.parseDouble(p);
    }

    public Rider(){
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }

    public double getPrice(){
        return price;
    }
}
