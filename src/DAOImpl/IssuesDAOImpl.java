package DAOImpl;

import DAO.IssuesDAO;
import dbConnection.DbConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import pojos.SessionManager;
import webissuesFrame.LoginFrame;

public class IssuesDAOImpl implements IssuesDAO {
    GlobalDAOImpl global = new GlobalDAOImpl();
    @Override
    public DefaultTableModel getTableData(int folderId) {
        DefaultTableModel tableModel = new DefaultTableModel();

        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(LoginFrame.apiUrl);
            String api = url.getProtocol() + "://" + url.getHost() + "/";
            System.out.println("Original API " + api);
            String apiUrl = api + "server/api/issues/list.php";

            // Open a connection to the API endpoint using apiUrl
            connection = (HttpURLConnection) new URL(apiUrl).openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            // Set headers
            connection.setRequestProperty("X-Csrf-Token", SessionManager.getInstance().getCsrfToken());
            connection.setRequestProperty("Cookie", SessionManager.getInstance().getCookie());
            connection.setRequestProperty("Content-Type", "application/json");

            System.out.println("Folder ID in getTable Data " + folderId);
            String jsonInputString = "{\"folderId\": " + folderId + ", \"limit\": " + 10 + "}";
            // Enable input/output streams for the connection
            connection.setDoInput(true);
            connection.setDoOutput(true);

            connection.getOutputStream().write(jsonInputString.toString().getBytes("utf-8"));

            // Get the response code
            int responseCode = connection.getResponseCode();
            System.out.println("Respoce Code " + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the response
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = reader.readLine()) != null) {
                    response.append(inputLine);
                }

                // Parse the JSON response
                JSONObject jsonResponse = new JSONObject(response.toString());
                if (jsonResponse.has("result")) {
                    JSONObject result = jsonResponse.getJSONObject("result");
                    JSONArray columnsArray = result.getJSONArray("columns");
                    JSONArray issuesArray = result.getJSONArray("issues");
                    // Extract column names
                    for (int i = 0; i < columnsArray.length(); i++) {
                        JSONObject column = columnsArray.getJSONObject(i);
                        String columnName = column.getString("name");
                        tableModel.addColumn(columnName);
                    }

                    // Extract row data
                    for (int i = 0; i < issuesArray.length(); i++) {
                        JSONArray rowDataArray = issuesArray.getJSONObject(i).getJSONArray("cells");
                        Vector<Object> row = new Vector<>();
                        boolean isReadNull = issuesArray.getJSONObject(i).isNull("read"); // Check if "read" is null

                        for (int j = 0; j < rowDataArray.length(); j++) {
                            Object cellData = rowDataArray.get(j);
                            if (j == 2) { // Check if it's the "Modified Date" column
                                // Parse and format the date
                                long timestamp = Long.parseLong(cellData.toString()) * 1000; // Convert to milliseconds
                                Date date = new Date(timestamp);
                                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
                                cellData = sdf.format(date);
                            }
                            if (isReadNull) {
                                // If "read" is null, make the entire row bold with HTML tags
                                cellData = "<html><b>" + cellData.toString() + "</b></html>";
                            }

                            row.add(cellData);
                        }

                        tableModel.addRow(row);
                    }
                } else {
                    // Handle the case where "result" is not found in the response
                    System.err.println("Error: 'result' not found in the JSON response.");
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return tableModel;
    }

