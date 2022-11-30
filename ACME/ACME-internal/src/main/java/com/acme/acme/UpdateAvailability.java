package com.acme.acme;

import com.acme.LoggerDelegate;
import com.acme.utils.Database;
import com.acme.utils.models.Restaurant;

import com.acme.utils.models.RestaurantAvailability;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class UpdateAvailability implements JavaDelegate {
    private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        RestaurantAvailability restaurantAvailability = (RestaurantAvailability) execution.getVariable("resAvailability");

        LOGGER.info("--update av--" + restaurantAvailability.name);
        //info needed : restaurant name and availability
        //update restaurant availability
        Database db = new Database();
        changeResAvailabilit(restaurantAvailability.name, Boolean.parseBoolean(restaurantAvailability.disp), db);
        //update info and save
        execution.setVariable("successUpdate", true);
        LOGGER.info("***TIME OK restaurant name: "+restaurantAvailability.name+" is " + restaurantAvailability.disp+ "***");
    }

    private void changeResAvailabilit(String name, boolean isOpen, Database db){
        db.restaurants.stream()
                .filter(restaurant -> name.equals(restaurant.name))
                .forEach(restaurant -> restaurant.isOpen = isOpen);
        db.save();
    }
}
