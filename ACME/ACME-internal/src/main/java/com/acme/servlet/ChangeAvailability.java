package com.acme.servlet;

import camundajar.impl.com.google.gson.Gson;
import com.acme.LoggerDelegate;
import com.acme.utils.ApiHttpServlet;
import com.acme.utils.Database;
import com.acme.utils.models.RestaurantAvailability;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@WebServlet("/changeAvailability")
public class ChangeAvailability extends ApiHttpServlet {
    private final java.util.logging.Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        ProcessEngineAdapter process = new ProcessEngineAdapter(processEngine);

        Gson gson = new Gson();
        Map<String, Object> resAv = new HashMap<>();
        RestaurantAvailability restaurantAvailability = gson.fromJson(req.getReader(), RestaurantAvailability.class);
        resAv.put("resAvailability", restaurantAvailability);
        String processInstanceID = processEngine.getRuntimeService()
                .startProcessInstanceByMessage("ChangeAvailability", resAv)
                .getProcessInstanceId();
        LOGGER.info("started process instance with id: " + processInstanceID);

        boolean successUpdate = (boolean) process.getVariable(processInstanceID, "successUpdate");
        LOGGER.info("is succesfull " + successUpdate);
        sendResponse(resp, ""+successUpdate, "POST");

    }

}
