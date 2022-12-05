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
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/changeMenu")
public class ChangeMenu extends ApiHttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        ProcessEngineAdapter process = new ProcessEngineAdapter(processEngine);
        Gson gson = new Gson();
        RestaurantMenu restaurantMenu = gson.fromJson(request.getReader(), RestaurantMenu.class);
        Map<String, Object> resMenu = new HashMap<>();

        resMenu.put("resMenu", restaurantMenu);

        String processInstanceId = processEngine.getRuntimeService()
                .startProcessInstanceByMessage("ChangeMenu", resMenu)
                .getProcessInstanceId();

        boolean successUpdate = (boolean) process.getVariable(processInstanceId, "successUpdate");

        sendResponse(response, ""+successUpdate, "POST");

    }


}
