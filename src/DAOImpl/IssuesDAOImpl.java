package DAOImpl;

import DAO.IssuesDAO;
import dbConnection.DbConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import pojos.SessionManager;
public class IssuesDAOImpl implements IssuesDAO {

    private List<String> columnNames;

    @Override
    public List<String> getColumnNames() {
        return columnNames;
    }

    @Override
    public DefaultTableModel getIssuesByTypeId(int typeId, String folderName) {
        DefaultTableModel tableModel = null;
        Connection con = null;
        Statement statement = null;
        ResultSet resultSet = null;

        if (typeId == 2) {
            try {
                con = DbConnection.getConnection();
                statement = con.createStatement();
                String issueQuery = "SELECT i.issue_id AS ID, "
                        + "i.issue_name AS ISSUE_NAME, "
                        + "CONCAT(p.project_name, ' - ', f.folder_name) AS LOCATION, "
                        + "DATE_FORMAT(CONVERT_TZ(FROM_UNIXTIME(s_created.stamp_time), '+00:00', '+05:00'), '%m/%d/%Y %h:%i:%s %p') AS `Modified Date`, "
                        + "u.user_name AS `Modified By`, "
                        + "av_assigned.attr_value AS `ASSIGNED TO`, "
                        + "av_status.attr_value AS STATUS, "
                        + "av_severity.attr_value AS SEVERITY "
                        + "FROM issues i "
                        + "JOIN folders f ON i.folder_id = f.folder_id "
                        + "JOIN projects p ON f.project_id = p.project_id "
                        + "JOIN issue_types it ON f.type_id = it.type_id "
                        + "LEFT JOIN stamps s_created ON i.stamp_id = s_created.stamp_id "
                        + "LEFT JOIN users u ON s_created.user_id = u.user_id "
                        + "LEFT JOIN attr_values av_assigned ON i.issue_id = av_assigned.issue_id "
                        + "AND av_assigned.attr_id = ( "
                        + "SELECT attr_id "
                        + "FROM attr_types "
                        + "WHERE attr_name = 'Assigned To' "
                        + "AND type_id = it.type_id "
                        + ") "
                        + "LEFT JOIN attr_values av_status ON i.issue_id = av_status.issue_id "
                        + "AND av_status.attr_id = ( "
                        + "SELECT attr_id "
                        + "FROM attr_types "
                        + "WHERE attr_name = 'Status' "
                        + "AND type_id = it.type_id "
                        + ") "
                        + "LEFT JOIN attr_values av_severity ON i.issue_id = av_severity.issue_id "
                        + "AND av_severity.attr_id = ( "
                        + "SELECT attr_id "
                        + "FROM attr_types "
                        + "WHERE attr_name = 'Severity' "
                        + "AND type_id = it.type_id "
                        + ") "
                        + "WHERE f.folder_name = '" + folderName + "'"
                        + " AND it.type_id = " + typeId;

                resultSet = statement.executeQuery(issueQuery);
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();
                columnNames = new ArrayList<>();

                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnLabel(i);
                    columnNames.add(columnName);
                }

                List<Object[]> data = new ArrayList<>();
                while (resultSet.next()) {
                    Object[] rowData = new Object[columnCount];
                    for (int i = 1; i <= columnCount; i++) {
                        rowData[i - 1] = resultSet.getObject(i);
                    }
                    data.add(rowData);
                }

                tableModel = new DefaultTableModel(data.toArray(new Object[0][]), columnNames.toArray());
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (resultSet != null) {
                    try {
                        resultSet.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (con != null) {
                    try {
                        con.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if (typeId == 3) {
            try {
                con = DbConnection.getConnection();
                statement = con.createStatement();
                String issueQuery = "SELECT i.issue_id AS ID, "
                        + "i.issue_name AS ISSUE_NAME, "
                        + "CONCAT(p.project_name, ' - ', f.folder_name) AS LOCATION, "
                        + "DATE_FORMAT(CONVERT_TZ(FROM_UNIXTIME(s_created.stamp_time), '+00:00', '+05:00'), '%m/%d/%Y %h:%i:%s %p') AS `Modified Date`, "
                        + "u.user_name AS `Modified By`, "
                        + "av_assigned.attr_value AS `ASSIGNED TO`, "
                        + "av_status.attr_value AS STATUS, "
                        + "av_priority.attr_value AS PRIORITY "
                        + "FROM issues i "
                        + "JOIN folders f ON i.folder_id = f.folder_id "
                        + "JOIN projects p ON f.project_id = p.project_id "
                        + "JOIN issue_types it ON f.type_id = it.type_id "
                        + "LEFT JOIN stamps s_created ON i.stamp_id = s_created.stamp_id "
                        + "LEFT JOIN users u ON s_created.user_id = u.user_id "
                        + "LEFT JOIN attr_values av_assigned ON i.issue_id = av_assigned.issue_id "
                        + "AND av_assigned.attr_id = ( "
                        + "SELECT attr_id "
                        + "FROM attr_types "
                        + "WHERE attr_name = 'Assigned To' "
                        + "AND type_id = it.type_id "
                        + ") "
                        + "LEFT JOIN attr_values av_status ON i.issue_id = av_status.issue_id "
                        + "AND av_status.attr_id = ( "
                        + "SELECT attr_id "
                        + "FROM attr_types "
                        + "WHERE attr_name = 'Status' "
                        + "AND type_id = it.type_id "
                        + ") "
                        + "LEFT JOIN attr_values av_priority ON i.issue_id = av_priority.issue_id "
                        + "AND av_priority.attr_id = ( "
                        + "SELECT attr_id "
                        + "FROM attr_types "
                        + "WHERE attr_name = 'Priority' "
                        + "AND type_id = it.type_id "
                        + ") "
                        + "WHERE f.folder_name = '" + folderName + "'"
                        + " AND it.type_id = " + typeId;

                resultSet = statement.executeQuery(issueQuery);
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();
                columnNames = new ArrayList<>();

                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnLabel(i);
                    columnNames.add(columnName);
                }

                List<Object[]> data = new ArrayList<>();
                while (resultSet.next()) {
                    Object[] rowData = new Object[columnCount];
                    for (int i = 1; i <= columnCount; i++) {
                        rowData[i - 1] = resultSet.getObject(i);
                    }
                    data.add(rowData);
                }

                tableModel = new DefaultTableModel(data.toArray(new Object[0][]), columnNames.toArray());
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                // Close resources
                if (resultSet != null) {
                    try {
                        resultSet.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (con != null) {
                    try {
                        con.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {

            try {
                con = DbConnection.getConnection();
                statement = con.createStatement();
                String issueQuery = "SELECT i.issue_id AS ID, "
                        + "i.issue_name AS ISSUE_NAME, "
                        + "CONCAT(p.project_name, ' - ', f.folder_name) AS LOCATION, "
                        + "DATE_FORMAT(CONVERT_TZ(FROM_UNIXTIME(s.stamp_time), '+00:00', '+05:00'), '%m/%d/%Y %h:%i:%s %p') AS `MODIFIED DATE`, "
                        + "u.user_name AS `MODIFIED BY` "
                        + "FROM folders f "
                        + "JOIN projects p ON f.project_id = p.project_id "
                        + "JOIN issues i ON f.folder_id = i.folder_id "
                        + "JOIN stamps s ON i.stamp_id = s.stamp_id "
                        + "JOIN users u ON s.user_id = u.user_id "
                        + "WHERE f.type_id = " + typeId + " AND f.folder_name = '" + folderName + "'";

                resultSet = statement.executeQuery(issueQuery);
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();
                columnNames = new ArrayList<>();

                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnLabel(i);
                    columnNames.add(columnName);
                }

                List<Object[]> data = new ArrayList<>();
                while (resultSet.next()) {
                    Object[] rowData = new Object[columnCount];
                    for (int i = 1; i <= columnCount; i++) {
                        rowData[i - 1] = resultSet.getObject(i);
                    }
                    data.add(rowData);
                }

                tableModel = new DefaultTableModel(data.toArray(new Object[0][]), columnNames.toArray());
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                // Close resources
                if (resultSet != null) {
                    try {
                        resultSet.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (con != null) {
                    try {
                        con.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return tableModel;
    }

    public DefaultTableModel createTableModel(String[] columnNames, List<Object[]> data) {
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        for (Object[] rowData : data) {
            tableModel.addRow(rowData);
        }
        return tableModel;
    }

@Override
public void deleteIssue(int issueId) {
        int userID = SessionManager.getInstance().getUserId();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DbConnection.getConnection();
            connection.setAutoCommit(false);

            String getFolderId = "SELECT folder_id from issues where issue_id = ?";
            statement = connection.prepareStatement(getFolderId);
            statement.setInt(1, issueId);
            resultSet = statement.executeQuery();
            int folderId = 0;
            if (resultSet.next()) {
                folderId = resultSet.getInt("folder_id");
            }

            String insertQuery = "INSERT INTO stamps ( user_id, stamp_time ) VALUES ( ?, ? )";
            statement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, userID);
            statement.setInt(2, (int) (System.currentTimeMillis() / 1000));
            statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();
            int generatedId = 0;
            if (resultSet.next()) {
                generatedId = resultSet.getInt(1);
            }

            String issueStubsQuery = "INSERT INTO issue_stubs ( stub_id, prev_id, issue_id, folder_id ) VALUES ( ?, ?, ?, ? )";
            statement = connection.prepareStatement(issueStubsQuery);
            statement.setInt(1, generatedId);
            statement.setInt(2, issueId);
            statement.setInt(3, issueId);
            statement.setInt(4, folderId);
            statement.executeUpdate();

            String deleteQuery = "DELETE FROM issues WHERE issue_id = ?";
            statement = connection.prepareStatement(deleteQuery);
            statement.setInt(1, issueId);
            statement.executeUpdate();

            String updateFoldersQuery = "UPDATE folders SET stamp_id = ? WHERE folder_id = ? AND COALESCE( stamp_id, 0 ) < ?";
            statement = connection.prepareStatement(updateFoldersQuery);
            statement.setInt(1, generatedId);
            statement.setInt(2, folderId);
            statement.setInt(3, generatedId);
            statement.executeUpdate();

            connection.commit();
            JOptionPane.showMessageDialog(null, "Issue successfully deleted!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            throw new RuntimeException("Error deleting issue: " + ex.getMessage(), ex);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
}
}
