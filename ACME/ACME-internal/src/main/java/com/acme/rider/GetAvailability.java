package com.acme.rider;

import camundajar.impl.com.google.gson.Gson;
import com.acme.LoggerDelegate;
import com.acme.rider.utils.AvailabilityRequest;
import com.acme.utils.models.OrderRestaurant;
import com.acme.utils.models.Rider;
import com.acme.utils.models.RiderAvailability;
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

import static com.acme.utils.acmeVar.RESTAURANT_ORDER;
import static com.acme.utils.acmeVar.RIDER_URL;
import static com.sun.jersey.api.client.ClientResponse.Status.OK;



public class GetAvailability implements JavaDelegate {

    private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        /**GET CURRENT RIDER VARIABLE**/
        Rider rider = (Rider) execution.getVariable("rider");
        OrderRestaurant order = (OrderRestaurant) execution.getVariable(RESTAURANT_ORDER);
        Double bill = order.getTotalPrice();
        LOGGER.info("GET RIDER AVAILABILITY : "+ rider.getName());
        AvailabilityRequest availabilityRequest = new AvailabilityRequest(rider.getName(), bill, order.getNameRisto() ,order.getIndCliente());
        Gson g = new Gson();

        /**CALLING DISP RIDER SERVICE**/
        String url = RIDER_URL+"dispRider";
        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        Client client = Client.create(clientConfig);
        WebResource webResource = client.resource(url);
        ClientResponse response =  webResource
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .post(ClientResponse.class, g.toJson(availabilityRequest));

        /**READ RESPONSE**/
        if(response.getStatus() == OK.getStatusCode()){
            RiderAvailability responseRider = response.getEntity(RiderAvailability.class);
            execution.setVariable("riderAvailable", responseRider.isDisp());

            execution.setVariable("price", responseRider.getPrezzo());
            LOGGER.info("disp: " + responseRider.isDisp() + " price : "+ responseRider.getPrezzo());
            if(responseRider.isDisp() == true){
                rider.setPrice(responseRider.prezzo);
            }
        } else {
            LOGGER.info("server error");
        }
    }
}
