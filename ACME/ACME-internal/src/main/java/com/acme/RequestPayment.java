package com.acme;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import com.acme.utils.models.Rider;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;

public class RequestPayment implements JavaDelegate{
    private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
       // LOGGER.info("PAGAMENTO");
              // create random object
        /*Random random = new Random();

        // get next next boolean value 
        boolean reqPayment = random .nextBoolean();

        execution.setVariable("payment", reqPayment);*/
        LOGGER.info("Richiesta pagamento");

        boolean reqPayment = false;

        /*Creo il totale da pagare: RIDER + MENU*/
        //Rider
        Rider rider = (Rider) execution.getVariable("cheaperRider");
        double costoRider = rider.getPrice();
        LOGGER.info("COSTO DEL RIDER: " + costoRider);

        //Menu
        /*OrderRestaurant order = (OrderRestaurant) execution.getVariable(RESTAURANT_ORDER);
        double dish = order.dish.getPrice();*/

        double costoOrdine = 10.0; /*Recuperarlo, non va settato così */
        LOGGER.info("COSTO DEL MENU: " + costoOrdine);
        double totalCost = costoRider + costoOrdine;
        LOGGER.info("TOTALE ORDINE: " + totalCost);
        execution.setVariable("totalCost", totalCost);
        
        if(costoOrdine != 0 && costoRider != 0){
            reqPayment = true;
            //execution.setVariable("token", USER_TOKEN);
            execution.setVariable("token", "UserBankToken");
        }

        execution.setVariable("payment", reqPayment);

    }

}
