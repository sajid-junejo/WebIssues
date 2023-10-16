package DAOImpl;

import DAO.GlobalDAO;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import pojos.SessionManager;
import webissuesFrame.LoginFrame;

public class GlobalDAOImpl implements GlobalDAO {

    @Override
    public String getUserName(int userId) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        String userName = null;
        try {
            URL url = new URL(LoginFrame.apiUrl);
            String api = url.getProtocol() + "://" + url.getHost() + "/";
            String apiUrl = api + "server/api/global.php";
            //String apiUrl = api + "server/api/global.php";
            // Open a connection to the API endpoint using apiUrl
            connection = (HttpURLConnection) new URL(apiUrl).openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            // Set headers
            connection.setRequestProperty("X-Csrf-Token", SessionManager.getInstance().getCsrfToken());
            connection.setRequestProperty("Cookie", SessionManager.getInstance().getCookie());
            connection.setRequestProperty("Content-Type", "application/json");
            String jsonInputString = "{}";

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
                        JSONArray usersArray = result.getJSONArray("users");

                        // Iterate through the users array to find the matching ID
                        for (int i = 0; i < usersArray.length(); i++) {
                            JSONObject user = usersArray.getJSONObject(i);
                            int id = user.getInt("id");
                            if (id == userId) {
                                userName = user.getString("name");
                                break;
                            }
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

        return userName;
    }

    public String getServerName() {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        String serverName = null;
        try {
            URL url = new URL(LoginFrame.apiUrl);
            String api = url.getProtocol() + "://" + url.getHost() + "/";
            String apiUrl = api + "webissues-new/server/api/global.php";
            //String apiUrl = api + server/api/global.php";
            // Open a connection to the API endpoint using apiUrl
            System.out.println("API URL "+apiUrl);
            connection = (HttpURLConnection) new URL(apiUrl).openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            // Set headers
            connection.setRequestProperty("X-Csrf-Token", SessionManager.getInstance().getCsrfToken());
            connection.setRequestProperty("Cookie", SessionManager.getInstance().getCookie());
            connection.setRequestProperty("Content-Type", "application/json");
            String jsonInputString = "{}";

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
                        System.out.println("Result "+result);
                        serverName = result.getString("serverName");
                        System.out.println("Server Name "+serverName);
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

        return serverName;
    }

    @Override
    public String getAttributeName(int attributeId) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        String attributeName = null;

        try {
            URL url = new URL(LoginFrame.apiUrl);
            String api = url.getProtocol() + "://" + url.getHost() + "/";
            //String apiUrl = api + "server/api/global.php";
            String apiUrl = api + "server/api/global.php";
            // Open a connection to the API endpoint using apiUrl
            connection = (HttpURLConnection) new URL(apiUrl).openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            // Set headers
            connection.setRequestProperty("X-Csrf-Token", SessionManager.getInstance().getCsrfToken());
            connection.setRequestProperty("Cookie", SessionManager.getInstance().getCookie());
            connection.setRequestProperty("Content-Type", "application/json");

            // Construct the request body
            String jsonInputString = "{}";

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

                        if (result.has("types")) {
                            JSONArray typesArray = result.getJSONArray("types");

                            // Iterate through the types array
                            for (int i = 0; i < typesArray.length(); i++) {
                                JSONObject type = typesArray.getJSONObject(i);

                                if (type.has("attributes")) {
                                    JSONArray attributesArray = type.getJSONArray("attributes");

                                    // Iterate through the attributes array to find the matching ID
                                    for (int j = 0; j < attributesArray.length(); j++) {
                                        JSONObject attribute = attributesArray.getJSONObject(j);
                                        int id = attribute.getInt("id");

                                        if (id == attributeId) {
                                            attributeName = attribute.getString("name");
                                            return attributeName; // Return the name once found
                                        }
                                    }
                                }
                            }
                            // If the attributeId was not found, attributeName remains null
                        } else {
                            System.err.println("Error: 'types' not found in the JSON response.");
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

        return attributeName; // Return null if attribute is not found
    }

    public List<Integer> getMembersByProjectId(int projectId) {
        List<Integer> members = new ArrayList<>();
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        String text = null;

        try {
            URL url = new URL(LoginFrame.apiUrl);
            String api = url.getProtocol() + "://" + url.getHost() + "/";
            //String apiUrl = api + "server/api/global.php";
            String apiUrl = api + "server/api/global.php";
            connection = (HttpURLConnection) new URL(apiUrl).openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            // Set headers
            connection.setRequestProperty("X-Csrf-Token", SessionManager.getInstance().getCsrfToken());
            connection.setRequestProperty("Cookie", SessionManager.getInstance().getCookie());
            connection.setRequestProperty("Content-Type", "application/json");
            // Construct the request body
            String jsonInputString = "{}";

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
                    System.out.println("Response " + jsonResponse);
                    if (jsonResponse.has("result")) {
                        JSONObject result = jsonResponse.getJSONObject("result");
                        System.out.println("Result " + result);
                        JSONArray projectsArray = result.getJSONArray("projects");
                        System.out.println("Projects Array " + projectsArray);
                        for (int i = 0; i < projectsArray.length(); i++) {
                            JSONObject projectJson = projectsArray.getJSONObject(i);
                            int currentProjectId = projectJson.getInt("id");

                            if (currentProjectId == projectId) {
                                JSONArray membersArray = projectJson.getJSONArray("members");

                                int membersArrayLength = membersArray.length();
                                for (int j = 0; j < membersArrayLength; j++) {
                                    members.add(membersArray.getInt(j));
                                }

                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        } catch (Exception e) {

        }
        return members;
    }
}
