package com.acme;

import com.acme.utilities.Rider;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.ArrayList;
import java.util.logging.Logger;

public class CheaperRider implements JavaDelegate{
    private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        LOGGER.info("Rider prezzo più basso");
        ArrayList<Rider> availableRider = (ArrayList<Rider>) execution.getVariable("riderAvailableList");

        // se la lista non è vuota calcolo prezzo migliore
        double minPrice = 0; 
        Rider cRider = availableRider.get(0);
        for(int i = 0; i < availableRider.size(); i++) {
            double price = availableRider.get(i).getPrice();
            if(price < minPrice){
                cRider = availableRider.get(i);
            }
        }
        System.out.println("Rider selezionato: " + cRider.getName()+ " Prezzo: " + cRider.getPrice());
        execution.setVariable("cheaperRider", cRider);
        
        
    }
}
