package com.acme.rider;

import com.acme.LoggerDelegate;
import com.acme.utilities.Rider;
import com.acme.utilities.RiderAvailability;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import javax.ws.rs.core.MediaType;
import java.io.Serializable;
import java.util.logging.Logger;

import static com.sun.jersey.api.client.ClientResponse.Status.OK;

public class GetAvailability implements JavaDelegate {

    private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Rider rider = (Rider) execution.getVariable("rider");
        LOGGER.info("GET RIDER AVAILABILITY : "+ rider.getName());
        execution.setVariable("riderAvailable", true);
        //call rider server with url
        String url = rider.getSite()+"/dispRider";
        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        Client client = Client.create(clientConfig);
        WebResource webResource = client.resource(url);
        ClientResponse response =  webResource
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .get(ClientResponse.class);
        if(response.getStatus() == OK.getStatusCode()){
            RiderAvailability responseRider = response.getEntity(RiderAvailability.class);
            execution.setVariable("riderAvailable", responseRider.isDisp());
            execution.setVariable("price", responseRider.getPrezzo());
            LOGGER.info("disp: " + responseRider.isDisp() + " price : "+ responseRider.getPrezzo());
        } else {
            LOGGER.info("server error");
        }
        //execute.setVariable("riderAvailable", response.riderAvailability)
        //execute.setVariable("price", response.price)
    }
}
