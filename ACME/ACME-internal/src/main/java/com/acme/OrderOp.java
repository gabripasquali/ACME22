package com.acme;

import com.acme.utilities.Order;
import com.acme.utilities.Rider;
import com.acme.utilities.RiderConsResp;

import camundajar.impl.com.google.gson.Gson;


import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.delegate.DelegateExecution;

import java.time.LocalTime;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;
import javax.ws.rs.core.MediaType;

import static com.sun.jersey.api.client.ClientResponse.Status.OK;


public class OrderOp implements JavaDelegate{
    private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());


    @Override
    public void execute(DelegateExecution execution) throws Exception {
        LOGGER.info("Ordine Operativo, comunicazione a Rider");

        Rider rider = (Rider) execution.getVariable("cheaperRider");
        LOGGER.info("Recupero cheaper Rider: " + rider.getName());

        int[] minu = {0 , 15, 30, 45};
        int minM = 0;
		int maxM = 4;
		Random randomM = new Random();
		int i = randomM.nextInt(maxM + minM) + minM;
        int minutes = minu[i];

        int minH = 10;
		int maxH = 23;
        ThreadLocalRandom randomH = ThreadLocalRandom.current();
        int  hour = randomH.nextInt(minH, maxH + 1);


        LocalTime time = LocalTime.of(hour, minutes);
        LOGGER.info("Orario Consegna: " + time);

        String indRisto = "Via Risto 33";
        String indCliente = "Via Cliente 43";

        execution.setVariable("oraCons", time);
        int id =  (int) execution.getVariable("idCons");


        Gson gson = new Gson();
        Order body =  new Order(id, time, indRisto, indCliente);
        
         /**CALLING CONSAFF RIDER SERVICE**/
         String url = rider.getSite()+"/consAff";
         ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        Client client = Client.create(clientConfig);
        WebResource webResource = client.resource(url);
        ClientResponse response =  webResource
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .post(ClientResponse.class, gson.toJson(body));
        LOGGER.info("ORDEROP STATUS CODE:" + response.getStatus());
         /**READ RESPONSE**/
         if(response.getStatus() == OK.getStatusCode()){
             RiderConsResp responseRider = response.getEntity(RiderConsResp.class);
             execution.setVariable("consegna", responseRider.getConsegna());
             if(responseRider.getConsegna() == true){
                LOGGER.info( responseRider.getInfo());
                execution.setVariable("OrderOk", true);
             }
             else{
                LOGGER.info( responseRider.getInfo());
                execution.setVariable("OrderOk", false);
             }
         } else {
             LOGGER.info("server error");
         }
    //dire al rider che è stato selezionato
    }
}
