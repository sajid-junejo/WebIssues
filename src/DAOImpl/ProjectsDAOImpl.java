package DAOImpl;

import DAO.ProjectsDAO; 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import pojos.Folder;
import pojos.IssueTypes;
import pojos.Project;
import pojos.SessionManager;
import webissuesFrame.LoginFrame;

public class ProjectsDAOImpl implements ProjectsDAO {

    private String folderName;
    ConnectionDAOImpl connectionDao = new ConnectionDAOImpl();
    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    @Override
   public List<Project> getProjects() {
    List<Project> projects = new ArrayList<>();
    HttpURLConnection connection = null;
    String apiUrl = connectionDao.buildApiUrl("projects/list.php");
    String jsonInputString = "{}";
    connection = connectionDao.establishConnection(apiUrl);
    if (connection != null) {
        try {
            JSONObject jsonResponse = connectionDao.makeRequestAndGetResponse(connection, jsonInputString);            
            if (jsonResponse != null && jsonResponse.has("result")) {
                JSONObject result = jsonResponse.getJSONObject("result");
                JSONArray projectsArray = result.getJSONArray("projects");                
                for (int i = 0; i < projectsArray.length(); i++) {
                    JSONObject projectJson = projectsArray.getJSONObject(i);
                    int projectId = projectJson.getInt("id");
                    String projectName = projectJson.getString("name");
                    int projectAccess = projectJson.getInt("access");
                    boolean isPublic = projectJson.getBoolean("public");
                    
                    Project project = new Project(projectId, projectName);
                    // project.setAccess(projectAccess);
                    // project.setPublic(isPublic);
                    projects.add(project);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    } else {
        System.err.println("Failed to establish a connection.");
    }

    return projects;
}

    public int getProjectIdByName(List<Project> projects, String projectName) {
        for (Project project : projects) {
            if (project.getProjectName().equals(projectName)) {
                return project.getProjectId();
            }
        }
        return -1;
    }
    @Override
    public List<Folder> getFoldersForProject(int projectId) {
        List<Folder> folders = new ArrayList<>();
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(LoginFrame.apiUrl);
            String api = url.getProtocol() + "://" + url.getHost() + "/";
            String apiUrl = api;
            if(apiUrl.startsWith("https")){
                apiUrl = apiUrl+"server/api/projects/load.php"; 
            }else{
                apiUrl = apiUrl + "webissues/server/api/projects/load.php";
            }
  
            connection = (HttpURLConnection) new URL(apiUrl).openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
 
            connection.setRequestProperty("X-Csrf-Token", SessionManager.getInstance().getCsrfToken());
            connection.setRequestProperty("Cookie", SessionManager.getInstance().getCookie());
            connection.setRequestProperty("Content-Type", "application/json");
 
            String jsonInputString = "{\"projectId\": " + projectId + ", \"description\": true, \"folders\": true, \"members\": true, \"html\": false}";
 
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
 
            int responseCode = connection.getResponseCode();
 
            if (responseCode == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = reader.readLine()) != null) {
                    response.append(inputLine);
                }
 
                JSONObject jsonResponse = new JSONObject(response.toString());
 
                JSONObject result = jsonResponse.getJSONObject("result");
                JSONArray foldersArray = result.getJSONArray("folders");
 
                for (int i = 0; i < foldersArray.length(); i++) {
                    JSONObject folderJson = foldersArray.getJSONObject(i);
                    int folderId = folderJson.getInt("id");
                    String folderName = folderJson.getString("name");
                    int typeId = folderJson.getInt("typeId");
                     
                    Folder folder = new Folder(folderId, folderName, typeId);
                    
                    folder.setFolderId(folderId);
                    folder.setTypeId(typeId);
                    folder.setFolderName(folderName);
                    folders.add(folder);  
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } 
        return folders;
    }
    public String getTypeName(int typeId) {
        String apiUrl = connectionDao.buildApiUrl("types/load.php");
        String jsonInputString = "{\"typeId\": " + typeId + ", \"attributes\": false, \"defaultView\": false, \"publicViews\": false, \"personalViews\": false, \"used\": false}";
        HttpURLConnection connection = connectionDao.establishConnection(apiUrl);
        if (connection != null) {
            try {
                JSONObject jsonResponse = connectionDao.makeRequestAndGetResponse(connection, jsonInputString);

                if (jsonResponse != null && jsonResponse.has("result")) {
                    JSONObject result = jsonResponse.getJSONObject("result");
                    String typeName = result.getString("name");
                    return typeName;
                }
            } catch (JSONException e) {
                e.printStackTrace(); 
                return "Error: " + e.getMessage();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        } else {
            System.err.println("Failed to establish a connection.");
        } 
    return "Type Name Not Found";
}

}
