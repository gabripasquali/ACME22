package com.acme.acme;

import camundajar.impl.com.google.gson.Gson;
import com.acme.LoggerDelegate;
import com.acme.utils.Database;
import com.acme.utils.models.RestaurantMenu;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.logging.Logger;

public class UpdateMenu implements JavaDelegate {
    private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
            RestaurantMenu restaurantMenu = (RestaurantMenu) execution.getVariable("resMenu");

            Database db = new Database();
            updateMenu(restaurantMenu, db);
            execution.setVariable("successUpdate", true);

            LOGGER.info("***TIME OK res name:" + restaurantMenu.name +" menu updated");
    }

    public void updateMenu(RestaurantMenu restaurantMenu, Database db){
        db.restaurants.stream()
                .filter(restaurant -> restaurantMenu.name.equals(restaurant.name))
                .forEach(restaurant -> restaurant.updateMenu(restaurantMenu.menu));

        db.save();
    }
}
