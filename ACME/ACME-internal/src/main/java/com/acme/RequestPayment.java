package com.acme;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;

public class RequestPayment implements JavaDelegate{
    private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        LOGGER.info("PAGAMENTO");
              // create random object
        Random random = new Random();

        // get next next boolean value 
        boolean reqPayment = random .nextBoolean();

        execution.setVariable("payment", reqPayment);
    }

}
