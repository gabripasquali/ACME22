package com.acme.servlet;

import camundajar.impl.com.google.gson.Gson;
import com.acme.utils.ApiHttpServlet;
import com.acme.utils.Database;
import com.acme.utils.models.RestaurantAvailability;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/changeAvailability")
public class ChangeAvailability extends ApiHttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        boolean isSuccesfull = true;
        Gson gson = new Gson();

        try {
            processEngine.getRuntimeService()
                    .createMessageCorrelation("ChangeAvailability")
                    .correlate();
        }catch (Exception e){
            System.out.println("------ ERRORE: "+ e.toString());
            isSuccesfull = false;
            sendResponse(resp, e + "  out of time", "POST");
        }

        /**get info and update db**/
        if(isSuccesfull){
            RestaurantAvailability restaurantAvailability = gson.fromJson(req.getReader(), RestaurantAvailability.class);
            System.out.println("***TIME OK restaurant name: "+restaurantAvailability.name+" is " + restaurantAvailability.disp+ "***");
            Database db = new Database();
            updateAvailability(restaurantAvailability.name, Boolean.parseBoolean(restaurantAvailability.disp), db);
            sendResponse(resp, "update restaurant" + restaurantAvailability.name + " availability as " + restaurantAvailability.disp, "POST");
        }

    }

    private void updateAvailability(String name, Boolean availability, Database db) {
        db.restaurants.stream()
                .filter(restaurant -> name.equals(restaurant.name))
                .forEach(restaurant -> restaurant.isOpen = availability  );

        db.save();
    }
}
