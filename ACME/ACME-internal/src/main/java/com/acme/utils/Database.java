package com.acme.utils;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

import com.acme.utils.models.Dish;
import com.acme.utils.models.Restaurant;

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

            restaurants.add(new Restaurant("Vegetale",
                    "Mantova",
                    "X", "http://localhost:10007",
                    true,
                    new ArrayList<Dish>() {
                        {
                            add(new Dish("Carote", 9.00));
                            add(new Dish("Insalata", 12.00));
                        }
                    }
            ));

            restaurants.add(new Restaurant("DeCarlo",
                    "Trento",
                    "X", "http://localhost:10008",
                    true,
                    new ArrayList<Dish>() {
                        {
                            add(new Dish("Canaderli", 12.50));
                            add(new Dish("Carlo",8.50));
                        }
                    }
            ));

            restaurants.add(new Restaurant("Paradiso",
                    "Mantova",
                    "X", "http://localhost:10009",
                    true,
                    new ArrayList<Dish>() {
                        {
                            add(new Dish("Tagliata", 15.00));
                            add(new Dish("Carbonara",11.50));
                        }
                    }
            ));

            restaurants.add(new Restaurant("Sushino",
                    "Cagliari",
                    "X", "http://localhost:10010",
                    true,
                    new ArrayList<Dish>() {
                        {
                            add(new Dish("Nigiri", 3.50));
                            add(new Dish("Udon",5.00));
                        }
                    }
            ));

            restaurants.add(new Restaurant("Tramonto",
                    "Cagliari",
                    "X", "http://localhost:10011",
                    true,
                    new ArrayList<Dish>() {
                        {
                            add(new Dish("Aragosta", 25.00));
                            add(new Dish("Frittura",18.50));
                        }
                    }
            ));

            restaurants.add(new Restaurant("YinDyan",
                    "Trento",
                    "X", "http://localhost:10012",
                    true,
                    new ArrayList<Dish>() {
                        {
                            add(new Dish("Nuvole", 4.50));
                            add(new Dish("Spicy Pork",9.50));
                        }
                    }
            ));


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