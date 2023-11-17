package DAOImpl;

import DAO.ConnectionDAO;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.json.JSONException;
import org.json.JSONObject;
import pojos.SessionManager;
import webissuesFrame.LoginFrame;

/**
 *
 * @author sajid.ali
 */
public class ConnectionDAOImpl implements ConnectionDAO{

    @Override
    public String buildApiUrl(String endpoint) {
            try {
            URL url = new URL(LoginFrame.apiUrl);
            String api = url.getProtocol() + "://" + url.getHost() + "/";
            String apiUrl = api;
            if (apiUrl.startsWith("https")) {
                apiUrl = apiUrl + "server/api/" + endpoint; 
            } else {
                apiUrl = apiUrl + "webissues/server/api/" + endpoint;
            }
            return apiUrl;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
   public JSONObject makeRequestAndGetResponse(HttpURLConnection connection, String jsonInputString) throws JSONException {
    try {
        connection.getOutputStream().write(jsonInputString.getBytes(StandardCharsets.UTF_8));
        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }

            return new JSONObject(response.toString());
        } else {
            System.err.println("Error: HTTP Response Code " + responseCode);
           
        }
    } catch (IOException | JSONException e) {
    } finally {
        if (connection != null) {
            connection.disconnect();
        }
    }

    throw new JSONException("Failed to parse JSON response");
}

    @Override
    public HttpURLConnection establishConnection(String apiUrl) {
               HttpURLConnection connection = null;

        try {
            URL url = new URL(apiUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            connection.setRequestProperty("X-Csrf-Token", SessionManager.getInstance().getCsrfToken());
            connection.setRequestProperty("Cookie", SessionManager.getInstance().getCookie());
            connection.setRequestProperty("Content-Type", "application/json");

            connection.setDoInput(true);
            connection.setDoOutput(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return connection;
    }
    
}
