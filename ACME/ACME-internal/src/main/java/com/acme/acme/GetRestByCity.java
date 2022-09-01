package com.acme.acme;

import com.acme.utils.Database;
import com.acme.utils.Restaurant;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import com.acme.LoggerDelegate;

import java.util.Random;

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
        //get restaurant of required city
        //TODO: remove hardcoded city
        ArrayList<Restaurant> restaurants = getResByCity("Mantova", db);
        for(int i = 0; i < restaurants.size(); i++){
            LOGGER.info(restaurants.get(i).name);
        }

        Random random = new Random();
		int i = random.nextInt(restaurants.size() + 0) + 0;
        Restaurant restaurant = (Restaurant) restaurants.get(i);
        execution.setVariable("restaurantC", restaurant);
        LOGGER.info(restaurant.name + "Menù: " + restaurant.menu);
    }

    private ArrayList<Restaurant> getResByCity(String city, Database db) {
        return (ArrayList<Restaurant>) db.restaurants.stream()
                .filter(restaurant -> city.equals(restaurant.city) && restaurant.isOpen)
                .collect(Collectors.toList());

    }
}