    @Override
    public Map<String, Object> getAttributes(int issueId) {
        Map<String, Object> attributesMap = new HashMap<>();

        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(LoginFrame.apiUrl);
            String api = url.getProtocol() + "://" + url.getHost() + "/";
            System.out.println("Original API " + api);
            String apiUrl = api + "server/api/issues/load.php";

            // Open a connection to the API endpoint using apiUrl
            connection = (HttpURLConnection) new URL(apiUrl).openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            // Set headers
            connection.setRequestProperty("X-Csrf-Token", SessionManager.getInstance().getCsrfToken());
            connection.setRequestProperty("Cookie", SessionManager.getInstance().getCookie());
            connection.setRequestProperty("Content-Type", "application/json");

            // Construct the request body
            String jsonInputString = "{"
                    + "\"issueId\": " + issueId + ","
                    + "\"description\": true,"
                    + "\"attributes\": true,"
                    + "\"history\": true,"
                    + "\"modifiedSince\": 0,"
                    + "\"filter\": 1,"
                    + "\"unread\": false,"
                    + "\"html\": true"
                    + "}";

            // Enable input/output streams for the connection
            connection.setDoInput(true);
            connection.setDoOutput(true);

            connection.getOutputStream().write(jsonInputString.getBytes(StandardCharsets.UTF_8));

            // Get the response code
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code " + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the response
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = reader.readLine()) != null) {
                    response.append(inputLine);
                }
                try {
                    // Parse the JSON response
                    JSONObject jsonResponse = new JSONObject(response.toString());
                    if (jsonResponse.has("result")) {
                        JSONObject result = jsonResponse.getJSONObject("result");
                        JSONArray attributesArray = result.getJSONArray("attributes");

                        // Extract attributes and store them in the attributesMap
                        for (int i = 0; i < attributesArray.length(); i++) {
                            JSONObject attribute = attributesArray.getJSONObject(i);
                            String attributeName = attribute.getString("name");
                            String attributeValue = attribute.optString("value", " "); // Use whitespace if "value" is null
                            attributesMap.put(attributeName, attributeValue);
                        }
                    } else {
                        System.err.println("Error: 'result' not found in the JSON response.");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                // Handle error cases here
                System.err.println("Error: HTTP Response Code " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Close the connection and reader
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
        return attributesMap;
    }

    private String formatDateTime(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        return sdf.format(new Date(timestamp * 1000L)); // Convert to milliseconds
    }

    @Override
    public Map<String, Object> getIssueDetails(int issueId) {
        Map<String, Object> issueDetailsMap = new HashMap<>();
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        int id = 0;
        String name = "";
        int folderId = 0;
        int typeId = 0;
        String createdDate = null;
        int createdBy = 0;
        String modifiedDate = null;
        int modifiedBy = 0; 

        try {
            URL url = new URL(LoginFrame.apiUrl);
            String api = url.getProtocol() + "://" + url.getHost() + "/";
            System.out.println("Original API " + api);
            String apiUrl = api + "server/api/issues/load.php"; 
            // Open a connection to the API endpoint using apiUrl
            connection = (HttpURLConnection) new URL(apiUrl).openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            // Set headers
            connection.setRequestProperty("X-Csrf-Token", SessionManager.getInstance().getCsrfToken());
            connection.setRequestProperty("Cookie", SessionManager.getInstance().getCookie());
            connection.setRequestProperty("Content-Type", "application/json");
            // Construct the request body
            String jsonInputString = "{"
                    + "\"issueId\": " + issueId + ","
                    + "\"description\": true,"
                    + "\"attributes\": true,"
                    + "\"history\": true,"
                    + "\"modifiedSince\": 0,"
                    + "\"filter\": 1,"
                    + "\"unread\": false,"
                    + "\"html\": true"
                    + "}";

            connection.setDoInput(true);
            connection.setDoOutput(true);

            connection.getOutputStream().write(jsonInputString.getBytes(StandardCharsets.UTF_8));

            int responseCode = connection.getResponseCode();
            System.out.println("Response Code " + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the response
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = reader.readLine()) != null) {
                    response.append(inputLine);
                }

                try { 
                    JSONObject jsonResponse = new JSONObject(response.toString());
                    if (jsonResponse.has("result")) {
                        JSONObject result = jsonResponse.getJSONObject("result");

                        JSONObject details = result.getJSONObject("details");
                        id = details.getInt("id");
                        name = details.getString("name");
                        folderId = details.getInt("folderId");
                        typeId = details.getInt("typeId");
                        createdDate = formatDateTime(details.getLong("createdDate")); // Convert Unix timestamp to milliseconds
                        createdBy = details.getInt("createdBy");
                        modifiedDate = formatDateTime(details.getLong("modifiedDate")); // Convert Unix timestamp to milliseconds
                        modifiedBy = details.getInt("modifiedBy"); 
                    } else {
                        System.err.println("Error: 'result' not found in the JSON response.");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                System.err.println("Error: HTTP Response Code " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String username = global.getUserName(createdBy);
        System.out.println("User NAme ---------------------------->>>>>>>>>> "+username);
        //Object created = createdDate + createdBy;
        issueDetailsMap.put("ID", id);
        issueDetailsMap.put("NAME", name);
        issueDetailsMap.put("folderId", folderId);
        issueDetailsMap.put("typeId", typeId);
        issueDetailsMap.put("created", createdDate);
        issueDetailsMap.put("createdBy", username);
        issueDetailsMap.put("modifiedDate", modifiedDate);
        issueDetailsMap.put("modifiedBy", modifiedBy);

        return issueDetailsMap;
    }
}
