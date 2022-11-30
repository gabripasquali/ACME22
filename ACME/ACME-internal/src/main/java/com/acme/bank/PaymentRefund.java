package com.acme.bank;

import camundajar.impl.com.google.gson.Gson;
import com.acme.LoggerDelegate;
import com.acme.bank.utils.*;
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

public class PaymentRefund implements JavaDelegate{
    private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        LOGGER.info("Rimborso Pagamento");
        String refoundUrl = BANK_URL + "refound";
        ClientConfig clientConfig = new DefaultClientConfig();
        Gson gson = new Gson();

        String sid = (String) execution.getVariable("sidBank");
        int id = (int) execution.getVariable("idCons");
        UpdateTransitionInfo updateTransitionInfo = new UpdateTransitionInfo(sid, id);

        /**REFOUND REQUEST**/
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        Client clientVerify = Client.create(clientConfig);
        WebResource webResourceVerify = clientVerify.resource(refoundUrl);
        ClientResponse respVerify =  webResourceVerify
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .post(ClientResponse.class, gson.toJson(updateTransitionInfo));

        if(respVerify.getStatus() == OK.getStatusCode()){

            VerifyResponse verifyResponse = respVerify.getEntity(VerifyResponse.class);
            LOGGER.info("response: " + verifyResponse.success);
        }

    }
}
