package dao;

import util.DBConnection;
import java.sql.*;

public class AdminDAO 
{

    public boolean validateAdmin(String username, String password) throws SQLException 
    {
        String sql = "SELECT * FROM admins WHERE username = ? AND password_hash = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) 
        {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) 
            {
                return rs.next();
            }
        }
    }
}