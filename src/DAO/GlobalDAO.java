 
package DAO;

import java.util.List;
 
public interface GlobalDAO {
    public String getUserName(int userId);  
    void fetchData();
    List<Integer> getMembersByProjectId(int projectId);
    Object getDefaultValue(int attributeId);
    String getType(int attributeId);
    Integer getDecimal(int attributeId);
    boolean getRequired(int attributeId);
}
