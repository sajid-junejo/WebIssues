package dbConnection;

import java.sql.DriverManager;
import java.sql.Connection;
public class DbConnection {
    public static void main(String[] args) {
        Connection con = getConnection();
        if (con != null) {
            System.out.println("Connection successful!");
            // Perform any additional database operations here
        } else {
            System.out.println("Failed to connect to the database.");
        }
    }

    public static Connection getConnection() {
        Connection con = null;
       String jdbcUrl = "jdbc:mysql://192.168.85.130/webissues";
       String user = "sajid";
       String pass = "1234";
       
       try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = (Connection) DriverManager.getConnection(jdbcUrl, user, pass);
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        return con;
    }
}
