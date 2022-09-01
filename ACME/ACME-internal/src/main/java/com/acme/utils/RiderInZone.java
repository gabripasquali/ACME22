package com.acme.utils;

import java.io.Serializable;
import java.util.ArrayList;

public class RiderInZone  implements Serializable{
    public ArrayList<Rider> results;

    public ArrayList<Rider> getResults() {
        return results;
    }
}
