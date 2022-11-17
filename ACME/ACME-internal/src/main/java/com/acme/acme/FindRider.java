package com.acme.acme;

import camundajar.impl.com.google.gson.Gson;

import com.acme.LoggerDelegate;
import com.acme.utils.Database;
import com.acme.utils.models.OrderRestaurant;
import com.acme.utils.models.Restaurant;
import com.acme.utils.models.Rider;
import com.acme.utils.models.RiderInZone;
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
import java.util.List;
import java.util.logging.Logger;

import static com.acme.utils.acmeVar.GIS_URL;
import static com.acme.utils.acmeVar.RESTAURANT_ORDER;
import static com.sun.jersey.api.client.ClientResponse.Status.OK;

public class FindRider implements JavaDelegate {
    private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        //distanza costante fissata a 10km per specifiche progetto
        int distance = 10;
        Database db = new Database();

        /**lettura parametri della richiesta, posizione ristorante**/
        OrderRestaurant orderRestaurant = (OrderRestaurant) execution.getVariable(RESTAURANT_ORDER);
        Restaurant res = getResByName(orderRestaurant.getNameRisto(), db);
        Double lat = res.lat;
        Double lng = res.lng;

        /**richiesta lista rider al db e creazione del body**/

        Gson gson = new Gson();
        List<Rider> riderList = db.riderList;
        RiderDistanceRequest body = new RiderDistanceRequest(distance, lat, lng, riderList);
        LOGGER.info("FIND RIDER BODY: "+ gson.toJson(body).toString());

        /**chiamata a GIS**/

        String url = GIS_URL+"isInDistance";
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
            /**creazione della lista per task parallelo**/
            RiderInZone riderInZoneRes = response.getEntity(RiderInZone.class);
            ArrayList<Rider> riderInZoneList = new ArrayList<>();
            //join info from riderList and riderInZoneList
            for (Rider r : riderList){
                for(Rider rIZ : riderInZoneRes.results){
                    if(r.name.equals(rIZ.name) && rIZ.isInDistance){
                        LOGGER.info(r.name);
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

    private Restaurant getResByName(String nameRisto, Database db) {
        return db.restaurants.stream()
                .filter(restaurant -> nameRisto.equals(restaurant.name))
                .findAny()
                .orElse(new Restaurant());
    }

    private class RiderDistanceRequest{
        int distance;
        double lat;
        double lng;
        List<Rider> listarider;
        public RiderDistanceRequest(int distance, double lat, double lng, List<Rider> riderList) {
            this.lat = lat;
            this.lng = lng;
            this.distance = distance;
            listarider = riderList;
        }
    }
}
