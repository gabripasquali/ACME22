package com.acme.bank;

import camundajar.impl.com.google.gson.Gson;
import com.acme.LoggerDelegate;
import com.acme.bank.utils.Credentials;
import com.acme.bank.utils.LoginResponse;
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

public class Login implements JavaDelegate {
    private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        LOGGER.info("LOGIN BANK");
        Credentials acmeCredentials = new Credentials("acme", "acme");
        String loginUrl = BANK_URL+"login-request";
        Gson gson = new Gson();

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
                execution.setVariable("sidBank", sid);
            }
        }
    }
}
