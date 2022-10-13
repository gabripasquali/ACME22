package com.acme.acme;

import com.acme.utils.Database;
import com.acme.utils.models.Restaurant;
import com.acme.utils.models.RestaurantList;

import camundajar.impl.com.google.gson.Gson;


import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import com.acme.LoggerDelegate;


import static com.acme.utils.acmeVar.*;

import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class GetRestByCity implements JavaDelegate{
    private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        LOGGER.info("Ristorante in base a città");
        //get db
        Database db = new Database();
        String city = (String) execution.getVariable("city");
        LOGGER.info((city));
        //get restaurant of required city
        RestaurantList restaurants = new RestaurantList();
        restaurants.setRestaurants(getResByCity(city, db));


        Gson g = new Gson();
        
    
        LOGGER.info("Lista Risto: " + g.toJson(restaurants));
        
        Restaurant restaurant = restaurants.gRestaurant(0);
        //execution.setVariable(RESTAURANT, g.toJson(restaurant));
        execution.setVariable(RESTAURANTS, g.toJson(restaurants));
        //LOGGER.info(restaurant.name + "Menù: " + restaurant.menu);
    }

    private ArrayList<Restaurant> getResByCity(String city, Database db) {
        return (ArrayList<Restaurant>) db.restaurants.stream()
                .filter(restaurant -> city.equals(restaurant.city) && restaurant.isOpen)
                .collect(Collectors.toList());
    }
}
