package com.acme.bank;

import camundajar.impl.com.google.gson.Gson;
import com.acme.bank.utils.*;
import com.acme.servlet.VerifyToken;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import com.acme.LoggerDelegate;

import javax.ws.rs.core.MediaType;
import java.util.Random;

import java.util.ArrayList;
import java.util.logging.Logger;

import static com.acme.utils.acmeVar.*;
import static com.sun.jersey.api.client.ClientResponse.Status.OK;

public class verifyToken implements JavaDelegate{
    private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        Credentials acmeCredentials = new Credentials("acme", "acme");
        String loginUrl = BANK_URL+"login-request";
        String verifyUrl = BANK_URL + "verifyToken";
        RequestVerify requestVerify = (RequestVerify) execution.getVariable(TOKEN);
        Gson gson = new Gson();
        LOGGER.info("Controllo Token: "+requestVerify.token);

        /**LOGIN**/
        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        Client client = Client.create(clientConfig);
        WebResource webResource = client.resource(loginUrl);
        ClientResponse resp =  webResource
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .post(ClientResponse.class, gson.toJson(acmeCredentials));

        if (resp.getStatus() == OK.getStatusCode()){
            LoginResponse loginResponse = resp.getEntity(LoginResponse.class);
            if(loginResponse.success){
                String sid = loginResponse.sid;
                String token = requestVerify.token;
                int id = (int) execution.getVariable("idCons");
                VerifyInfo verifyInfo = new VerifyInfo(sid, token, id );
                /**VERIFY TOKEN**/
                clientConfig = new DefaultClientConfig();

                clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
                Client clientVerify = Client.create(clientConfig);
                WebResource webResourceVerify = clientVerify.resource(verifyUrl);
                ClientResponse respVerify =  webResourceVerify
                        .accept(MediaType.APPLICATION_JSON_TYPE)
                        .type(MediaType.APPLICATION_JSON_TYPE)
                        .post(ClientResponse.class, gson.toJson(verifyInfo));

                if(respVerify.getStatus() == OK.getStatusCode()){

                    VerifyResponse verifyResponse = respVerify.getEntity(VerifyResponse.class);
                    LOGGER.info("response:" + verifyResponse.success);
                    execution.setVariable(TOKEN_OK, verifyResponse.success);
                }
            } else {
                LOGGER.info("server error");
                execution.setVariable(TOKEN_OK, false);
            }
        }
    }
}
