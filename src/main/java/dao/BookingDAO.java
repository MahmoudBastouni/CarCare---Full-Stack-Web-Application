package dao;

import model.Booking;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO 
{

    // CREATE
    public boolean createBooking(Booking b) throws SQLException 
    {
        String sql = "INSERT INTO bookings (full_name, phone, email, address, service_id, " +
                     "car_type, car_model, car_color, booking_date, booking_time, notes, status) " +
                     "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) 
        {
            ps.setString(1, b.getFullName());
            ps.setString(2, b.getPhone());
            ps.setString(3, b.getEmail());
            ps.setString(4, b.getAddress());
            ps.setInt(5, b.getServiceId());
            ps.setString(6, b.getCarType());
            ps.setString(7, b.getCarModel());
            ps.setString(8, b.getCarColor());
            ps.setDate(9, b.getBookingDate());
            ps.setString(10, b.getBookingTime());
            ps.setString(11, b.getNotes());
            ps.setString(12, b.getStatus());
            return ps.executeUpdate() > 0;
        }
    }

    // READ all
    public List<Booking> getAllBookings() throws SQLException 
    {
        List<Booking> list = new ArrayList<>();
        String sql = "SELECT * FROM bookings ORDER BY created_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) 
        {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    // READ by phone (for user to look up their own bookings)
    public List<Booking> getBookingsByPhone(String phone) throws SQLException 
    {
        List<Booking> list = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE phone = ? ORDER BY booking_date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) 
        {
            ps.setString(1, phone);
            try (ResultSet rs = ps.executeQuery()) 
            {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    // UPDATE status
    public boolean updateBookingStatus(int bookingId, String status) throws SQLException 
    {
        String sql = "UPDATE bookings SET status = ? WHERE booking_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) 
        {
            ps.setString(1, status);
            ps.setInt(2, bookingId);
            return ps.executeUpdate() > 0;
        }
    }

    // DELETE
    public boolean deleteBooking(int bookingId) throws SQLException 
    {
        String sql = "DELETE FROM bookings WHERE booking_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) 
        {
            ps.setInt(1, bookingId);
            return ps.executeUpdate() > 0;
        }
    }

    private Booking mapRow(ResultSet rs) throws SQLException 
    {
        return new Booking(
            rs.getInt("booking_id"),
            rs.getString("full_name"),
            rs.getString("phone"),
            rs.getString("email"),
            rs.getString("address"),
            rs.getInt("service_id"),
            rs.getString("car_type"),
            rs.getString("car_model"),
            rs.getString("car_color"),
            rs.getDate("booking_date"),
            rs.getString("booking_time"),
            rs.getString("notes"),
            rs.getString("status")
        );
    }
}