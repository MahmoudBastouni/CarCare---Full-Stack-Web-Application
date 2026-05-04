package servlet;

import dao.ServiceDAO;
import model.Service;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/services")
public class GetServicesServlet extends HttpServlet 
{
    private final ServiceDAO serviceDAO = new ServiceDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException 
    {
        response.setContentType("application/json");
        response.setHeader("Access-Control-Allow-Origin", "*");
        try 
        {
            List<Service> services = serviceDAO.getAllServices();
            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < services.size(); i++) 
            {
                Service s = services.get(i);
                json.append("{")
                    .append("\"serviceId\":").append(s.getServiceId()).append(",")
                    .append("\"title\":\"").append(s.getTitle()).append("\",")
                    .append("\"description\":\"").append(s.getDescription()).append("\",")
                    .append("\"label\":\"").append(s.getLabel()).append("\",")
                    .append("\"price\":").append(s.getPrice()).append(",")
                    .append("\"duration\":\"").append(s.getDurationText()).append("\",")
                    .append("\"features\":\"").append(s.getFeatures()).append("\",")
                    .append("\"icon\":\"").append(s.getIcon()).append("\"")
                    .append("}");
                if (i < services.size() - 1) json.append(",");
            }
            json.append("]");
            response.getWriter().write(json.toString());
        } 
        catch (Exception e) 
        {
            response.setStatus(500);
            response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}