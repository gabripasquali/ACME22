package com.acme.acme;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import com.acme.LoggerDelegate;

import java.util.Random;

import java.util.ArrayList;
import java.util.logging.Logger;

public class GetRestByCity implements JavaDelegate{
    private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        LOGGER.info("Ristorante in base a città");

    }
}
