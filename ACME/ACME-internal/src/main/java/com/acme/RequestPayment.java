package com.acme;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import com.acme.utils.DataBaseCons;
import com.acme.utils.models.Dish;
import com.acme.utils.models.OrderInfo;
import com.acme.utils.models.OrderRestaurant;
import com.acme.utils.models.Restaurant;
import com.acme.utils.models.Rider;
import com.acme.utils.models.Status;

import camundajar.impl.com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import static com.acme.utils.acmeVar.*;


public class RequestPayment implements JavaDelegate{
    private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        LOGGER.info("PAGAMENTO");
        execution.setVariable("nTryToken", 0);
         // create random object
        Random random = new Random();
        // get next next boolean value 
        boolean reqPayment = random .nextBoolean();
        execution.setVariable("payment", reqPayment);
         
        int id = (int) execution.getVariable("idCons");
        OrderRestaurant order = (OrderRestaurant) execution.getVariable(RESTAURANT_ORDER);
        String restaurant = order.getNameRisto();
        List<Dish> dishes = order.getDishes();
        Double price = (Double) execution.getVariable(PRICETOT);
        String oraCons = order.getOraCons();
        String indCliente = order.getIndCliente();
        Rider rider = (Rider) execution.getVariable(RIDER);
        String riderName = rider.getName();
        
        Status status = Status.IN_PROGRESS;
        OrderInfo info = new OrderInfo(id, restaurant, dishes, price, oraCons, indCliente, riderName,status);

        DataBaseCons db = new DataBaseCons();
        db.addCosegna(info, db);
    
    }

}
