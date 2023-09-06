package dbConnection;

import java.sql.DriverManager;
import java.sql.Connection;
import pojos.Path;
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
        String addres = Path.getInstance().getAddress();
        Connection con = null;
       String jdbcUrl = "";
       String user = "";
       String pass = "";
       if(addres.equals("https://webissues-new.genetechz.com/"))
       {
            jdbcUrl = "jdbc:mysql://192.168.14.2/test-webissues";
            user = "root";
            pass = "MMp9ug6e";
       }
       else if(addres.equals("https://192.168.85.130/webissues/"))
       {
                jdbcUrl = "jdbc:mysql://192.168.85.130/webissues";
                user = "sajid";
                pass = "1234";
       }
       
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
