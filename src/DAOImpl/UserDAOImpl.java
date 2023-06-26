package DAOImpl;

import DAO.UserDAO;
import dbConnection.DbConnection;
import pojos.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAOImpl implements UserDAO {

    @Override
    public User getUserByLoginAndPassword(String login, String password) {
        User user = null;
        try {
            Connection con = DbConnection.getConnection();
            PreparedStatement pst = con.prepareStatement("SELECT user_login, user_passwd FROM users WHERE user_login=? AND user_passwd=?");
            pst.setString(1, login);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                user = new User(rs.getString("user_login"), rs.getString("user_passwd"));
            }
            rs.close();
            pst.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
}
