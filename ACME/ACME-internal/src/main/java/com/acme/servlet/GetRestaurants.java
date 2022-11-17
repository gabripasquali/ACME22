package com.acme.servlet;

import camundajar.impl.com.google.gson.Gson;

import com.acme.LoggerDelegate;
import java.util.logging.Logger;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;



import javax.servlet.*;
import javax.servlet.http.*;


import javax.servlet.annotation.*;
import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

import com.acme.utils.ApiHttpServlet;
import com.acme.utils.models.RestaurantList;


import static com.acme.utils.acmeVar.*;

@WebServlet("/getRestaurant")
public class GetRestaurants extends ApiHttpServlet {
    private final java.util.logging.Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());
   
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine(true);
        ProcessEngineAdapter process = new ProcessEngineAdapter(processEngine);

        Map<String, Object> cityVariable = new HashMap<>();

       cityVariable.put("city", request.getParameter("city"));
       
        String processInstanceId = process
                .startProcessInstanceByMessage(START,cityVariable)
                .getProcessInstanceId();

        LOGGER.info("Started process instance with id: " + processInstanceId);
       
        processEngine.getRuntimeService().setVariable(processInstanceId, "city", request.getParameter("city"));
        String città = (String) processEngine.getRuntimeService().getVariable(processInstanceId, "city");

        LOGGER.info("Città selezionata: " + città);

        Gson g = new Gson();
        RestaurantList restaurants = g.fromJson((String) process.getVariable(processInstanceId, RESTAURANTS), RestaurantList.class);
        restaurants.setInstanceId(processInstanceId);
        sendResponse(response,g.toJson(restaurants), "GET");
    }
   
}
