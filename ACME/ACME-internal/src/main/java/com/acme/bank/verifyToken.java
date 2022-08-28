package com.acme.bank;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import com.acme.LoggerDelegate;

import java.util.Random;

import java.util.ArrayList;
import java.util.logging.Logger;

public class verifyToken implements JavaDelegate{
    private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        LOGGER.info("Controllo Token");
        // create random object
        Random random = new Random();

        // get next next boolean value 
        boolean verToken = random.nextBoolean();

        execution.setVariable("TokenOk", verToken);

        /*if (verToken == true){
            // create random object
            Random randomO = new Random();

            // get next next boolean value 
            boolean order = randomO.nextBoolean();

            execution.setVariable("OrderOk", order);

        }*/
    }
}
