package com.acme;

import camundajar.impl.com.google.gson.Gson;

import com.acme.utils.models.OrderRider;
import com.acme.utils.models.Restaurant;
import com.acme.utils.models.Rider;
import com.acme.utils.models.RiderConsResp;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.joda.time.Seconds;
import org.camunda.bpm.engine.delegate.DelegateExecution;


import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.logging.Logger;
import static com.acme.utils.acmeVar.*;

import com.acme.utils.models.OrderRestaurant;

import javax.ws.rs.core.MediaType;

import static com.sun.jersey.api.client.ClientResponse.Status.OK;


public class OrderOp implements JavaDelegate{
    private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());


    @Override
    public void execute(DelegateExecution execution) throws Exception {
        LOGGER.info("Ordine Operativo, comunicazione a Rider");

        Rider rider = (Rider) execution.getVariable("cheaperRider");
        LOGGER.info("Recupero cheaper Rider: " + rider.getName());

        Restaurant ris = (Restaurant) execution.getVariable("restaurantC");
        OrderRestaurant order = (OrderRestaurant) execution.getVariable(RESTAURANT_ORDER);
        String indRisto = ris.address;
        String indCliente = order.indCliente;
        String time = order.oraCons;
        LOGGER.info("Orario Consegna: " + time);
        //execution.setVariable("oraCons", time);
        int id =  (int) execution.getVariable("idCons");


        Gson gson = new Gson();
        OrderRider body =  new OrderRider(id, time, indRisto, indCliente);

         /**CALLING CONSAFF RIDER SERVICE**/
         String url = RIDER_URL+"/consAff";
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

                /*se il rider prende in carico la consegna viene settata la variabile dell'orario per controllare successivamente
                 l'abort order del cliente */
                //OrderRestaurant order = (OrderRestaurant) execution.getVariable(RESTAURANT_ORDER);
                LocalTime localTime = LocalTime.parse(order.oraCons);

                Instant orderCancellationTime = Instant.now()
                    .atZone(ZoneId.of( "Europe/Paris" ))
                    .withHour(localTime.getHour() - 1)
                    .withMinute(localTime.getMinute())
                    .withSecond(localTime.getSecond())
                    .toInstant();
                LOGGER.info("Delivery Time: " + orderCancellationTime.toString());
                //orderCancellationTime = orderCancellationTime.truncatedTo(ChronoUnit.SECONDS);
                Instant currentTime = Instant.now()
                    .atZone(ZoneId.of("Europe/Paris"))
                    .toInstant();
                LOGGER.info("ORA ATTUALE: " + currentTime.toString());
                int hour = orderCancellationTime.atZone(ZoneId.of( "Europe/Paris" )).getHour();
                int minutes = orderCancellationTime.atZone(ZoneId.of( "Europe/Paris" )).getMinute();
                int currentHour = currentTime.atZone(ZoneId.of( "Europe/Paris" )).getHour();
                int currentMinute = orderCancellationTime.atZone(ZoneId.of( "Europe/Paris" )).getMinute();

                if (!(currentHour > hour || (currentHour == hour && currentMinute > minutes))) {
                    execution.setVariable(DELIVERY_TIME, orderCancellationTime.toString());
                }
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
