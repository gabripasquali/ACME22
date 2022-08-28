package com.acme.restaurant;

import com.acme.LoggerDelegate;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.Random;

import java.util.logging.Logger;

public class GetAvailability implements JavaDelegate{
    private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        LOGGER.info("Disponibilità ristorante");

        int minId = 1;
		int maxId = 10;
		Random random = new Random();
		int id = random.nextInt(maxId + minId) + minId;
		LOGGER.info("ID: " + id);

        execution.setVariable("idCons", id);
        //execution.setVariable("restaurantAvailable", true);

    }
}