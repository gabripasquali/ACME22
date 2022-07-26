package com.acme.restaurant;

import com.acme.LoggerDelegate;
import com.acme.utils.DataBaseCons;
import com.acme.utils.Database;
import com.acme.utils.models.OrderRestaurant;
import com.acme.utils.models.Restaurant;
import com.acme.utils.models.RestaurantAvailability;
import com.acme.utils.models.RestaurantList;
import com.acme.utils.models.Status;
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

import static com.acme.utils.acmeVar.*;


import java.util.ArrayList;
import java.util.Random;

import java.util.logging.Logger;
import java.util.stream.Collectors;

public class GetAvailability implements JavaDelegate{
    private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        LOGGER.info("Disponibilità ristorante");
        Database db = new Database();
        OrderRestaurant orderR = (OrderRestaurant) execution.getVariable(RESTAURANT_ORDER);
        LOGGER.info(orderR.instanceId);
        
        String name = orderR.getNameRisto();

        Restaurant rest = getResByName(name, db);
        execution.setVariable("restaurantC", rest);
        LOGGER.info("Risto " + RESTAURANT_URL);
        DataBaseCons dbc = new DataBaseCons();
        
        int checkId = dbc.lastId(dbc);
        LOGGER.info("ID vecchio: " + checkId);
        int id = 0;

        if(checkId == 0){
            id = 1;
        }else{
            id = checkId + 1;
        }
        
        LOGGER.info("ID: " + id);

        execution.setVariable("idCons", id);
        Gson gson = new Gson();

        
        dbc.modifyStatus(1, dbc, Status.DELIVERED);

        /**CALLING GETAVAILABILITY RISTO SERVICE**/
        String url = RESTAURANT_URL+"/getAvailability";
        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        Client client = Client.create(clientConfig);
        WebResource webResource = client.resource(url);
        ClientResponse response =  webResource
               .accept(MediaType.APPLICATION_JSON_TYPE)
               .type(MediaType.APPLICATION_JSON_TYPE)
               .post(ClientResponse.class, gson.toJson(orderR));
        LOGGER.info("Availability Rest STATUS CODE:" + response.getStatus());
        LOGGER.info("++ res mediatype:" + response.getType().toString());

        /**READ RESPONSE**/
        if(response.getStatus() == OK.getStatusCode()){
            RestaurantAvailability responseRest = response.getEntity(RestaurantAvailability.class);
            LOGGER.info(responseRest.isDisp());

            if(responseRest.isDisp().equals("True")){
                    LOGGER.info(rest.getName() + " Disponibile");
                    execution.setVariable("restaurantAvailable", true);
                    execution.setVariable(RESTAURANTAV, true);
            }
            else
                if(responseRest.isDisp().equals("False")){
                    LOGGER.info(rest.getName() + " Non disponibile");
                    execution.setVariable("restaurantAvailable", false);
                    execution.setVariable(RESTAURANTAV, false);
            }
        } else {
            LOGGER.info("server error");
        }
    } 
 
    private Restaurant getResByName(String name, Database db) {
        return db.restaurants.stream()
                .filter(restaurant -> name.equals(restaurant.name))
                .findAny()
                .orElse(new Restaurant());

    }
    
    
}