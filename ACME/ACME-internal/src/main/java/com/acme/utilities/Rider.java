package com.acme.utilities;

import java.io.Serializable;

public class Rider implements Serializable {
    String name;
    String site;
    boolean isInDistance;
    double lat;
    double lng;
    public Rider(String name, String site, double lng, double lat){
        this.lat = lat;
        this.lng = lng;
        this.site = site;
        this.name = name;
        this.isInDistance = false;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }
}
