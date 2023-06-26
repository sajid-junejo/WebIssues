package dbConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    public static void main(String[] args) {
        Connection con = null;
        try {
            con = getConnection();
            if (con != null) {
            } else {
                System.out.println("Failed to connect to the database.");
            }
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Connection getConnection(){
        Connection con = null;
        String jdbcUrl = "jdbc:mysql://192.168.85.130/webissues";
        String user = "sajid.ali";
        String pass = "W3b1ssu3s!";
        int maxConnections = 100; 

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            DriverManager.setLoginTimeout(10);
            con = DriverManager.getConnection(jdbcUrl, user, pass);
            con.createStatement().execute("SET GLOBAL max_connections = " + maxConnections); 
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        return con;
    }
}
