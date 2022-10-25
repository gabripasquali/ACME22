package com.acme.bank;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import com.acme.LoggerDelegate;
import com.acme.bank.generated.*;
import static com.acme.utils.acmeVar.*;

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

        String token = (String) execution.getVariable(USER_TOKEN);

        try {
            Bank bankService = new BankService().getBankServicePort();
            VerifyToken verifyToken = new VerifyToken();
            verifyToken.setSid(token);
            VerifyTokenResponse resp = bankService.verifyToken(token);
            execution.setVariable(IS_VALID_TOKEN, resp.isSuccess());
            LOGGER.info("IsValidToken: " + resp.isSuccess());
            execution.setVariable(IS_UNREACHABLE_BANK_SERVICE, false);
        } catch (Exception e) {
            e.printStackTrace();
            execution.setVariable(IS_VALID_TOKEN, false);
            execution.setVariable(IS_UNREACHABLE_BANK_SERVICE, true);
            LOGGER.info("IsValidToken: false");
        }
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
