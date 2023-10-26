/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.net.HttpURLConnection;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author sajid.ali
 */
public interface ConnectionDAO {
    String buildApiUrl(String endpoint);
    JSONObject makeRequestAndGetResponse(HttpURLConnection connection, String jsonInputString)throws JSONException;
    HttpURLConnection establishConnection(String apiUrl);
}
