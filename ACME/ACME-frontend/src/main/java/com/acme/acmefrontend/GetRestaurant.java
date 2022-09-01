package com.acme.acmefrontend;

import com.google.gson.Gson;

//import com.acme.acmefrontend.utils.AcmeProcessEngine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.camunda.bpm.ProcessEngineService;
import org.camunda.bpm.engine.ProcessEngine;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/getRestaurant")
public class GetRestaurant extends HttpServlet {
    @Inject
    public ProcessEngine processEngine;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /**Call camunda engine**/

        ProcessEngine process = processEngine;
        Map<String, Object> cityVariable = new HashMap<>();

        cityVariable.put("city", request.getParameter("city"));
        //String processInstanceId = process.toString();
          //      .getProcessInstanceId();

        String url = "http://localhost:8080/engine-rest/message";
        String json = "{\"messageName\" : \"GetRestaurant\","+
                        "\"businessKey\" : \"1\""+
                        "\"processVariables\" : {" +
                        "\"city\" : { \"value\" : \"" + request.getParameter("city") +"\", \"type\" : \"String\"}" +
                        "}"+
                        "}";
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        StringEntity entity = new StringEntity(json);
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        CloseableHttpResponse res = client.execute(httpPost);


        /**set response**/
        Gson gson = new Gson();
        ArrayList<Restaurant> restaurants = new ArrayList<>();
        ArrayList<String> menuA = new ArrayList<>();
        menuA.add("menu1");
        menuA.add("menu2");
        menuA.add("menu3");
        ArrayList<String> menuC = new ArrayList<>();
        menuC.add("meNu1");
        menuC.add("meNu2");
        menuC.add("meNu3");
        restaurants.add(new Restaurant("arturo", menuA));
        restaurants.add(new Restaurant("carlo", menuC));
        sendResponse(response, gson.toJson(restaurants));
    }
    private void sendResponse(HttpServletResponse resp, String response) throws IOException {
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        out.print(response);
        out.flush();
    }

    private class Restaurant {
        String name;
        ArrayList<String> menu;
        Restaurant(String name, ArrayList<String> menu){
            this.menu = menu;
            this.name = name;
        }
    }
}
