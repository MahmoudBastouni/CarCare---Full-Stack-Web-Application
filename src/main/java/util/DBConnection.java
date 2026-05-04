package util;

import java.io.InputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection
{
    public static Connection getConnection() throws SQLException {
        String URL;
        String USER;
        String PASSWORD;

        Properties prop = new Properties();

        try (InputStream input = DBConnection.class.getClassLoader().getResourceAsStream("util/.env")) {
            
            if (input == null) {
                throw new SQLException(".env file not found. Make sure it is inside src/main/java/util/.env");
            }

            prop.load(input);

            URL = prop.getProperty("DB_URL");
            USER = prop.getProperty("DB_USER");
            PASSWORD = prop.getProperty("DB_PASSWORD");

            System.out.println("URL: " + URL);
            System.out.println("Username: " + USER);
            System.out.println("Password: " + PASSWORD);
        } 
        catch (IOException e) {
            throw new SQLException("Error loading .env file", e);
        }

        if (URL == null || USER == null || PASSWORD == null) {
            throw new SQLException(".env parameters not loaded. Check DB_URL, DB_USER, and DB_PASSWORD.");
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } 
        catch (ClassNotFoundException e) {
            throw new SQLException("MySQL driver not found", e);
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}