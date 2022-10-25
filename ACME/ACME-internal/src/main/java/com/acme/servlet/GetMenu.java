package com.acme.servlet;

import camundajar.impl.com.google.gson.Gson;
import com.acme.utils.ApiHttpServlet;
import com.acme.utils.Database;
import com.acme.utils.models.Dish;
import com.acme.utils.models.Restaurant;
import com.acme.utils.models.RestaurantMenu;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/getMenu")
public class GetMenu extends ApiHttpServlet {
    private final Logger LOGGER = LogManager.getLogger(this.getClass());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String resName = request.getParameter("res");
        Database database = new Database();
        Restaurant res = database.restaurants
                .stream()
                .filter(restaurant -> resName.equals(restaurant.name))
                .findAny()
                .orElse(new Restaurant());

        List<Dish> menu = res.menu;
        Gson gson = new Gson();
        sendResponse(response, gson.toJson(menu));
    }
}
