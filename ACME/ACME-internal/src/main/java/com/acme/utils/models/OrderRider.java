package com.acme.utils.models;

import java.io.Serializable;


public class OrderRider implements Serializable {
    public int id;
    public String oraCons;
    public String indRisto;
    public String indCliente;
    public OrderRider(int id, String oraCons, String indRisto, String indCliente){
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
