package utils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class ACMEbeServlet extends HttpServlet {

    private void SendResponse(HttpServletResponse httpServletResponse, String response) throws IOException {
        PrintWriter out = httpServletResponse.getWriter();
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON);
        httpServletResponse.setCharacterEncoding(StandardCharsets.UTF_8.name());
        setAccessControlHeaders(httpServletResponse);
        out.println(response);
        out.flush();
    }

    private void setAccessControlHeaders(HttpServletResponse httpServletResponse) {
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "http://localhost:8080");
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET");
    }

}
