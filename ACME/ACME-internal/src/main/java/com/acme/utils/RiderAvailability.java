package com.acme.utils;
import java.io.Serializable;

public class RiderAvailability  implements Serializable{
    public boolean disp;
    public String prezzo;

    public String getPrezzo() {
        return prezzo;
    }

    public boolean isDisp() {
        return disp;
    }
}
