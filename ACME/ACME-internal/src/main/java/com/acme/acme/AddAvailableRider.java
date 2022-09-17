package com.acme.acme;

import com.acme.LoggerDelegate;
import com.acme.utils.models.Rider;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.ArrayList;
import java.util.logging.Logger;

public class AddAvailableRider implements JavaDelegate {
    private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());


    @Override
    public void execute(DelegateExecution execution) throws Exception {
        LOGGER.info("Add available rider to list");
        Rider rider = (Rider) execution.getVariable("rider");
        ArrayList<Rider> availableRider = (ArrayList<Rider>) execution.getVariable("riderAvailableList");
        availableRider.add(rider);
        execution.setVariable("riderAvailableList", availableRider);   
    }
}
