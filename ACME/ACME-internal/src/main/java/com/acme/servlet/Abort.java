package com.acme.servlet;

import javax.servlet.annotation.WebServlet;

import com.acme.LoggerDelegate;
import com.acme.bank.utils.RequestVerify;
import com.acme.utils.ApiHttpServlet;

import camundajar.impl.com.google.gson.Gson;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;



import javax.servlet.http.HttpServletResponse;

import javax.servlet.*;
import javax.servlet.http.*;

import java.io.IOException;
import java.util.logging.Logger;


import static com.acme.utils.acmeVar.*;

@WebServlet("/abort")
public class Abort extends ApiHttpServlet{
    private HttpSession session;
    private ProcessEngineAdapter process;

    private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        process = new ProcessEngineAdapter(processEngine);
        Gson g = new Gson();

        String camundaProcessId = g.fromJson(request.getReader(), RequestVerify.class).instanceId;

        process.correlate(camundaProcessId, CLIENT_ABORT);

        Boolean abort = (Boolean) process.getVariable(camundaProcessId, ABORT);

        if (!process.isCorrelationSuccessful()) {
            sendResponse(response, g.toJson("no"), "PUT");
        } else {
            sendResponse(response, g.toJson("ok"), "PUT");
        }
        
    }
    
}
