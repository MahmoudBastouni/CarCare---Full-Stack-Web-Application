package servlet;

import dao.BookingDAO;
import model.Booking;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

@WebServlet("/api/bookings")
public class BookingServlet extends HttpServlet 
{
    private final BookingDAO bookingDAO = new BookingDAO();

    // POST: Submit a new booking
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	response.setContentType("application/json");
    	response.setHeader("Access-Control-Allow-Origin", "*");
        try 
        {
            String fullName = request.getParameter("fullName");
            String phone = request.getParameter("phone");
            String email = request.getParameter("email");
            String address = request.getParameter("address");
            int serviceId = Integer.parseInt(request.getParameter("serviceId"));
            String carType = request.getParameter("carType");
            String carModel = request.getParameter("carModel");
            String carColor = request.getParameter("carColor");
            Date bookingDate = Date.valueOf(request.getParameter("bookingDate"));
            String bookingTime = request.getParameter("bookingTime");
            String notes = request.getParameter("notes");

            Booking booking = new Booking(fullName, phone, email, address,
                                          serviceId, carType, carModel, carColor,
                                          bookingDate, bookingTime, notes);

            boolean success = bookingDAO.createBooking(booking);
            response.getWriter().write("{\"success\":" + success + "}");
        }
        catch (Exception e) 
        {
        	response.setStatus(500);
        	response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    // GET: Fetch bookings by phone number
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	response.setContentType("application/json");
    	response.setHeader("Access-Control-Allow-Origin", "*");
        String phone = request.getParameter("phone");
        try 
        {
            if (phone != null && !phone.isEmpty()) 
            {
                List<Booking> bookings = bookingDAO.getBookingsByPhone(phone);
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
            else 
            {
            	response.setStatus(400);
            	response.getWriter().write("{\"error\":\"Phone number required\"}");
            }
        } 
        catch (Exception e) 
        {
        	response.setStatus(500);
        	response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    // DELETE: Cancel a booking
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	response.setContentType("application/json");
    	response.setHeader("Access-Control-Allow-Origin", "*");
        String id = request.getParameter("id");
        try 
        {
            boolean success = bookingDAO.deleteBooking(Integer.parseInt(id));
            response.getWriter().write("{\"success\":" + success + "}");
        } 
        catch (Exception e) 
        {
        	response.setStatus(500);
        	response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}