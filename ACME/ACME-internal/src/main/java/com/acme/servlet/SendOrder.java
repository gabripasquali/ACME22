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
import com.acme.utils.models.Rider;
import com.acme.utils.models.SendOrderContent;

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

        Boolean restAv = (boolean) process.getVariable(camundaProcessId, RESTAURANTAV);
        
       
        if (session == null || session.getAttribute(PROCESS_ID) == null
                    || (!process.isCorrelationSuccessful()  && session.getAttribute(SEND_ORDER) == null)) {
                        respAbort no = new respAbort("no");
                    sendResponse(response,g.toJson(no));
                    }
        else
            if (restAv == false){
                respAbort abortRest = new respAbort("abortRest");
                sendResponse(response, g.toJson(abortRest));
            }else{
                Boolean riderAv = (boolean) process.getVariable(camundaProcessId, RIDERAV);
                if (riderAv == false){
                    respAbort abortRider = new respAbort("abortRider");
                    sendResponse(response, g.toJson(abortRider));
                }
                else
                    {
                        respAbort go = new respAbort("go");
                        sendResponse(response,g.toJson(go));
                    }
        }
    }

    private class respAbort {
        String info;
        public respAbort(String info){
            this.info = info;
        }
    }



}