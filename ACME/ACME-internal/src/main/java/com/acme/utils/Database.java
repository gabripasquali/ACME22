package com.acme.utils;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

import camundajar.impl.com.google.gson.Gson;
import camundajar.impl.com.google.gson.stream.JsonReader;
import camundajar.impl.com.google.gson.stream.JsonWriter;

import java.util.Arrays;

public class Database {
    static String dbName = "resDB.json";
    public List<Restaurant> restaurants;

    public Database(){

        Gson g = new Gson();
        /**TRY READ FROM FILE**/
        try (JsonReader reader = new JsonReader(new FileReader(dbName))) {
            this.restaurants = Arrays.asList(g.fromJson(reader, Restaurant[].class));
        } catch (Exception e) {
            /**INITILIAZE EMPTY FILE AND ARRAY**/
            File file = new File(dbName);
            try {
                file.createNewFile();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            this.restaurants = new ArrayList<>();
            ArrayList<Dish> menu = new ArrayList<>();
            menu.add(new Dish("Carote", 9.00));
            menu.add(new Dish("Insalata", 12.00));

            restaurants.add(new Restaurant("Vegetale",
                    "Mantova",
                    "X", "http://localhost:10007",
                    true,
                    menu));
            this.save();
            System.out.println("NON TROVO IL FILE");
        }
    }

    public void save() {
        Gson g = new Gson();
        try (JsonWriter jsonWriter = new JsonWriter(new FileWriter(dbName));){
            g.toJson(g.toJsonTree(this.restaurants), jsonWriter);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}