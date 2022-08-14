package com.acme.utilities;

import java.io.Serializable;
import java.time.LocalTime;

public class Order implements Serializable {
    public int id;
    public LocalTime oraCons;
    public String indRisto;
    public String indCliente;
    public Order(int id, LocalTime oraCons, String indRisto, String indCliente){
        this.id = id;
        this.oraCons = oraCons;
        this.indRisto = indRisto;
        this.indCliente = indCliente;
    }

    public Order(){

    }

    public int getId(){
        return id;
    }
}
