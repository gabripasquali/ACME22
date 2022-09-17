package com.acme.servlet;

import camundajar.impl.com.google.gson.Gson;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;



import javax.servlet.*;
import javax.servlet.http.*;


import javax.servlet.annotation.*;
import java.io.IOException;

import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.acme.utils.ApiHttpServlet;
import com.acme.utils.models.RestaurantList;


import static com.acme.utils.acmeVar.*;

@WebServlet("/getRestaurant")
public class GetRestaurants extends ApiHttpServlet {
    private final Logger LOGGER = LogManager.getLogger(this.getClass());
   
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine(true);
        
        ProcessEngineAdapter process = new ProcessEngineAdapter(processEngine);
            
        
  
        Map<String, Object> cityVariable = new HashMap<>();
        
       
  
        
       cityVariable.put("city", request.getParameter("city"));
       
        String processInstanceId = process
                .startProcessInstanceByMessage(START,cityVariable)
                .getProcessInstanceId();
        
        
       
        session.setAttribute(PROCESS_ID, processInstanceId);
        LOGGER.info("Started process instance with id: {}", processInstanceId);
       
        processEngine.getRuntimeService().setVariable(processInstanceId, "city", request.getParameter("city"));
        String città = (String) processEngine.getRuntimeService().getVariable(processInstanceId, "city");

        LOGGER.info("Città selezionata: " + città);
        

        Gson g = new Gson();
        RestaurantList restaurants = g.fromJson((String) process.getVariable(processInstanceId, RESTAURANTS), RestaurantList.class);
   
        sendResponse(response,g.toJson(restaurants));

       
    }
   
}
