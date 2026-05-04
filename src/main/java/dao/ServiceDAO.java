package dao;

import model.Service;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceDAO 
{

    public List<Service> getAllServices() throws SQLException 
    {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT * FROM services";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) 
        {
            while (rs.next()) 
            {
                services.add(mapRow(rs));
            }
        }
        return services;
    }

    public Service getServiceById(int id) throws SQLException 
    {
        String sql = "SELECT * FROM services WHERE service_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) 
        {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) 
            {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    public boolean updateServicePrice(int serviceId, double newPrice) throws SQLException 
    {
        String sql = "UPDATE services SET price = ? WHERE service_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) 
        {
            ps.setDouble(1, newPrice);
            ps.setInt(2, serviceId);
            return ps.executeUpdate() > 0;
        }
    }

    private Service mapRow(ResultSet rs) throws SQLException 
    {
        return new Service(
            rs.getInt("service_id"),
            rs.getString("title"),
            rs.getString("description"),
            rs.getString("label"),
            rs.getDouble("price"),
            rs.getString("duration_text"),
            rs.getString("features"),
            rs.getString("icon")
        );
    }
}