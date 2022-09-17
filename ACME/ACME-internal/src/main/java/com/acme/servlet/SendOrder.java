package com.acme.servlet;

import camundajar.impl.com.google.gson.Gson;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;



import javax.servlet.http.HttpServletResponse;

import javax.servlet.*;
import javax.servlet.http.*;

import javax.servlet.annotation.*;
import java.io.IOException;



import com.acme.utils.ApiHttpServlet;
import com.acme.utils.models.OrderRestaurant;


import static com.acme.utils.acmeVar.*;




@WebServlet("/sendOrder")
public class SendOrder extends ApiHttpServlet {

    private HttpSession session;
    private ProcessEngineAdapter process;


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        process = new ProcessEngineAdapter(processEngine);
        session = request.getSession(false);
        String camundaProcessId = session != null ? (String) session.getAttribute(PROCESS_ID) : "";
        Gson g = new Gson();
        
        if (process.isActive(camundaProcessId) && session != null && session.getAttribute(SEND_ORDER) == null)
            process.setVariable(
                    camundaProcessId,
                    RESTAURANT_ORDER,
                    g.fromJson(request.getReader(), OrderRestaurant.class));
        process.correlate(camundaProcessId, SEND_ORDER);

        OrderRestaurant restaurantOrder =
                (OrderRestaurant) process.getVariable(camundaProcessId, RESTAURANT_ORDER);
        sendResponse(response, g.toJson(restaurantOrder));
    }




}