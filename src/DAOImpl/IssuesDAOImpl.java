package DAOImpl;

import DAO.IssuesDAO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.*;
import com.sun.media.jai.codec.ImageCodec;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.MediaTracker;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import pojos.SessionManager;
import webissuesFrame.HomeFrame;
import webissuesFrame.LoginFrame;
import pojos.Issues;
import pojos.Path;
import sun.awt.image.ImageDecoder;
import webissuesFrame.AddNewIssue;
import webissuesFrame.EditIssues;

public class IssuesDAOImpl implements IssuesDAO {

    public static String name = "";
    GlobalDAOImpl global = new GlobalDAOImpl();
    ProjectsDAOImpl projectsDao = new ProjectsDAOImpl();
    Issues issue = new Issues();

    @Override
    public DefaultTableModel getTableData(int folderId) {
        DefaultTableModel tableModel = new DefaultTableModel();

        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(LoginFrame.apiUrl);
            String api = url.getProtocol() + "://" + url.getHost() + "/";
            String apiUrl = api + "server/api/issues/list.php";

            // Open a connection to the API endpoint using apiUrl
            connection = (HttpURLConnection) new URL(apiUrl).openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            // Set headers
            connection.setRequestProperty("X-Csrf-Token", SessionManager.getInstance().getCsrfToken());
            connection.setRequestProperty("Cookie", SessionManager.getInstance().getCookie());
            connection.setRequestProperty("Content-Type", "application/json");
            String jsonInputString = "{\"folderId\": " + folderId + ", \"limit\": " + 50000 + "}";
            // Enable input/output streams for the connection
            connection.setDoInput(true);
            connection.setDoOutput(true);

            connection.getOutputStream().write(jsonInputString.toString().getBytes("utf-8"));

            // Get the response code
            int responseCode = connection.getResponseCode();
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

    public Map<Integer, Object> printAttributes(int issueId) {
        Map<Integer, Object> attributes = new HashMap<>();

        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(LoginFrame.apiUrl);
            String api = url.getProtocol() + "://" + url.getHost() + "/";
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
                        JSONObject details = result.getJSONObject("details");
                        JSONArray attributesArray = result.getJSONArray("attributes");

                        // Extract attributes and store them in the attributesMap
                        for (int i = 0; i < attributesArray.length(); i++) {
                            JSONObject attribute = attributesArray.getJSONObject(i);
                            int attrId = attribute.getInt("id");
                            String attributeValue = attribute.optString("value", " "); // Use whitespace if "value" is null
                            System.out.println("id :" + attrId + "Attr value :" + attributeValue);

                            attributes.put(attrId, attributeValue);
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
        return attributes;
    }

    @Override
    public Map<String, Object> getAttributes(int issueId) {
        Map<String, Object> attributesMap = new HashMap<>();

        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(LoginFrame.apiUrl);
            String api = url.getProtocol() + "://" + url.getHost() + "/";
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
                        JSONObject details = result.getJSONObject("details");
                        JSONArray attributesArray = result.getJSONArray("attributes");

                        // Extract attributes and store them in the attributesMap
                        for (int i = 0; i < attributesArray.length(); i++) {
                            JSONObject attribute = attributesArray.getJSONObject(i);
                            int attrId = attribute.getInt("id");
                            String attributeName = global.getAttributeName(attrId);
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
        int folderId = 0;
        int typeId = 0;
        String createdDate = null;
        int createdBy = 0;
        String modifiedDate = null;
        int modifiedBy = 0;

        try {
            URL url = new URL(LoginFrame.apiUrl);
            String api = url.getProtocol() + "://" + url.getHost() + "/";
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
                        issue.setIssueName(name);
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
        String createdByUser = global.getUserName(createdBy);
        String typename = projectsDao.getTypeName(typeId);
        String modifiedByUser = global.getUserName(modifiedBy);
        Object createdDetails = createdDate + " - " + createdByUser;
        Object modifiedDetails = modifiedDate + " - " + modifiedByUser;
        //Object created = createdDate + createdBy;
        issueDetailsMap.put("ID", id);
        issueDetailsMap.put("NAME", name);
        issueDetailsMap.put("LOCATION", HomeFrame.PATH);
        issueDetailsMap.put("TYPE", typename);
        issueDetailsMap.put("CREATED", createdDetails);
        issueDetailsMap.put("LAST MODIFIED", modifiedDetails);

        return issueDetailsMap;
    }

    @Override
    public Map<String, String> getHistory(int issueId) {
        Map<String, String> historyMap = new LinkedHashMap<>(); // Use LinkedHashMap to preserve order
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(LoginFrame.apiUrl);
            String api = url.getProtocol() + "://" + url.getHost() + "/";
            String apiUrl = api + "server/api/issues/load.php";

            // Create the JSON request body using a JSONObject
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

            // Open a connection to the API endpoint using apiUrl
            connection = (HttpURLConnection) new URL(apiUrl).openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            // Set headers
            connection.setRequestProperty("X-Csrf-Token", SessionManager.getInstance().getCsrfToken());
            connection.setRequestProperty("Cookie", SessionManager.getInstance().getCookie());
            connection.setRequestProperty("Content-Type", "application/json");

            // Write the JSON request body to the output stream
            // Write the JSON request body to the output stream
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8); // Use jsonInputString here
                os.write(input, 0, input.length);
            }

            // Get the response code
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the response
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = reader.readLine()) != null) {
                    response.append(inputLine);
                }
                // Parse the JSON response
                try {
                    JSONObject jsonResponse = new JSONObject(response.toString());
                    if (jsonResponse.has("result")) {
                        // Assuming that the API response is stored in a variable named 'apiResponse'
                        JSONObject result = jsonResponse.getJSONObject("result");
                        JSONArray historyArray = result.getJSONArray("history");
                        String currentKey = ""; // Initialize current key
                        StringBuilder currentEntry = new StringBuilder(); //
                        String currentAttribute = null; // Initialize current attribute
                        for (int i = 0; i < historyArray.length(); i++) {
                            JSONObject historyEntry = historyArray.getJSONObject(i);
                            String createdDate = formatDateTime(historyEntry.getLong("createdDate"));
                            String createdByStr = historyEntry.optString("createdBy");
                            int created = Integer.parseInt(createdByStr);
                            String createdBy = global.getUserName(created);

                            // Check if currentKey is empty or different from the current entry's key
                            if (currentKey.isEmpty() || !currentKey.equals(createdDate + " — " + createdBy)) {
                                // If it's a new key, create a new StringBuilder
                                currentKey = createdDate + " — " + createdBy;
                                currentEntry = new StringBuilder();
                                currentAttribute = null; // Reset current attribute
                            }

                            String newValue = historyEntry.optString("new");
                            String oldValue = historyEntry.optString("old");
                            String text = historyEntry.optString("text");
                            String name = historyEntry.optString("name");
                            int id = historyEntry.optInt("id");
                            String attributeId = historyEntry.optString("attributeId");
                            int atrID = 0; // Default value if attributeId is empty
                            if (!attributeId.isEmpty()) {
                                atrID = Integer.parseInt(attributeId);
                            }
                            String attributeName = global.getAttributeName(atrID);

                            // Check for null attributeName and provide a default value
                            if (attributeName == null) {
                                if (!historyEntry.has("attributeId") && !historyEntry.has("text") && !historyEntry.has("name")) {
                                    attributeName = "NAME";
                                } else if (!historyEntry.has("attributeId")) {
                                    attributeName = "Comment";
                                } else {
                                    attributeName = "UNKNOWN ATTRIBUTE";
                                }
                            }

                            // Handle attachments generically
                            if (historyEntry.has("name")) { // Assuming type 4 represents attachments
                                attributeName = "ATTACHMENT";
                            }

                            // Append the information to the current entry
                            if (!newValue.equals(oldValue) || !attributeName.equals(currentAttribute)) {
                                // Insert a newline if the attribute or value has changed
                                currentEntry.append("\n");
                            }
                            currentAttribute = attributeName; // Update current attribute

                            currentEntry.append("   ").append(attributeName).append(":");
                            if ("Comment".equals(attributeName) || "Comment".equals(currentAttribute)) {
                                currentEntry.append(text.replace("\n", " "));
                            } else if ("ATTACHMENT".equals(attributeName) || "ATTACHMENT".equals(currentAttribute)) {
                                currentEntry.append("{" + name + "}").append("[id:").append(historyEntry.optInt("id")).append("]");
                            } else {
                                currentEntry.append(oldValue).append(" → ").append(newValue);
                            }

                            // Add the current entry to the historyMap
                            historyMap.put(currentKey, currentEntry.toString());

                        }
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
        } finally {
            // Close the reader and connection in a finally block
            try {
                if (reader != null) {
                    reader.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            //in pther cases it show liek cihrtoscindm
        }
        return historyMap;
    }

    @Override
    public String getDescription(int IssueID) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        String text = null;

        try {
            URL url = new URL(LoginFrame.apiUrl);
            String api = url.getProtocol() + "://" + url.getHost() + "/";
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
                    + "\"issueId\": " + IssueID + ","
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
                        // Check if "description" is not null and is an object
                        if (!result.isNull("description") && result.get("description") instanceof JSONObject) {
                            JSONObject description = result.getJSONObject("description");

                            // Get the "text" field from the "description" object
                            if (description.has("text")) {
                                text = description.getString("text");
                            } else {
                                System.err.println("Error: 'text' not found in the 'description' object.");
                            }
                        } else {
                            System.err.println("Error: 'description' is null or not a JSONObject.");
                        }
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

        return text;
    }

    @Override
    public String deleteIssue(int issueId) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(LoginFrame.apiUrl);
            String api = url.getProtocol() + "://" + url.getHost() + "/";
            String apiUrl = api + "server/api/issues/delete.php";
            // Open a connection to the API endpoint using apiUrl
            connection = (HttpURLConnection) new URL(apiUrl).openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            // Set headers
            connection.setRequestProperty("X-Csrf-Token", SessionManager.getInstance().getCsrfToken());
            connection.setRequestProperty("Cookie", SessionManager.getInstance().getCookie());
            connection.setRequestProperty("Content-Type", "application/json");
            String jsonInputString = "{"
                    + "\"issueId\": " + issueId + "}";

            connection.setDoInput(true);
            connection.setDoOutput(true);

            connection.getOutputStream().write(jsonInputString.getBytes(StandardCharsets.UTF_8));
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Parse and process the JSON response if needed
                String jsonResponse = response.toString();
                return "Issue with ID " + issueId + " deleted successfully. Response: " + jsonResponse;
            } else {
                return "Failed to delete the issue. HTTP Error Code: " + responseCode;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to communicate with the API.";
        }
    }

    public void editIssue() {
        HttpURLConnection connection = null;
        JSONObject requestObject = null;
        JSONArray valuesArray = null;
        try {
            // Construct the API endpoint URL
            URL url = new URL(LoginFrame.apiUrl);
            String api = url.getProtocol() + "://" + url.getHost() + "/";
            String apiUrl = api + "server/api/issues/edit.php";

            // Open a connection to the API endpoint using apiUrl
            connection = (HttpURLConnection) new URL(apiUrl).openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            // Set headers
            connection.setRequestProperty("X-Csrf-Token", SessionManager.getInstance().getCsrfToken());
            connection.setRequestProperty("Cookie", SessionManager.getInstance().getCookie());
            connection.setRequestProperty("Content-Type", "application/json");

            // Create a new JSON array for the "values" field
            valuesArray = new JSONArray();

            // Add key-value pairs from EditIssues.getAPIValues to the "values" array
            for (Map.Entry<Integer, Object> entry : EditIssues.getAPIValues.entrySet()) {
                Integer key = entry.getKey();
                Object value = entry.getValue();
                if (value instanceof Integer || value instanceof Long || value instanceof Short || value instanceof Byte) {
                    // Convert the integer value to a string
                    value = String.valueOf(value);
                }
                System.out.println("Key in Map: " + key + " Value " + value);

                // Create a JSON object for each key-value pair and add it to the "values" array
                JSONObject entryObject = new JSONObject();
                entryObject.put("id", key);
                entryObject.put("value", value);
                valuesArray.put(entryObject);
            }

            // Create a new JSON object for the request
            requestObject = new JSONObject();
            int issueId = HomeFrame.IssueID;
            String name = EditIssues.oldName;
            requestObject.put("issueId", issueId);
            requestObject.put("name", name);
            requestObject.put("values", valuesArray);
            String jsonInputString = requestObject.toString();
            System.out.println("JSON Input" + jsonInputString);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the response body
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    StringBuilder responseStringBuilder = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        responseStringBuilder.append(line);
                    }
                    String responseBody = responseStringBuilder.toString();

                    // Process the response if needed
                    System.out.println("Response Body: " + responseBody);
                }
            } else {
                // Handle HTTP error responses
                System.err.println("HTTP Error Response: " + responseCode);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            // Handle exceptions appropriately
        } finally {
            // Close the connection
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public void addIssue() {
        HttpURLConnection connection = null;
        JSONObject requestObject = null;
        JSONArray valuesArray = null;
        String desc = AddNewIssue.getDescription;
        try {
            // Construct the API endpoint URL
            URL url = new URL(LoginFrame.apiUrl);
            String api = url.getProtocol() + "://" + url.getHost() + "/";
            String apiUrl = api + "server/api/issues/add.php";

            // Open a connection to the API endpoint using apiUrl
            connection = (HttpURLConnection) new URL(apiUrl).openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            // Set headers
            connection.setRequestProperty("X-Csrf-Token", SessionManager.getInstance().getCsrfToken());
            connection.setRequestProperty("Cookie", SessionManager.getInstance().getCookie());
            connection.setRequestProperty("Content-Type", "application/json");

            // Create a new JSON array for the "values" field
            valuesArray = new JSONArray();

            // Add key-value pairs from EditIssues.getAPIValues to the "values" array
            for (Map.Entry<Integer, Object> entry : AddNewIssue.filteredValues.entrySet()) {
                Integer key = entry.getKey();
                Object value = entry.getValue();
                if (value instanceof Integer || value instanceof Long || value instanceof Short || value instanceof Byte) {
                    // Convert the integer value to a string
                    value = String.valueOf(value);
                }
                System.out.println("Key in Map: " + key + " Value " + value);

                // Create a JSON object for each key-value pair and add it to the "values" array
                JSONObject entryObject = new JSONObject();
                entryObject.put("id", key);
                entryObject.put("value", value);
                valuesArray.put(entryObject);
            }

            requestObject = new JSONObject();
            int folderId = HomeFrame.FoldersID;
            String name = AddNewIssue.name;
            requestObject.put("folderId", folderId);
            requestObject.put("name", name);
            requestObject.put("values", valuesArray);
            if (desc != null && !desc.isEmpty()) {  // Check if desc is not null and not empty
                requestObject.put("description", desc);
                requestObject.put("descriptionFormat", 1);
            }
            String jsonInputString = requestObject.toString();
            System.out.println("JSON Input" + jsonInputString);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            // Write the JSON payload to the connection's output stream
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Check the HTTP response status code
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the response body
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    StringBuilder responseStringBuilder = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        responseStringBuilder.append(line);
                    }
                    String responseBody = responseStringBuilder.toString();

                    System.out.println("Response Body: " + responseBody);

                    JSONObject jsonResponse = new JSONObject(responseBody);
                    if (jsonResponse.has("errorCode")) {
                        int errorCode = jsonResponse.getInt("errorCode");
                        String errorMessage = jsonResponse.getString("errorMessage");

                        // Show the error message in a JOptionPane dialog
                        JOptionPane.showMessageDialog(
                                null,
                                "API Error Code: " + errorCode + "\nError Message: " + errorMessage,
                                "API Error",
                                JOptionPane.ERROR_MESSAGE
                        );

                        // Handle the error as needed, e.g., show an error message to the user
                    } else {
            // The response does not contain an error, process the response data
                        // ... (your code to process the response data)
                    }
                }
            } else {
                System.err.println("HTTP Error Response: " + responseCode);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            // Close the connection
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public void getFile(String id) {
        try {
            URL url = new URL(LoginFrame.apiUrl);
            String api = url.getProtocol() + "://" + url.getHost() + "/";
            String apiUrl = api + "webissues/client/file.php?id=" + id;

            // Open a connection to the API
            HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "image/jpeg");

            // Get the response code
            int responseCode = connection.getResponseCode();
            System.out.println("Response code in Image " + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (InputStream inputStream = connection.getInputStream()) {
                    byte[] imageData;
                    try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            byteArrayOutputStream.write(buffer, 0, bytesRead);
                        }
                        imageData = byteArrayOutputStream.toByteArray();
                    }

                    SwingUtilities.invokeLater(() -> {
                        displayImage(imageData);
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.err.println("HTTP Error Response: " + responseCode);
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayImage(byte[] imageData) {
        ImageIcon imageIcon = new ImageIcon(imageData);
        Image image = imageIcon.getImage();

        if (image != null) {
            JFrame frame = new JFrame("Image Display");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JPanel panel = new JPanel();
            JLabel imageLabel = new JLabel(new ImageIcon(image));
            panel.add(imageLabel);

            JScrollPane scrollPane = new JScrollPane(panel);
            panel.setPreferredSize(new Dimension(800, 600));
            scrollPane.setPreferredSize(new Dimension(800, 600));

            frame.add(scrollPane);
            frame.pack();
            frame.setVisible(true);
        } else {
            System.err.println("Failed to load the image.");
        }
    }
}
