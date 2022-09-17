package com.acme.acme;

import com.acme.LoggerDelegate;
import com.acme.utils.Database;
import com.acme.utils.models.Restaurant;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class UpdateAvailability implements JavaDelegate {
    private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        LOGGER.info("--update av--");
        //info needed : restaurant name and availability
        //hardcoded info
        String resName = "Vegetale";
        boolean isOpen = true;
        //update restaurant availability
        Database db = new Database();
        changeResAvailabilit(resName, isOpen, db);
        //update info and save
    }

    private void changeResAvailabilit(String name, boolean isOpen, Database db){
        db.restaurants.stream()
                .filter(restaurant -> name.equals(restaurant.name))
                .forEach(restaurant -> restaurant.isOpen = isOpen);
        db.save();
    }
}
