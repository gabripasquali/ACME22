package com.acme.abort;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import com.acme.LoggerDelegate;

import java.util.logging.Logger;

public class AbortOrderRest implements JavaDelegate {
    private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        LOGGER.info("Task abort order Cliente e Ristorante");

        LOGGER.info("1) Task abort order Cliente");

        LOGGER.info("2) Task abort order Ristorante ");
    }
}
