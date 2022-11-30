package com.acme.bank;

import camundajar.impl.com.google.gson.Gson;
import com.acme.LoggerDelegate;
import com.acme.bank.utils.UpdateTransitionInfo;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import javax.ws.rs.core.MediaType;
import java.util.logging.Logger;

import static com.acme.utils.acmeVar.BANK_URL;
import static com.sun.jersey.api.client.ClientResponse.Status.OK;


public class Logout implements JavaDelegate {
    private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        LOGGER.info("LOGOUT BANK");
        String logoutUrl = BANK_URL + "logout";
        ClientConfig clientConfig = new DefaultClientConfig();
        Gson gson = new Gson();
        LogoutInfo logoutInfo = new LogoutInfo( (String) execution.getVariable("sidBank"));


        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        Client clientVerify = Client.create(clientConfig);
        WebResource webResource = clientVerify.resource(logoutUrl);
        ClientResponse clientResponse = webResource
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .post(ClientResponse.class, gson.toJson(logoutInfo));
        if(clientResponse.getStatus() == OK.getStatusCode()){
            LOGGER.info("Logout succeed");
        }
    }

    private class LogoutInfo{
        String sid;
        public LogoutInfo(String sid){
            this.sid= sid;
        }
    }
}
