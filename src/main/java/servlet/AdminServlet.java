package servlet;

import dao.AdminDAO;
import dao.BookingDAO;
import dao.ServiceDAO;
import model.Booking;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/admin")
public class AdminServlet extends HttpServlet 
{
    private final AdminDAO adminDAO = new AdminDAO();
    private final BookingDAO bookingDAO = new BookingDAO();
    private final ServiceDAO serviceDAO = new ServiceDAO();

    // POST: Login
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setHeader("Access-Control-Allow-Origin", "*");
        try 
        {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            boolean valid = adminDAO.validateAdmin(username, password);
            if (valid) 
            {
                HttpSession session = request.getSession(true);
                session.setAttribute("isAdmin", true);
                response.getWriter().write("{\"success\":true}");
            } 
            else 
            {
                response.setStatus(401);
                response.getWriter().write("{\"success\":false,\"error\":\"Invalid credentials\"}");
            }
        } 
        catch (Exception e) 
        {
            response.setStatus(500);
            response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    // GET: Get all bookings (admin only)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setHeader("Access-Control-Allow-Origin", "*");
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("isAdmin") == null) 
        {
        	response.setStatus(403);
        	response.getWriter().write("{\"error\":\"Unauthorized\"}");
            return;
        }
        try 
        {
            List<Booking> bookings = bookingDAO.getAllBookings();
            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < bookings.size(); i++) 
            {
                Booking b = bookings.get(i);
                json.append("{")
                    .append("\"bookingId\":").append(b.getBookingId()).append(",")
                    .append("\"fullName\":\"").append(b.getFullName()).append("\",")
                    .append("\"phone\":\"").append(b.getPhone()).append("\",")
                    .append("\"email\":\"").append(b.getEmail() != null ? b.getEmail() : "").append("\",")
                    .append("\"address\":\"").append(b.getAddress()).append("\",")
                    .append("\"serviceId\":").append(b.getServiceId()).append(",")
                    .append("\"carType\":\"").append(b.getCarType()).append("\",")
                    .append("\"carModel\":\"").append(b.getCarModel() != null ? b.getCarModel() : "").append("\",")
                    .append("\"carColor\":\"").append(b.getCarColor() != null ? b.getCarColor() : "").append("\",")
                    .append("\"bookingDate\":\"").append(b.getBookingDate()).append("\",")
                    .append("\"bookingTime\":\"").append(b.getBookingTime()).append("\",")
                    .append("\"notes\":\"").append(b.getNotes() != null ? b.getNotes() : "").append("\",")
                    .append("\"status\":\"").append(b.getStatus()).append("\"")
                    .append("}");
                if (i < bookings.size() - 1) json.append(",");
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

    // POST to /api/admin/update: Update price or booking status
    // We reuse doPost and check for an "action" parameter
    // action=updatePrice → serviceId + price
    // action=updateStatus → bookingId + status
    // action=login → username + password (handled above by default)
    // To keep it simple we check the "action" param:
    private void handleUpdate(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("isAdmin") == null) 
        {
        	response.setStatus(403);
        	response.getWriter().write("{\"error\":\"Unauthorized\"}");
            return;
        }
        try 
        {
            String action = request.getParameter("action");
            boolean success = false;
            if ("updatePrice".equals(action)) 
            {
                int serviceId = Integer.parseInt(request.getParameter("serviceId"));
                double price = Double.parseDouble(request.getParameter("price"));
                success = serviceDAO.updateServicePrice(serviceId, price);
            } 
            else if ("updateStatus".equals(action)) 
            {
                int bookingId = Integer.parseInt(request.getParameter("bookingId"));
                String status = request.getParameter("status");
                success = bookingDAO.updateBookingStatus(bookingId, status);
            }
            response.getWriter().write("{\"success\":" + success + "}");
        } 
        catch (Exception e) 
        {
        	response.setStatus(500);
        	response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    // Override doPost to route between login and update actions
    // If action param is present it's an update, otherwise it's a login
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws jakarta.servlet.ServletException, IOException {
    	response.setContentType("application/json");
    	response.setHeader("Access-Control-Allow-Origin", "*");
        String action = request.getParameter("action");
        if ("POST".equals(request.getMethod()) && action != null) 
        {
            handleUpdate(request, response);
        }
        else 
        {
            super.service(request, response);
        }
    }
}