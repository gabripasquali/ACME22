package com.acme.acme;

import camundajar.impl.com.google.gson.Gson;

import com.acme.LoggerDelegate;
import com.acme.utils.Rider;
import com.acme.utils.RiderInZone;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.logging.Logger;

import static com.sun.jersey.api.client.ClientResponse.Status.OK;

public class FindRider implements JavaDelegate {
    private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());
    ArrayList<Rider> riderList = new ArrayList<Rider>(){
        {
            add(new Rider("Redir", "http://localhost:10002", 44.4992, 11.330));
            add(new Rider("Riderin", "http://localhost:10003", 44.4994439, 11.3297796));
            add(new Rider("Astro", "http://localhost:10004",44,  11));
            add(new Rider("B4L", "http://localhost:10005", 44.28, 11.55));
        }
    };


    @Override
    public void execute(DelegateExecution execution) throws Exception {
        //distanza costante fissata a 10km per specifiche progetto
        int distance = 10;

        /**lettura parametri della richiesta, posizione utente/ristorante**/
        //lat = execution.getVariable("lat")
        //lng = execution.getVariable("lng" )
        double lat =  11.3297796;
        double lng =  44.4994439;

        /**richiesta lista rider al db e creazione del body**/
        Gson gson = new Gson();
        RiderDistanceRequest body = new RiderDistanceRequest(distance, lat, lng);
        LOGGER.info("FIND RIDER BODY: "+ gson.toJson(body).toString());

        /**chiamata a GIS**/
        String url = "http://localhost:10000/isInDistance";
        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        Client client = Client.create(clientConfig);
        WebResource webResource = client.resource(url);
        ClientResponse response =  webResource
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .post(ClientResponse.class, gson.toJson(body));
        LOGGER.info("FIND RIDER STATUS CODE:" + response.getStatus());

        if(response.getStatus() == OK.getStatusCode()){
            //TODO read data from response, deserialize and use to create new list
            /**creazione della lista per task parallelo**/
            RiderInZone riderInZoneRes = response.getEntity(RiderInZone.class);
            ArrayList<Rider> riderInZoneList = new ArrayList<>();
            //join info from riderList and riderInZoneList
            for (Rider r : riderList){
                for(Rider rIZ : riderInZoneRes.results){
                    if(r.name.equals(rIZ.name) && rIZ.isInDistance){
                        riderInZoneList.add(r);
                    }
                }
            }

            //list to iterate on
            execution.setVariable("riderInZoneList", riderInZoneList);
            //list to fill with rider available
            execution.setVariable("riderAvailableList", new ArrayList<Rider>());
        }
    }

    private class RiderDistanceRequest{
        int distance;
        double lat;
        double lng;
        ArrayList<Rider> listarider;
        public RiderDistanceRequest(int distance, double lat, double lng) {
            this.lat = lat;
            this.lng = lng;
            this.distance = distance;
            //list retrieved from db
            listarider = riderList;
        }
    }
}
