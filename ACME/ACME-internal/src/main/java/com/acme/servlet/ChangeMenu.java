package com.acme.servlet;

import camundajar.impl.com.google.gson.Gson;
import com.acme.utils.ApiHttpServlet;
import com.acme.utils.Database;
import com.acme.utils.models.Dish;
import com.acme.utils.models.Restaurant;
import com.acme.utils.models.RestaurantMenu;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

@WebServlet("/changeMenu")
public class ChangeMenu extends ApiHttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        boolean isSuccesfull = true;

        try{
            processEngine.getRuntimeService()
                    .createMessageCorrelation("ChangeMenu")
                    .correlate();
        } catch (Exception e){
            sendResponse(response, "out of time", "POST");
            isSuccesfull = false;
        }
        if (isSuccesfull){
            Gson gson = new Gson();
            RestaurantMenu restaurantMenu = gson.fromJson(request.getReader(), RestaurantMenu.class);
            Database db = new Database();
            updateMenu(restaurantMenu, db);

            sendResponse(response, "menu updated", "POST");
        }

    }

    public void updateMenu(RestaurantMenu restaurantMenu, Database db){
        db.restaurants.stream()
                .filter(restaurant -> restaurantMenu.name.equals(restaurant.name))
                .forEach(restaurant -> restaurant.updateMenu(restaurantMenu.menu));

        db.save();
    }
}
