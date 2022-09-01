package com.acme.restaurant;

import com.acme.LoggerDelegate;
import com.acme.utils.Dish;
import com.acme.utils.OrderRestaurant;
import com.acme.utils.Restaurant;
import com.acme.utils.RestaurantAvailability;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import javax.ws.rs.core.MediaType;
import static com.sun.jersey.api.client.ClientResponse.Status.OK;
import camundajar.impl.com.google.gson.Gson;


import java.time.LocalTime;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

public class GetAvailability implements JavaDelegate{
    private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        LOGGER.info("Disponibilità ristorante");

        Restaurant rest = (Restaurant) execution.getVariable("restaurantC");
        LOGGER.info("Risto " + rest.getSite());

        Dish dish = rest.menu.get(0);

        int[] minu = {0 , 15, 30, 45};
        int minM = 0;
		int maxM = 4;
		Random randomM = new Random();
		int i = randomM.nextInt(maxM + minM) + minM;
        int minutes = minu[i];

        int minH = 10;
		int maxH = 23;
        ThreadLocalRandom randomH = ThreadLocalRandom.current();
        int hour = randomH.nextInt(minH, maxH + 1);

        LocalTime time = LocalTime.of(hour, minutes);
        LOGGER.info("Orario Consegna: " + time);
        execution.setVariable("oraCons", time);
       
        //String indRisto = "Via Risto 33";
        //String indCliente = "Via Cliente 43";

        int minId = 1;
		int maxId = 10;
		Random random = new Random();
		int id = random.nextInt(maxId + minId) + minId;
		
        LOGGER.info("ID: " + id);
        OrderRestaurant body = new OrderRestaurant(id,rest.getName(),dish,time);


        execution.setVariable("idCons", id);
        Gson gson = new Gson();

        /**CALLING CONSAFF RIDER SERVICE**/
        String url = rest.getSite()+"/getAvailability";
        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        Client client = Client.create(clientConfig);
        WebResource webResource = client.resource(url);
        ClientResponse response =  webResource
               .accept(MediaType.APPLICATION_JSON_TYPE)
               .type(MediaType.APPLICATION_JSON_TYPE)
               .post(ClientResponse.class, gson.toJson(body));
        LOGGER.info("Availability Rest STATUS CODE:" + response.getStatus());
        //LOGGER.info( "hh" + response.getEntity(RestaurantAvailability.class));

        /**READ RESPONSE**/
        if(response.getStatus() == OK.getStatusCode()){
            RestaurantAvailability responseRest = response.getEntity(RestaurantAvailability.class);
            LOGGER.info(responseRest.isDisp());
            String disp = responseRest.isDisp();
            //(execution.setVariable("restaurantAvailable", responseRest.isDisp());
            //execution.setVariable("consegna", responseRest.getConsegna());
            if(responseRest.isDisp().equals("True")){
                    LOGGER.info(rest.getName() + " Disponibile");
                    execution.setVariable("restaurantAvailable", true);
            }
            else
                if(responseRest.isDisp().equals("False")){
                    LOGGER.info(rest.getName() + " Non disponibile");
                    execution.setVariable("restaurantAvailable", false);
            }
        } else {
            LOGGER.info("server error");
        }
    }
}