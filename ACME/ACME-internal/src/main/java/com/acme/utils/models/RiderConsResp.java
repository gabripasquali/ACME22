package com.acme.utils.models;
import java.io.Serializable;

public class RiderConsResp  implements Serializable{
    public boolean consegna;
    public String info;

    public String getInfo() {
        return info;
    }

    public boolean getConsegna() {
        return consegna;
    }
}
