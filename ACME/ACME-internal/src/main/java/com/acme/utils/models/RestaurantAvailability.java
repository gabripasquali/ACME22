package com.acme.utils.models;
import java.io.Serializable;

public class RestaurantAvailability  implements Serializable{
    
    public String name;
    public String disp;

    public String isDisp() {
        return disp;
    }
    
}
