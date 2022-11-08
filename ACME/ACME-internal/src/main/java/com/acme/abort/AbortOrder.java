package com.acme.abort;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import com.acme.LoggerDelegate;
import com.acme.utils.DataBaseCons;
import com.acme.utils.models.Status;

import java.util.logging.Logger;


public class AbortOrder implements JavaDelegate {
    private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        LOGGER.info("1) Task abort order Cliente");

       
    }
}
