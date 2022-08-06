package com.acme;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.Random;

public class SetAvailability implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Random rando = new Random();
        execution.setVariable("restaurantAvailable", true);
    }
}
