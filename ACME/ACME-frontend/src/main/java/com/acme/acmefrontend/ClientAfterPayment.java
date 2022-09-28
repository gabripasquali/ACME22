package com.acme.acmefrontend;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/ClientAfterPayment")
public class ClientAfterPayment extends HttpServlet {
	
	@Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        RequestDispatcher dispatcher = request.getServletContext().getRequestDispatcher("/AfterPaymentPage.jsp");
        dispatcher.forward(request, response);
    }
    
    

}

