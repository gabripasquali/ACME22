package com.acme.servlet;

import camundajar.impl.com.google.gson.Gson;

import com.acme.LoggerDelegate;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;



import javax.servlet.http.HttpServletResponse;

import javax.servlet.*;
import javax.servlet.http.*;

import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.logging.Logger;


import com.acme.utils.ApiHttpServlet;
import com.acme.utils.models.OrderRestaurant;
import com.acme.utils.models.Rider;
import com.acme.utils.models.SendOrderContent;

import static com.acme.utils.acmeVar.*;




@WebServlet("/sendOrder")
public class SendOrder extends ApiHttpServlet {

    private HttpSession session;
    private ProcessEngineAdapter process;

    private final java.util.logging.Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());
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

        if(process.isCorrelationSuccessful()){
            Boolean restAv = (boolean) process.getVariable(camundaProcessId, RESTAURANTAV);

            if (restAv == false){
                respAbort abortRest = new respAbort("abortRest");
                sendResponse(response, g.toJson(abortRest));
            }else{
                Boolean riderAv = (boolean) process.getVariable(camundaProcessId, RIDERAV);
                if (riderAv == false){
                    respAbort abortRider = new respAbort("abortRider");
                    sendResponse(response, g.toJson(abortRider));
                }
                else{
                    Rider rider = (Rider) process.getVariable(camundaProcessId, RIDER);
                    Double priceR = rider.getPrice();
                    OrderRestaurant order = (OrderRestaurant) process.getVariable(camundaProcessId, RESTAURANT_ORDER);
                    Double priceO = order.getTotalPrice();
                    Double priceTot = priceR + priceO;
                    process.setVariable(camundaProcessId, PRICETOT, priceTot);
                    int id = (int) process.getVariable(camundaProcessId, "idCons");
                    SendOrderContent content = new SendOrderContent("go", BANK_URL, priceTot, id);
                    //respAbort go = new respAbort("go");
                    LOGGER.info(content.bank_url);
                    sendResponse(response,g.toJson(content));
                }
            }
        } else {
            //timeout error: too much time passed
            respAbort resp = new respAbort("out of time");
            sendResponse(response, g.toJson(resp));
            LOGGER.info("--iscorrelationsuccesfull"+process.isCorrelationSuccessful());
        }
    }

    private class respAbort {
        String info;
        public respAbort(String info){
            this.info = info;
        }
    }



}