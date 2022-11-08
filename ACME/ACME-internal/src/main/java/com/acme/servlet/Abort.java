package com.acme.servlet;

import javax.servlet.annotation.WebServlet;

import com.acme.utils.ApiHttpServlet;

import camundajar.impl.com.google.gson.Gson;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;



import javax.servlet.http.HttpServletResponse;

import javax.servlet.*;
import javax.servlet.http.*;

import java.io.IOException;


import static com.acme.utils.acmeVar.*;

@WebServlet("/abort")
public class Abort extends ApiHttpServlet{
    private HttpSession session;
    private ProcessEngineAdapter process;


    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        process = new ProcessEngineAdapter(processEngine);
        session = request.getSession(false);
        String camundaProcessId = session != null ? (String) session.getAttribute(PROCESS_ID) : "";
        process.correlate(camundaProcessId, CLIENT_ABORT);
        
    
        Gson g = new Gson();
        Boolean abort = (Boolean) process.getVariable(camundaProcessId, ABORT);
        if (session == null || session.getAttribute(PROCESS_ID) == null ||
        (!process.isCorrelationSuccessful() && session.getAttribute(CLIENT_ABORT) == null) || abort == false) {
            sendResponse(response, g.toJson("no"));
        } else 
            if(abort == true){
                session.setAttribute(CLIENT_ABORT,CLIENT_ABORT);
                sendResponse(response, g.toJson("ok"));
        }
        
    }
    
}
