package com.acme.utils;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import camundajar.impl.com.google.gson.Gson;
import camundajar.impl.com.google.gson.stream.JsonReader;

import java.util.Arrays;

public class Database {
    static String dbName = "src/main/java/com/acme/utils/resDB.json";
    public List<Restaurant> restaurants;

    public Database(){
        Gson g = new Gson();
        /**TRY READ FROM FILE**/
        try (JsonReader reader = new JsonReader(new FileReader(dbName))) {
            this.restaurants = Arrays.asList(g.fromJson(reader, Restaurant[].class));
        } catch (Exception e) {
            /**INITILIAZE EMPTY ARRAY**/
            this.restaurants = new ArrayList<>();
            e.printStackTrace();
        }
    }
}