package DAOImpl;

import DAO.GlobalDAO;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GlobalDAOImpl implements GlobalDAO {

    private List<JSONObject> typesList;
    private List<JSONObject> resultList;
    private List<JSONObject> userList;

    public GlobalDAOImpl() {
        typesList = new ArrayList<>();
        userList = new ArrayList<>();
        resultList = new ArrayList<>();
        fetchData();
        //result();
    }

    public List<JSONObject> getResultList() {
        return resultList;
    }

    public List<JSONObject> getTypesList() {
        return typesList;
    }

    public List<JSONObject> getUsersList() {
        return userList;
    }
    ConnectionDAOImpl issueDao = new ConnectionDAOImpl();

    @Override
    public String getUserName(int userId) {
        try {
            for (JSONObject jsonResponse : resultList) {
                if (jsonResponse != null && jsonResponse.has("result")) {
                    JSONObject result = jsonResponse.getJSONObject("result");
                    JSONArray usersArray = result.getJSONArray("users");

                    for (int i = 0; i < usersArray.length(); i++) {
                        JSONObject user = usersArray.getJSONObject(i);
                        int id = user.getInt("id");

                        if (id == userId) {
                            return user.getString("name");
                        }
                    }
                } else {
                    System.err.println("Error: 'result' not found in the JSON response.");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
 

    @Override
    public void fetchData() {
        String apiUrl = issueDao.buildApiUrl("global.php");
        String jsonInputString = "{}";
        HttpURLConnection connection = issueDao.establishConnection(apiUrl);
        if (connection != null) {
            try {
                JSONObject jsonResponse = issueDao.makeRequestAndGetResponse(connection, jsonInputString);

                if (jsonResponse != null && jsonResponse.has("result")) {
                    resultList.clear();
                    userList.clear();
                    typesList.clear();
                    JSONObject result = jsonResponse.getJSONObject("result");
                    resultList.add(jsonResponse);
                    JSONArray usersArray = result.getJSONArray("users");

                    for (int i = 0; i < usersArray.length(); i++) {
                        JSONObject userJSON = usersArray.getJSONObject(i);
                        userList.add(userJSON);
                    }
                    if (result.has("types")) {
                        JSONArray typesArray = result.getJSONArray("types");

                        for (int i = 0; i < typesArray.length(); i++) {
                            JSONObject type = typesArray.getJSONObject(i);
                            typesList.add(type);
                        }
                    } else {
                        System.err.println("Error: 'types' not found in the JSON response.");
                    }
                } else {
                    System.err.println("Error: 'result' not found in the JSON response.");
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
    }

    @Override
    public boolean getRequired(int attributeId) {
        try {
            for (JSONObject jsonResponse : resultList) {
                if (jsonResponse == null || !jsonResponse.has("result")) {
                    System.err.println("Error: 'result' not found in the JSON response.");
                    return false;
                }

                JSONObject result = jsonResponse.getJSONObject("result");

                if (!result.has("types")) {
                    System.err.println("Error: 'types' not found in the JSON response.");
                    return false;
                }

                JSONArray typesArray = result.getJSONArray("types");

                for (int i = 0; i < typesArray.length(); i++) {
                    JSONObject type = typesArray.getJSONObject(i);

                    if (type.has("attributes")) {
                        JSONArray attributesArray = type.getJSONArray("attributes");

                        for (int j = 0; j < attributesArray.length(); j++) {
                            JSONObject attribute = attributesArray.getJSONObject(j);
                            int id = attribute.getInt("id");

                            if (id == attributeId && attribute.has("required")) {
                                return attribute.getInt("required") == 1;
                            } 
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Integer getDecimal(int attributeId) { 
        try { 
            for (JSONObject jsonResponse : resultList) {
                if (jsonResponse != null && jsonResponse.has("result")) {
                    JSONObject result = jsonResponse.getJSONObject("result");

                    if (result.has("types")) {
                        JSONArray typesArray = result.getJSONArray("types");

                        for (int i = 0; i < typesArray.length(); i++) {
                            JSONObject type = typesArray.getJSONObject(i);

                            if (type.has("attributes")) {
                                JSONArray attributesArray = type.getJSONArray("attributes");

                                for (int j = 0; j < attributesArray.length(); j++) {
                                    JSONObject attribute = attributesArray.getJSONObject(j);
                                    int id = attribute.getInt("id");

                                    if (id == attributeId) {
                                        if (attribute.has("decimal")) {
                                            return attribute.getInt("decimal");
                                        } 
                                    }
                                }
                            }
                        }
                    } else {
                        System.err.println("Error: 'types' not found in the JSON response.");
                    }
                } else {
                    System.err.println("Error: 'result' not found in the JSON response.");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null; // Return null if the attribute is not found or does not have the "decimal" property
    }

    @Override
    public String getType(int attributeId) {
        try {
            for (JSONObject jsonResponse : resultList) {
                if (jsonResponse != null && jsonResponse.has("result")) {
                    JSONObject result = jsonResponse.getJSONObject("result");

                    if (result.has("types")) {
                        JSONArray typesArray = result.getJSONArray("types");

                        for (int i = 0; i < typesArray.length(); i++) {
                            JSONObject type = typesArray.getJSONObject(i);

                            if (type.has("attributes")) {
                                JSONArray attributesArray = type.getJSONArray("attributes");

                                for (int j = 0; j < attributesArray.length(); j++) {
                                    JSONObject attribute = attributesArray.getJSONObject(j);
                                    int id = attribute.getInt("id");

                                    if (id == attributeId) {
                                        if (attribute.has("type")) {
                                            return attribute.getString("type");
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        System.err.println("Error: 'types' not found in the JSON response.");
                    }
                } else {
                    System.err.println("Error: 'result' not found in the JSON response.");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null; // Return null if the attribute is not found or does not have the "decimal" property
    }

    @Override
    public Object getDefaultValue(int attributeId) {
        try {
            //JSONObject jsonResponse = issueDao.makeRequestAndGetResponse(connection, jsonInputString);
            for (JSONObject jsonResponse : resultList) {
                if (jsonResponse != null && jsonResponse.has("result")) {
                    JSONObject result = jsonResponse.getJSONObject("result");

                    if (result.has("types")) {
                        JSONArray typesArray = result.getJSONArray("types");

                        for (int i = 0; i < typesArray.length(); i++) {
                            JSONObject type = typesArray.getJSONObject(i);

                            if (type.has("attributes")) {
                                JSONArray attributesArray = type.getJSONArray("attributes");

                                for (int j = 0; j < attributesArray.length(); j++) {
                                    JSONObject attribute = attributesArray.getJSONObject(j);
                                    int id = attribute.getInt("id");

                                    if (id == attributeId) {
                                        if (attribute.has("default")) {
                                            return attribute.getString("default");
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        System.err.println("Error: 'types' not found in the JSON response.");
                    }
                } else {
                    System.err.println("Error: 'result' not found in the JSON response.");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null; // Return null if the attribute is not found or does not have a default value
    }

    @Override
    public List<Integer> getMembersByProjectId(int projectId) {
        List<Integer> members = new ArrayList<>();
        try {
            for (JSONObject jsonResponse : resultList) {
                if (jsonResponse != null && jsonResponse.has("result")) {
                    JSONObject result = jsonResponse.getJSONObject("result");
                    JSONArray projectsArray = result.getJSONArray("projects");

                    for (int i = 0; i < projectsArray.length(); i++) {
                        JSONObject projectJson = projectsArray.getJSONObject(i);
                        int currentProjectId = projectJson.getInt("id");

                        if (currentProjectId == projectId) {
                            JSONArray membersArray = projectJson.getJSONArray("members");

                            for (int j = 0; j < membersArray.length(); j++) {
                                members.add(membersArray.getInt(j));
                            }
                            break;
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return members;
    }

    public Object getMinValue(int attributeId) {
     try {
            for (JSONObject jsonResponse : resultList) {
                if (jsonResponse != null && jsonResponse.has("result")) {
                    JSONObject result = jsonResponse.getJSONObject("result");

                    if (result.has("types")) {
                        JSONArray typesArray = result.getJSONArray("types");

                        for (int i = 0; i < typesArray.length(); i++) {
                            JSONObject type = typesArray.getJSONObject(i);

                            if (type.has("attributes")) {
                                JSONArray attributesArray = type.getJSONArray("attributes");

                                for (int j = 0; j < attributesArray.length(); j++) {
                                    JSONObject attribute = attributesArray.getJSONObject(j);
                                    int id = attribute.getInt("id");

                                    if (id == attributeId) {
                                        if (attribute.has("min-value")) {
                                            return attribute.getString("min-value");
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        System.err.println("Error: 'types' not found in the JSON response.");
                    }
                } else {
                    System.err.println("Error: 'result' not found in the JSON response.");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
    
    public Object getMaxValue(int attributeId) {
     try {
            for (JSONObject jsonResponse : resultList) {
                if (jsonResponse != null && jsonResponse.has("result")) {
                    JSONObject result = jsonResponse.getJSONObject("result");

                    if (result.has("types")) {
                        JSONArray typesArray = result.getJSONArray("types");

                        for (int i = 0; i < typesArray.length(); i++) {
                            JSONObject type = typesArray.getJSONObject(i);

                            if (type.has("attributes")) {
                                JSONArray attributesArray = type.getJSONArray("attributes");

                                for (int j = 0; j < attributesArray.length(); j++) {
                                    JSONObject attribute = attributesArray.getJSONObject(j);
                                    int id = attribute.getInt("id"); 
                                    if (id == attributeId) {
                                        if (attribute.has("max-value")) {
                                            return attribute.getString("max-value");
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        System.err.println("Error: 'types' not found in the JSON response.");
                    }
                } else {
                    System.err.println("Error: 'result' not found in the JSON response.");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
