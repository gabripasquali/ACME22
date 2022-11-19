package com.acme.rider;
import com.acme.LoggerDelegate;
import com.acme.utils.DataBaseCons;
import com.acme.utils.models.Rider;
import com.acme.utils.models.RiderConsResp;
import com.acme.utils.models.Status;

import camundajar.impl.com.google.gson.Gson;


import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.delegate.DelegateExecution;


import java.util.logging.Logger;
import javax.ws.rs.core.MediaType;

import static com.sun.jersey.api.client.ClientResponse.Status.OK;

import static com.acme.utils.acmeVar.*;


public class NotifyRider implements JavaDelegate{
    private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        LOGGER.info("Notifica Rider");

        Rider rider = (Rider) execution.getVariable("cheaperRider");
        LOGGER.info("Recupero cheaper Rider: " + rider.getName());

        int idCons =  (int) execution.getVariable("idCons");

        Gson gson = new Gson();
        NotifyRiderReq body = new NotifyRiderReq(idCons);

        /**CALLING NOTIFICACONS RIDER SERVICE**/
        
        String url = RIDER_URL+"/notificaCons";
        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        Client client = Client.create(clientConfig);
        WebResource webResource = client.resource(url);
        ClientResponse response =  webResource
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .post(ClientResponse.class, gson.toJson(body));
        LOGGER.info("NOTIFICACONS STATUS CODE:" + response.getStatus());

         /**READ RESPONSE**/
        if(response.getStatus() == OK.getStatusCode()){
            //RiderConsResp responseRider = response.getEntity(RiderConsResp.class);
            //if(responseRider.getConsegna() == true){
                execution.setVariable(ABORT, false);
                
                DataBaseCons db = new DataBaseCons();
                Status status = Status.DELIVERED;
                db.modifyStatus(idCons, db, status);
            //}
            //LOGGER.info( responseRider.getInfo());
        
        } else {
            LOGGER.info("server error");
        }
    }

    private class NotifyRiderReq{
        int id;
        public NotifyRiderReq(int id){
            this.id = id;
        }
    }
}
    



