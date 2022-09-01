package com.acme.utils;

import java.io.Serializable;
import java.time.LocalTime;

public class OrderRider implements Serializable {
    public int id;
    public LocalTime oraCons;
    public String indRisto;
    public String indCliente;
    public OrderRider(int id, LocalTime oraCons, String indRisto, String indCliente){
        this.id = id;
        this.oraCons = oraCons;
        this.indRisto = indRisto;
        this.indCliente = indCliente;
    }

    public OrderRider(){

    }

    public int getId(){
        return id;
    }
}
