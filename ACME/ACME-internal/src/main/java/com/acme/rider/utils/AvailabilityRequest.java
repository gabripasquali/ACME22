package com.acme.rider.utils;

import java.io.Serializable;

public class AvailabilityRequest implements Serializable{
    public String rider;
    public Double bill;
    public String resAddress;
    public String clientAddress;

    public AvailabilityRequest(String rider, Double bill, String resAddress, String clientAddress){
        this.clientAddress = clientAddress;
        this.bill = bill;
        this.resAddress = resAddress;
        this.rider = rider;
    }
}
