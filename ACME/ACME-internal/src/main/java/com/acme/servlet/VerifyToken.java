package com.acme.servlet;

import camundajar.impl.com.google.gson.Gson;
import com.acme.LoggerDelegate;
import com.acme.bank.utils.RequestVerify;
import com.acme.bank.utils.VerifyResponse;
import com.acme.utils.ApiHttpServlet;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

import static com.acme.utils.acmeVar.*;
import java.io.Serializable;
import java.util.logging.Logger;

@WebServlet("/verifyToken")
public class VerifyToken extends ApiHttpServlet {
    private HttpSession session;
    private ProcessEngineAdapter process;
    private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /**CORRELATE MESSAGE**/
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        Gson gson = new Gson();
        process = new ProcessEngineAdapter(processEngine);
        session = request.getSession(false);
        String camundaProcessId = session != null ? (String) session.getAttribute(PROCESS_ID) : "";
        if(process.isActive(camundaProcessId) && session != null){
            process.setVariable(camundaProcessId, TOKEN, gson.fromJson(request.getReader(), RequestVerify.class));
        }
        process.correlate(camundaProcessId, RECEIVE_TOKEN);

        if(process.isCorrelationSuccessful()){
            //wait for process to be completed
            boolean token_ok = (boolean) process.getVariable(camundaProcessId, TOKEN_OK);
            sendResponse(response, gson.toJson(token_ok));
        } else {
            //return error
            sendResponse(response, "SERVER ERROR");
        }
    }

}
