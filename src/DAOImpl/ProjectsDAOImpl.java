/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAOImpl;

import DAO.ProjectsDAO;
import dbConnection.DbConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import pojos.Folder;
import pojos.Project;
import pojos.SessionManager;

public class ProjectsDAOImpl implements ProjectsDAO{
     private String folderName;

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }
    
    @Override
    public List<Project> getProjects() {
    List<Project> projects = new ArrayList<>();
    Connection con = null;
    Statement statement = null;
    ResultSet projectResultSet = null;
    try {
        con = DbConnection.getConnection();
        statement = con.createStatement();

        int userId = SessionManager.getInstance().getUserId();
        int userAccess = SessionManager.getInstance().getUserAccess();

        String projectQuery = "SELECT p.project_id, p.project_name "
            + "FROM projects p "
            + "LEFT JOIN rights r ON p.project_id = r.project_id AND r.user_id = " + userId + " "
            + "WHERE (r.project_access IS NOT NULL OR p.is_public = 1) "
            + "AND p.is_archived = 0 "
            + "ORDER BY p.project_name COLLATE utf8_bin";

        projectResultSet = statement.executeQuery(projectQuery);
        while (projectResultSet.next()) {
            int projectId = projectResultSet.getInt("project_id");
            String projectName = projectResultSet.getString("project_name");
            Project project = new Project(projectId, projectName);
            projects.add(project);
        }

    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        try {
            if (projectResultSet != null) {
                projectResultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    return projects;
}


    @Override
    public List<Folder> getFoldersForProject(int projectId) {
        List<Folder> folders = new ArrayList<>();
        Connection con = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            con = DbConnection.getConnection();
              statement = con.createStatement();

            String folderQuery = "SELECT f.folder_name, it.type_name "
                    + "FROM folders f "
                    + "INNER JOIN issue_types it ON f.type_id = it.type_id "
                    + "WHERE f.project_id = " + projectId;
            ResultSet folderResultSet = statement.executeQuery(folderQuery);
            while (folderResultSet.next()) {
                String folderName = folderResultSet.getString("folder_name");
                String typeName = folderResultSet.getString("type_name");
                Folder folder = new Folder(folderName, typeName);
                folders.add(folder);
            }

            folderResultSet.close();
            } catch (Exception e) {
        e.printStackTrace();
    } finally {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
        return folders;
    }


    @Override
 public int getFolderIdByName(String folderName) {
     Connection con = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            con = DbConnection.getConnection();
            statement = con.createStatement();

            String folderQuery = "SELECT folder_id FROM folders WHERE folder_name = '" + folderName + "'";
            resultSet = statement.executeQuery(folderQuery);
            if (resultSet.next()) {
                int folderId = resultSet.getInt("folder_id");
                resultSet.close();
                statement.close();
                con.close();
                return folderId;
            }
       } catch (Exception e) {
        e.printStackTrace();
    } finally {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
        return -1; // Return -1 if folder ID is not found
    }
}
