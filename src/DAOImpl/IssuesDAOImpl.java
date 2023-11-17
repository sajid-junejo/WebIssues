package DAOImpl;

import DAO.IssuesDAO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.*;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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
import webissuesFrame.HomeFrame;
import webissuesFrame.LoginFrame;
import pojos.SessionManager;
import webissuesFrame.AddNewIssue;
import webissuesFrame.EditIssues;

public class IssuesDAOImpl implements IssuesDAO {

    String FileName = null;
    public static String name = "";
    GlobalDAOImpl global = new GlobalDAOImpl();
    private Map<Integer, String> userMap = new HashMap<>();
    public List<JSONObject> resultList;
    public Map<Integer, Object> AttributesMap;
    public static byte[] transferData = null;

    public IssuesDAOImpl() {
        resultList = new ArrayList<>();
        AttributesMap = new HashMap<>();
        for (JSONObject user : usersList) {
            int id = user.optInt("id");
            String name = user.optString("name");
            userMap.put(id, name);
        }
        global.fetchData();
        //printAttributes(issueId);
    }

    ConnectionDAOImpl connectionDao = new ConnectionDAOImpl();
    ProjectsDAOImpl projectsDao = new ProjectsDAOImpl();
    public List<JSONObject> typesList = global.getTypesList();
    List<JSONObject> usersList = global.getUsersList();

