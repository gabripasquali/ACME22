package com.acme.abort;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.acme.LoggerDelegate;
import com.acme.utils.Restaurant;
import com.acme.utils.Rider;
import com.acme.utils.RiderConsResp;

import camundajar.impl.com.google.gson.Gson;

import java.util.logging.Logger;

import javax.ws.rs.core.MediaType;

import static com.sun.jersey.api.client.ClientResponse.Status.OK;

public class AbortOrderEveryone implements JavaDelegate {
    private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        LOGGER.info("Task abort order Cliente Ristorante e Rider");

        LOGGER.info("1) Task abort order Cliente");

        LOGGER.info("2) Task abort order Ristorante ");

        Restaurant rest = (Restaurant) execution.getVariable("restaurantC");
        LOGGER.info("ABORT REST : "+ rest.getName());

        /**CALLING ABORT REST SERVICE**/
        String url = rest.getSite()+"/abortOrder";
        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        Client client = Client.create(clientConfig);
        WebResource webResource = client.resource(url);
        ClientResponse response =  webResource
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .get(ClientResponse.class);

         /**READ RESPONSE**/
         if(response.getStatus() == OK.getStatusCode()){
            LOGGER.info("Restaurant " + rest.getName() + ": ABORT SUCCESSED");
            
        } else {
            LOGGER.info("server error");
        }


        LOGGER.info("3) Task abort order Rider");

        Rider rider = (Rider) execution.getVariable("cheaperRider");
        LOGGER.info("Recupero Rider: " + rider.getName());

        int idCons =  (int) execution.getVariable("idCons");

        Gson gson = new Gson();
        IdConsAbort body = new IdConsAbort(idCons);

        /**CALLING ABORT RIDER SERVICE**/
        url = rider.getSite()+"/consAnnul";
        clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        client = Client.create(clientConfig);
        webResource = client.resource(url);
        response =  webResource
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .post(ClientResponse.class, gson.toJson(body));
        LOGGER.info("ABORT RIDER STATUS CODE:" + response.getStatus());
         /**READ RESPONSE**/
        if(response.getStatus() == OK.getStatusCode()){
            RiderConsResp responseRider = response.getEntity(RiderConsResp.class);
            LOGGER.info( responseRider.getInfo());
        
        } else {
            LOGGER.info("server error");
        }
    }

    private class IdConsAbort{
        int id;
        public IdConsAbort(int id){
            this.id = id;
        }
    }
}



 

