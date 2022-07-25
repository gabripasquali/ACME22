package com.amce.acmeservice;


import camundajar.impl.com.google.gson.Gson;
import camundajar.impl.com.google.gson.GsonBuilder;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import javax.ws.rs.core.MediaType;
import javax.xml.transform.Result;
import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

@WebServlet("/abortOrder")
public class AbortOrder extends HttpServlet {
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession(true);

        //call ACME internal AKA camunda engine here
        Response s = new Response(true);
        Gson gson = new GsonBuilder().serializeNulls().create();
        sendResponse(response,  gson.toJson(s));
    }

    private void sendResponse(HttpServletResponse response, String s) throws IOException {
        PrintWriter out = response.getWriter();
        response.setContentType(MediaType.APPLICATION_JSON);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:8080");
        response.setHeader("Access-Control-Allow-Methods", "GET");
        out.print(s);
        out.flush();
    }

    public class Response{
        int code;
        String message;

        public Response(boolean b){
            if(b){
                this.code = 200;
                this.message = "order aborted";
            } else {
                this.code = 200;
                this.message = "order not aborted";
            }

        }
    }
}