    @Override
    public DefaultTableModel getTableData(int folderId) {
        DefaultTableModel tableModel = new DefaultTableModel();
        String apiUrl = connectionDao.buildApiUrl("issues/list.php");
        String jsonInputString = "{\"folderId\": " + folderId + ", \"limit\": " + 5000 + "}";

        HttpURLConnection connection = connectionDao.establishConnection(apiUrl);

        if (connection != null && folderId != 0) {
            try {
                JSONObject jsonResponse = connectionDao.makeRequestAndGetResponse(connection, jsonInputString);

                if (jsonResponse != null && jsonResponse.has("result")) {
                    JSONObject result = jsonResponse.getJSONObject("result");
                    JSONArray columnsArray = result.getJSONArray("columns");
                    JSONArray issuesArray = result.getJSONArray("issues");
                    //System.out.println("ISsues Array : "+issuesArray);
                    for (int i = 0; i < columnsArray.length(); i++) {
                        JSONObject column = columnsArray.getJSONObject(i);
                        String columnName = column.getString("name");
                        tableModel.addColumn(columnName);
                    }

                    for (int i = 0; i < issuesArray.length(); i++) {
                        JSONArray rowDataArray = issuesArray.getJSONObject(i).getJSONArray("cells");
                        Vector<Object> row = new Vector<>();
                        boolean isReadNull = issuesArray.getJSONObject(i).isNull("read");

                        for (int j = 0; j < rowDataArray.length(); j++) {
                            Object cellData = rowDataArray.get(j);
                            if (j == 2) {
                                long timestamp = Long.parseLong(cellData.toString()) * 1000;
                                Date date = new Date(timestamp);
                                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
                                cellData = sdf.format(date);
                            }
                            if (isReadNull) {
                                cellData = "<html><b>" + cellData.toString() + "</b></html>";
                            }

                            row.add(cellData);
                        }

                        tableModel.addRow(row);
                    }
                } else {
                    System.err.println("Error: 'result' not found in the JSON response.");
                }

                if (connection != null) {
                    connection.disconnect();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return tableModel;
    }

    public Map<Integer, Object> printAttributes(int issueId) {
        Map<Integer, Object> attributes = new HashMap<>();
        try {
            for (JSONObject result : resultList) {
                if (result.has("result")) {
                    JSONObject resultObject = result.getJSONObject("result");

                    if (resultObject.has("details")) {
                        JSONObject details = resultObject.getJSONObject("details");
                        int id = details.getInt("id");
                        if (id == issueId) {
                            JSONArray attributesArray = resultObject.getJSONArray("attributes");
                            for (int i = 0; i < attributesArray.length(); i++) {
                                JSONObject attribute = attributesArray.getJSONObject(i);
                                int attrId = attribute.getInt("id");
                                String attributeValue = attribute.optString("value", " ");
                                //System.out.println("ID :"+attrId+ " Value : "+attributeValue);
                                attributes.put(attrId, attributeValue);
                                AttributesMap.put(attrId, attributeValue);
                            }
                        } else {
                            System.err.println("Error: Issue with the specified ID not found in the JSON response.");
                        }
                    } else {
                        System.err.println("Error: 'details' not found in the JSON response.");
                    }
                } else {
                    System.err.println("Error: 'result' not found in the JSON response.");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return attributes;
    }

    @Override
    public Map<String, Object> getAttributes(int issueId) {
        Map<String, Object> attributesMap = new LinkedHashMap<>();
        try {
            for (JSONObject result : resultList) {
                if (result.has("result")) {
                    JSONObject resultObject = result.getJSONObject("result");
                    if (resultObject.has("details")) {
                        JSONObject details = resultObject.getJSONObject("details");
                        int id = details.getInt("id");
                        if (id == issueId) {
                            JSONArray attributesArray = resultObject.getJSONArray("attributes");
                            for (int i = 0; i < attributesArray.length(); i++) {
                                JSONObject attribute = attributesArray.getJSONObject(i);
                                int attrId = attribute.getInt("id");
                                String attributeName = null;
                                String attributeValue = attribute.optString("value", " ");
                                for (JSONObject type : typesList) {
                                    if (type.has("attributes")) {
                                        JSONArray typeAttributes = type.getJSONArray("attributes");
                                        for (int j = 0; j < typeAttributes.length(); j++) {
                                            JSONObject typeAttribute = typeAttributes.getJSONObject(j);
                                            int typeAttrId = typeAttribute.getInt("id");
                                            String typeAttrName = typeAttribute.optString("name", " ");
                                            if (typeAttrId == attrId) {
                                                attributeName = typeAttrName;

                                            }
                                        }
                                    }
                                }
                                attributesMap.put(attributeName, attributeValue);
                            }
                            return attributesMap;
                        }
                    } else {
                        System.err.println("Error: 'details' not found in the JSON response.");
                    }
                } else {
                    System.err.println("Error: 'result' not found in the JSON response.");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return attributesMap;
    }

    private String formatDateTime(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        return sdf.format(new Date(timestamp * 1000L));
    }

    @Override
    public Map<String, Object> getIssueDetails(int issueId) {
        Map<String, Object> issueDetailsMap = new HashMap<>();
        try {
            for (JSONObject result : resultList) {
                if (result.has("result")) {
                    JSONObject resultObject = result.getJSONObject("result");
                    if (resultObject.has("details")) {
                        JSONObject details = resultObject.getJSONObject("details");
                        int id = details.getInt("id");
                        if (id == issueId) {
                            name = details.getString("name");
                            int folderId = details.getInt("folderId");
                            int typeId = details.getInt("typeId");
                            String createdDate = formatDateTime(details.getLong("createdDate"));
                            int createdBy = details.getInt("createdBy");
                            String modifiedDate = formatDateTime(details.getLong("modifiedDate"));
                            int modifiedBy = details.getInt("modifiedBy");

                            String createdByUser = userMap.get(createdBy);
                            String typename = projectsDao.getTypeName(typeId);
                            String modifiedByUser = userMap.get(modifiedBy);

                            Object createdDetails = createdDate + " — " + createdByUser;
                            Object modifiedDetails = modifiedDate + " — " + modifiedByUser;

                            issueDetailsMap.put("ID", id);
                            issueDetailsMap.put("NAME", name);
                            issueDetailsMap.put("LOCATION", HomeFrame.PATH);
                            issueDetailsMap.put("TYPE", typename);
                            issueDetailsMap.put("CREATED", createdDetails);
                            issueDetailsMap.put("LAST MODIFIED", modifiedDetails);

                            return issueDetailsMap; // Return the details for the specified issue ID
                        }
                    } else {
                        System.err.println("Error: 'details' not found in the JSON response.");
                    }
                } else {
                    System.err.println("Error: 'result' not found in the JSON response.");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return issueDetailsMap;
    }

    @Override
    public Map<String, String> getHistory(int issueId) {
        Map<String, String> historyMap = new LinkedHashMap<>();

        Map<Integer, String> userMap = new HashMap<>();
        for (JSONObject user : usersList) {
            int id = user.optInt("id");
            String name = user.optString("name");
            userMap.put(id, name);
        }

        try {
            for (JSONObject result : resultList) {
                if (result.has("result")) {
                    JSONObject resultObject = result.getJSONObject("result");

                    int detailsId = resultObject.getJSONObject("details").optInt("id");

                    if (resultObject.has("history")) {
                        JSONArray historyArray = resultObject.getJSONArray("history");
                        //System.out.println("History Array : " + historyArray);
                        for (int i = 0; i < historyArray.length(); i++) {
                            JSONObject historyEntry = historyArray.getJSONObject(i);

                            if (detailsId == issueId) {
                                String createdDate = formatDateTime(historyEntry.optLong("createdDate"));
                                int createdBy = historyEntry.optInt("createdBy");
                                String createdByUser = userMap.get(createdBy);
                                String newValue = historyEntry.optString("new");
                                String oldValue = historyEntry.optString("old");
                                String text = historyEntry.optString("text");
                                String name = historyEntry.optString("name");
                                String attributeId = historyEntry.optString("attributeId");
                                int id = historyEntry.optInt("id");
                                int atrID = attributeId.isEmpty() ? 0 : Integer.parseInt(attributeId);
                                String attributeName = null;
                                for (JSONObject type : typesList) {
                                    if (type.has("attributes")) {
                                        JSONArray typeAttributes = type.getJSONArray("attributes");
                                        for (int j = 0; j < typeAttributes.length(); j++) {
                                            JSONObject typeAttribute = typeAttributes.getJSONObject(j);
                                            int typeAttrId = typeAttribute.optInt("id");
                                            String typeAttrName = typeAttribute.optString("name", " ");
                                            if (typeAttrId == atrID) {
                                                attributeName = typeAttrName;
                                            }
                                        }
                                    }
                                }

                                if (attributeName == null) {
                                    if (attributeId.isEmpty() && text.isEmpty() && name.isEmpty()) {
                                        attributeName = "NAME";
                                    } else if (attributeId.isEmpty()) {
                                        attributeName = "Comment";
                                    } else {
                                        attributeName = "UNKNOWN ATTRIBUTE";
                                    }
                                }
                                if (!name.isEmpty()) {
                                    attributeName = "ATTACHMENT";
                                }

                                String currentKey = createdDate + " — " + createdByUser;
                                String currentEntry = historyMap.get(currentKey);

                                if (currentEntry == null) {
                                    currentEntry = "";
                                }

                                if (!newValue.equals(oldValue) || (attributeName.equals("Comment") && detailsId == issueId)) {
                                    if (!currentEntry.isEmpty()) {
                                        currentEntry += "\n";
                                    }
                                }
//                                currentEntry += "   " + attributeName + ": "
//                                        + (attributeName.equals("Comment") ? text.replace("\n", " ")
//                                        : (attributeName.equals("ATTACHMENT") ? "{" + name + "}[id:" + id + "]"
//                                        : (oldValue + " → " + newValue)));
                                if (attributeName.equals("Comment")) {
                                    currentEntry += "   " + attributeName + ": " + text.replace("\n", " ") + "[id:" + id + "]";
                                } else if (attributeName.equals("ATTACHMENT")) {
                                    currentEntry += "   " + attributeName + ": {" + name + "}[id:" + id + "]";
                                } else {
                                    currentEntry += "   " + attributeName + ": " + oldValue + " → " + newValue;
                                }

                                //System.out.println("id : " + id);
                                historyMap.put(currentKey, currentEntry);
                            }
                        }
                    } else {
                        System.err.println("Error: 'history' not found in the JSON response.");
                    }
                } else {
                    System.err.println("Error: 'result' not found in the JSON response.");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return historyMap;
    }

    @Override
    public String getDescription(int issueID) {
        String text = null;
        try {
            for (JSONObject result : resultList) {
                if (result.has("result")) {
                    JSONObject resultObject = result.getJSONObject("result");
                    if (resultObject.has("description") && resultObject.get("description") instanceof JSONObject) {
                        JSONObject description = resultObject.getJSONObject("description");
                        if (description.has("text")) {
                            text = description.getString("text");
                        } else {
                            System.err.println("Error: 'text' not found in the 'description' object.");
                        }
                    } else {
                    }
                } else {
                    System.err.println("Error: 'result' not found in the JSON response.");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return text;
    }
    @Override
    public String deleteIssue(int issueId) {
        HttpURLConnection connection = null;
        try {
            String apiUrl = connectionDao.buildApiUrl("issues/delete.php");
            connection = connectionDao.establishConnection(apiUrl);
            if (connection != null && issueId != 0) {
                String jsonInputString = "{\"issueId\": " + issueId + "}";
                connection.getOutputStream().write(jsonInputString.getBytes(StandardCharsets.UTF_8));
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    StringBuilder response = new StringBuilder();
                    String inputLine;
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
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
            } else {
                return "Failed to establish a connection.";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to communicate with the API.";
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
    @Override
    public String markRead(int folderId) {
        HttpURLConnection connection = null;
        try {
            String apiUrl = connectionDao.buildApiUrl("issues/mark.php");
            connection = connectionDao.establishConnection(apiUrl);
            if (connection != null && folderId != 0) {
                String jsonInputString = "{\"folderId\": " + folderId + ", \"read\": true}";
                connection.getOutputStream().write(jsonInputString.getBytes(StandardCharsets.UTF_8));
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    StringBuilder response = new StringBuilder();
                    String inputLine;
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    // Parse and process the JSON response if needed
                    String jsonResponse = response.toString();
                    return "Issue with ID " + folderId + " deleted successfully. Response: " + jsonResponse;
                } else {
                    return "Failed to delete the issue. HTTP Error Code: " + responseCode;
                }
            } else {
                return "Failed to establish a connection.";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to communicate with the API.";
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
    public String getComment(int issueId, String commentId) {
        String text = null;
        HttpURLConnection connection = null;
        try {
            String apiUrl = connectionDao.buildApiUrl("issues/comments/load.php");
            connection = connectionDao.establishConnection(apiUrl);
            if (connection != null) {
                String jsonInputString = "{\"issueId\":" + issueId + ",\"commentId\":" + commentId + "}";
                JSONObject jsonResponse = connectionDao.makeRequestAndGetResponse(connection, jsonInputString);
                JSONObject result = (JSONObject) jsonResponse.get("result");
                if (result != null) {
                    text = (String) result.get("text");
                }
            }
        } catch (JSONException e) {
        }
        return text;
    }
    public void editDescription(int issueId, String text) {
        HttpURLConnection connection = null;
        try {
            String apiUrl = connectionDao.buildApiUrl("issues/description/edit.php");
            connection = connectionDao.establishConnection(apiUrl);
            if (connection != null) {
                String jsonInputString = "{\"issueId\": " + issueId + ", \"description\": \"" + text + "\", \"descriptionFormat\": 1}";
                try ( OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    System.out.println("Comment added successfully");
                } else {
                    System.out.println("Failed to add comment. Response Code: " + responseCode);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void editComment(int issueId, String text) {
        HttpURLConnection connection = null;
        try {
            String apiUrl = connectionDao.buildApiUrl("issues/comments/edit.php");
            connection = connectionDao.establishConnection(apiUrl);
            if (connection != null) {
                String jsonInputString = "{\"commentId\": " + issueId + ", \"comment\": \"" + text + "\", \"commentFormat\": 1}";
                try ( OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    System.out.println("Comment Edited successfully"+responseCode);
                } else {
                    System.out.println("Failed to Edit comment. Response Code: " + responseCode);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void addComment(int issueId, String text) {
        HttpURLConnection connection = null;
        try {
            String apiUrl = connectionDao.buildApiUrl("issues/comments/add.php");
            connection = connectionDao.establishConnection(apiUrl);

            if (connection != null) {
                String jsonInputString = "{\"issueId\": " + issueId + ", \"comment\": \"" + text + "\", \"commentFormat\": 1}";

                try ( OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    System.out.println("Comment added successfully");
                } else {
                    System.out.println("Failed to add comment. Response Code: " + responseCode);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String deleteDescription(int issueId) {
        HttpURLConnection connection = null;
        try {
            String apiUrl = connectionDao.buildApiUrl("issues/description/delete.php");
            connection = connectionDao.establishConnection(apiUrl);

            if (connection != null && issueId != 0) {
                String jsonInputString = "{\"issueId\": " + issueId + "}";
                connection.getOutputStream().write(jsonInputString.getBytes(StandardCharsets.UTF_8));

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    StringBuilder response = new StringBuilder();
                    String inputLine;
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

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
            } else {
                return "Failed to establish a connection.";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to communicate with the API.";
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
    public void deleteComment(String commentId){
         HttpURLConnection connection = null;
        try {
            String apiUrl = connectionDao.buildApiUrl("issues/comments/delete.php");
            connection = connectionDao.establishConnection(apiUrl);
            int commentID = Integer.parseInt(commentId);
            if (connection != null && commentID != 0) {
                String jsonInputString = "{\"commentId\": " + commentID + "}";
                connection.getOutputStream().write(jsonInputString.getBytes(StandardCharsets.UTF_8));
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    StringBuilder response = new StringBuilder();
                    String inputLine;
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close(); 
                } else { 
                }
            } else { 
            }
        } catch (IOException e) {
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
    @Override
    public String unMark(int folderId) {
        HttpURLConnection connection = null;
        try {
            String apiUrl = connectionDao.buildApiUrl("issues/mark.php");
            connection = connectionDao.establishConnection(apiUrl);
            if (connection != null && folderId != 0) {
                String jsonInputString = "{\"folderId\": " + folderId + ", \"read\": false}";
                connection.getOutputStream().write(jsonInputString.getBytes(StandardCharsets.UTF_8));
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    StringBuilder response = new StringBuilder();
                    String inputLine;
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    // Parse and process the JSON response if needed
                    String jsonResponse = response.toString();
                    return "Issue with ID " + folderId + " deleted successfully. Response: " + jsonResponse;
                } else {
                    return "Failed to delete the issue. HTTP Error Code: " + responseCode;
                }
            } else {
                return "Failed to establish a connection.";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to communicate with the API.";
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
    @Override
    public void editIssue() {
        HttpURLConnection connection = null;
        try {
            String apiUrl = connectionDao.buildApiUrl("issues/edit.php");
            connection = connectionDao.establishConnection(apiUrl);

            if (connection != null) {
                JSONObject requestObject = new JSONObject();
                JSONArray valuesArray = new JSONArray();
                for (Map.Entry<Integer, Object> entry : EditIssues.getAPIValues.entrySet()) {
                    Integer key = entry.getKey();
                    Object value = entry.getValue();

                    if (value instanceof Number) {
                        value = value.toString();
                    }
                    JSONObject entryObject = new JSONObject();
                    entryObject.put("id", key);
                    entryObject.put("value", value);
                    valuesArray.put(entryObject);
                }

                int issueId = HomeFrame.IssueID;
                String name = EditIssues.oldName;

                requestObject.put("issueId", issueId);
                requestObject.put("name", name);
                requestObject.put("values", valuesArray);

                String jsonInputString = requestObject.toString();
                System.out.println("JSON Input: " + jsonInputString);
                connection.setDoInput(true);
                connection.setDoOutput(true);

                try ( OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    try ( BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                        String line;
                        StringBuilder responseStringBuilder = new StringBuilder();
                        while ((line = reader.readLine()) != null) {
                            responseStringBuilder.append(line);
                        }
                        String responseBody = responseStringBuilder.toString();
                        System.out.println("Response Body: " + responseBody);
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        System.out.println("Response: " + jsonResponse);
                        if (jsonResponse.has("errorCode")) {
                            int errorCode = jsonResponse.getInt("errorCode");
                            String errorMessage = jsonResponse.getString("errorMessage");

                            JOptionPane.showMessageDialog(
                                    null,
                                    "API Error Code: " + errorCode + "\nError Message: " + errorMessage,
                                    "API Error",
                                    JOptionPane.ERROR_MESSAGE
                            );

                        }
                    }
                } else {
                    System.err.println("HTTP Error Response: " + responseCode);
                }
            } else {
                System.err.println("Failed to establish a connection.");
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
    @Override
    public void addIssue() {
        HttpURLConnection connection = null;
        try {
            String apiUrl = connectionDao.buildApiUrl("issues/add.php");
            connection = connectionDao.establishConnection(apiUrl);

            if (connection != null) {
                JSONObject requestObject = new JSONObject();
                JSONArray valuesArray = new JSONArray();

                for (Map.Entry<Integer, Object> entry : AddNewIssue.filteredValues.entrySet()) {
                    Integer key = entry.getKey();
                    Object value = entry.getValue();
                    System.out.println(" Key : " + key + " Value : " + value);
                    if (value instanceof Number) {
                        value = value.toString();
                    }

                    JSONObject entryObject = new JSONObject();
                    entryObject.put("id", key);
                    entryObject.put("value", value);
                    valuesArray.put(entryObject);
                }

                int folderId = HomeFrame.FoldersID;
                String name = AddNewIssue.name;
                requestObject.put("folderId", folderId);
                requestObject.put("name", name);
                requestObject.put("values", valuesArray);

                String desc = AddNewIssue.getDescription;
                if (desc != null && !desc.isEmpty()) {
                    requestObject.put("description", desc);
                    requestObject.put("descriptionFormat", 1);
                }
                String jsonInputString = requestObject.toString();
                System.out.println("JSON Input: " + jsonInputString);
                connection.setDoInput(true);
                connection.setDoOutput(true);

                try ( OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    try ( BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                        StringBuilder responseStringBuilder = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            responseStringBuilder.append(line);
                        }
                        String responseBody = responseStringBuilder.toString();
                        System.out.println("Response Body: " + responseBody);
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        System.out.println("Response: " + jsonResponse);
                        if (jsonResponse.has("errorCode")) {
                            int errorCode = jsonResponse.getInt("errorCode");
                            String errorMessage = jsonResponse.getString("errorMessage");

                            JOptionPane.showMessageDialog(
                                    null,
                                    "API Error Code: " + errorCode + "\nError Message: " + errorMessage,
                                    "API Error",
                                    JOptionPane.ERROR_MESSAGE
                            );

                        } else {
                        }
                    }
                } else {
                    System.err.println("HTTP Error Response: " + responseCode);
                }
            } else {
                System.err.println("Failed to establish a connection.");
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    @Override
    public void getIssueResult(int issueId) {
        String apiUrl = connectionDao.buildApiUrl("issues/load.php");
        try {
            HttpURLConnection connection = connectionDao.establishConnection(apiUrl);
            if (connection != null) {
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
                JSONObject jsonResponse = connectionDao.makeRequestAndGetResponse(connection, jsonInputString);
                if (jsonResponse != null) {
                    resultList.clear();
                    resultList.add(jsonResponse);
                } else {
                    System.err.println("Error: 'result' not found in the JSON response.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getFile(String id) {
        try {
            URL url = new URL(LoginFrame.apiUrl);
            String api = url.getProtocol() + "://" + url.getHost() + "/";
            String apiUrl = api;
            if (apiUrl.startsWith("https")) {
                apiUrl = apiUrl + "client/file.php?id=" + id;
            } else {
                apiUrl = apiUrl + "webissues/client/file.php?id=" + id;
            }
            HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
            connection.setRequestMethod("GET");
            System.out.println("api " + apiUrl);
            connection.setRequestProperty("Cookie", SessionManager.getInstance().getCookie());
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try ( InputStream inputStream = connection.getInputStream()) {
                    byte[] imageData;
                    try ( ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
                        byte[] buffer = new byte[16384];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            byteArrayOutputStream.write(buffer, 0, bytesRead);
                        }
                        imageData = byteArrayOutputStream.toByteArray();
//                        transferData = imageData;
//                        ShowImage img = new ShowImage();
//                        img.setVisible(true);
                        SwingUtilities.invokeLater(() -> {
                            displayImage(imageData);
                        });
                    }

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

    @Override
    public void displayImage(byte[] imageData) {
        if (imageData != null && imageData.length > 0) {
            try {
                if (ImageIO.read(new ByteArrayInputStream(imageData)) != null) {
                    ImageIcon imageIcon = new ImageIcon(ImageIO.read(new ByteArrayInputStream(imageData)));

                    FileName = HomeFrame.fileName;
                    if (imageIcon.getIconWidth() > 0) {
                        JFrame frame = new JFrame(FileName);
                        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        GraphicsDevice[] screens = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
                        int screenIndex = 0;
                        if (screenIndex >= 0 && screenIndex < screens.length) {
                            GraphicsConfiguration gc = screens[screenIndex].getDefaultConfiguration();
                            int screenWidth = gc.getBounds().width;
                            int screenHeight = gc.getBounds().height;
                            int frameWidth = 800;
                            int frameHeight = 600;
                            int x = (screenWidth - frameWidth) / 2;
                            int y = (screenHeight - frameHeight) / 2;
                            System.out.println("X " + x + " Y " + y);
                            frame.setLocation(x, y);
                        } else {

                            frame.setLocationRelativeTo(null);
                        }

                        JPanel panel = new JPanel(new BorderLayout());

                        JLabel imageLabel = new JLabel(imageIcon);
                        panel.add(imageLabel, BorderLayout.CENTER);

                        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

                        JButton saveButton = new JButton("SAVE");
                        saveButton.setBackground(Color.decode("#337ab7"));
                        //saveButton.setForeground(Color.decode("#fff"));
                        saveButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                try {
                                    String downloadsFolder = System.getProperty("user.home") + File.separator + "Downloads";
                                    String filePath = downloadsFolder + File.separator + FileName;
                                    FileOutputStream fileOutput = new FileOutputStream(filePath);
                                    fileOutput.write(imageData);
                                    fileOutput.close();
                                    System.out.println("Image saved!");
                                    frame.dispose();
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });

                        JButton cancelButton = new JButton("CANCEL");
                        cancelButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                frame.dispose();
                            }
                        });

                        buttonPanel.add(saveButton);
                        buttonPanel.add(cancelButton);
                        panel.add(buttonPanel, BorderLayout.SOUTH);

                        JScrollPane scrollPane = new JScrollPane(panel);
                        frame.add(scrollPane);
                        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
                        int screenWidth = gd.getDisplayMode().getWidth();

                        if (screenWidth > 1400) {
                            frame.setPreferredSize(new Dimension(1500, 800));
                            frame.pack();
                            frame.setVisible(true);
                        } else {
                            frame.setPreferredSize(new Dimension(800, 600));
                            frame.pack();
                            frame.setVisible(true);
                        }

                    } else {
                        System.err.println("Failed to load the image.");
                    }
                } else {
                    System.err.println("Image format not supported or image is corrupted.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Image data is empty or null.");
        }
    }

    @Override
    public void unMarkIssue(int issueId) {
        String apiUrl = connectionDao.buildApiUrl("issues/load.php");
        try {
            HttpURLConnection connection = connectionDao.establishConnection(apiUrl);
            if (connection != null) {
                String jsonInputString = "{"
                        + "\"issueId\": " + issueId + ","
                        + "\"description\": true,"
                        + "\"attributes\": true,"
                        + "\"history\": true,"
                        + "\"modifiedSince\": 0,"
                        + "\"filter\": 1,"
                        + "\"unread\": true,"
                        + "\"html\": true"
                        + "}";
                JSONObject jsonResponse = connectionDao.makeRequestAndGetResponse(connection, jsonInputString);
                if (jsonResponse != null) {
                    resultList.clear();
                    resultList.add(jsonResponse);
                } else {
                    System.err.println("Error: 'result' not found in the JSON response.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
