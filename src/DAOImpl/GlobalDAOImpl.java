 package DAOImpl;
import DAO.GlobalDAO;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets; 
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import pojos.SessionManager;
import webissuesFrame.LoginFrame;

public class GlobalDAOImpl implements GlobalDAO{
    @Override
     public String getUserName(int userId) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        String userName = null;

        try {
            URL url = new URL(LoginFrame.apiUrl);
            String api = url.getProtocol() + "://" + url.getHost() + "/";
            System.out.println("Original API " + api);
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
}
