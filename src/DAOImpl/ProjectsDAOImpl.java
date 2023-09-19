package DAOImpl;

import DAO.ProjectsDAO;
import com.oracle.jrockit.jfr.ContentType;
import dbConnection.DbConnection;
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

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    @Override
    public List<Project> getProjects() {
        List<Project> projects = new ArrayList<>();

        try {

            URL url = new URL(LoginFrame.apiUrl);
            String api = url.getProtocol() + "://" + url.getHost() + "/";
            System.out.println("Original API " + api);
            String apiUrl = api + "server/api/projects/list.php";
            System.out.println("Appending Api " + apiUrl);

            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();

            // Set up the connection
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            System.out.println(SessionManager.getInstance().getCsrfToken());
            // Set headers
            connection.setRequestProperty("X-Csrf-Token", SessionManager.getInstance().getCsrfToken());
            connection.setRequestProperty("Cookie", SessionManager.getInstance().getCookie());
            connection.setRequestProperty("Content-Type", "application/json");

            String jsonInputString = "{}";
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Get the response
            int responseCode = connection.getResponseCode();
            System.out.println("Response Status Code: " + responseCode);
            System.out.println(connection.getResponseMessage());

            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    StringBuilder jsonResponse = new StringBuilder();

                    while ((line = in.readLine()) != null) {
                        jsonResponse.append(line);
                    }

                    // Print the JSON response for debugging purposes
                    System.out.println("JSON Response: " + jsonResponse.toString());

                    // Parse the JSON response
                    JSONObject jsonObject = new JSONObject(jsonResponse.toString());

                    // Extract the "projects" array from the JSON response
                    JSONObject result = jsonObject.getJSONObject("result");
                    JSONArray projectsArray = result.getJSONArray("projects");

                    for (int i = 0; i < projectsArray.length(); i++) {
                        JSONObject projectJson = projectsArray.getJSONObject(i);
                        int projectId = projectJson.getInt("id");
                        String projectName = projectJson.getString("name");
                        int projectAccess = projectJson.getInt("access");
                        boolean isPublic = projectJson.getBoolean("public");

                        Project project = new Project(projectId, projectName);
                        //project.setAccess(projectAccess);
                        // project.setPublic(isPublic);
                        projects.add(project);

                    }
                } catch (JSONException ex) {
                    Logger.getLogger(ProjectsDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                System.out.println("Request failed with status code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return projects;
    }

    public int getProjectIdByName(List<Project> projects, String projectName) {
        for (Project project : projects) {
            if (project.getProjectName().equals(projectName)) {
                return project.getProjectId();
            }
        }
        // Return a default value (e.g., -1) if the project is not found
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
            System.out.println("Original API " + api);
            String apiUrl = api + "server/api/projects/load.php";

            // Open a connection to the API endpoint using apiUrl
            connection = (HttpURLConnection) new URL(apiUrl).openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            // Set headers
            connection.setRequestProperty("X-Csrf-Token", SessionManager.getInstance().getCsrfToken());
            connection.setRequestProperty("Cookie", SessionManager.getInstance().getCookie());
            connection.setRequestProperty("Content-Type", "application/json");

            // Create JSON request body
            String jsonInputString = "{\"projectId\": " + projectId + ", \"description\": true, \"folders\": true, \"members\": true, \"html\": false}";

            // Write the JSON request body to the output stream
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Get the response code
            int responseCode = connection.getResponseCode();

            // Read the response
            if (responseCode == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = reader.readLine()) != null) {
                    response.append(inputLine);
                }

                // Parse the JSON response
                JSONObject jsonResponse = new JSONObject(response.toString());

                // Navigate to the "folders" array within the "result" object
                JSONObject result = jsonResponse.getJSONObject("result");
                JSONArray foldersArray = result.getJSONArray("folders");

                // Iterate through the folders array and extract folder data
                for (int i = 0; i < foldersArray.length(); i++) {
                    JSONObject folderJson = foldersArray.getJSONObject(i);
                    int folderId = folderJson.getInt("id");
                    String folderName = folderJson.getString("name");
                    int typeId = folderJson.getInt("typeId");
                    
                    // Create Folder objects and add them to the list
                    Folder folder = new Folder(folderId, folderName, typeId);
                    
                    folder.setFolderId(folderId);
                    folder.setTypeId(typeId);
                    folder.setFolderName(folderName);
                    folders.add(folder);
                    System.out.println("Type ma,e"+folder.getFolderName());
//                    IssueTypes issuetypes = new IssueTypes();
//                    issuetypes.setTypeId(typeId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } 
        return folders;
    }
  public String getTypeName(int typeId) {
    HttpURLConnection connection = null;
    BufferedReader reader = null;

    try {
        URL url = new URL(LoginFrame.apiUrl);
        String api = url.getProtocol() + "://" + url.getHost() + "/";
        System.out.println("Original API " + api);
        String apiUrl = api + "server/api/types/load.php";  // Update the API endpoint URL

        // Open a connection to the API endpoint using apiUrl
        connection = (HttpURLConnection) new URL(apiUrl).openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        // Set headers
        connection.setRequestProperty("X-Csrf-Token", SessionManager.getInstance().getCsrfToken());
        connection.setRequestProperty("Cookie", SessionManager.getInstance().getCookie());
        connection.setRequestProperty("Content-Type", "application/json");

        String jsonInputString = "{\"typeId\": " + typeId + ", \"attributes\": false, \"defaultView\": false, \"publicViews\": false, \"personalViews\": false, \"used\": false}";

        // Enable input/output streams for the connection
        connection.setDoInput(true);
        connection.setDoOutput(true);

        // Write the JSON request body to the output stream
        connection.getOutputStream().write(jsonInputString.getBytes("utf-8"));

        // Get the response code
        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Read the response
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                // Parse the JSON response
                JSONObject jsonResponse = new JSONObject(response.toString());

                // Extract the "name" field from the "result" object
                JSONObject result = jsonResponse.getJSONObject("result");
                String typeName = result.getString("name");

                return typeName;
            }
        } else {
            // Handle the error (e.g., return an error message)
            return "Error: " + responseCode;
        }
    } catch (IOException | JSONException e) {
        e.printStackTrace();
        // Handle the exception (e.g., return an error message)
        return "Error: " + e.getMessage();
    } finally {
        // Close the connection and reader if necessary
        if (connection != null) {
            connection.disconnect();
        }
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
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
