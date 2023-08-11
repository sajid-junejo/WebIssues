/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package System;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import pojos.Principal;

public class SystemTypeManager  {
    /**
     * Flags
     */
    public static final int ForceDelete = 1;

    private static Map<Integer, Map<String, Object>> attributeTypes = new HashMap<>();

    /**
     * Constructor.
     */
    public SystemTypeManager() {
        super();
    }

    /**
     * Get list of issue types.
     * @return List of maps representing types.
     */
    public List<Map<String, Object>> getIssueTypes() {
        Principal principal = Principal.getCurrent();

        String query = "SELECT type_id, type_name FROM {issue_types}";
        if (!principal.isAuthenticated()) {
            query += " WHERE type_id IN ( SELECT f.type_id FROM {folders} AS f"
                    + " JOIN {projects} AS p ON p.project_id = f.project_id"
                    + " WHERE p.is_archived = 0 AND p.is_public = 1 )";
        } else if (!principal.isAdministrator()) {
            query += " WHERE EXISTS( SELECT * FROM {rights}"
                    + " WHERE user_id = %1d AND project_access = %2d AND project_id IN ( SELECT project_id FROM {projects} WHERE is_archived = 0 ) )"
                    + " OR type_id IN ( SELECT f.type_id FROM {folders} AS f"
                    + " JOIN {projects} AS p ON p.project_id = f.project_id"
                    + " WHERE p.is_archived = 0 AND ( p.project_id IN ( SELECT project_id FROM {rights} WHERE user_id = %1d ) OR p.is_public = 1 ) )";
        }
        query += " ORDER BY type_name COLLATE LOCALE";

        List<Map<String, Object>> result = new ArrayList<>();

        return result;
    }

    /**
     * Get the issue type with given identifier.
     * @param typeId Identifier of the type.
     * @return Map containing the issue type.
     * @throws System_Api_Error If the type is unknown.
     */
    public Map<String, Object> getIssueType(int typeId) throws SystemApiError {
        Principal principal = Principal.getCurrent();

        String query = "SELECT type_id, type_name FROM {issue_types} WHERE type_id = %1d";
        if (!principal.isAuthenticated()) {
            query += " AND type_id IN ( SELECT f.type_id FROM {folders} AS f"
                    + " JOIN {projects} AS p ON p.project_id = f.project_id"
                    + " WHERE p.is_archived = 0 AND p.is_public = 1 )";
        } else if (!principal.isAdministrator()) {
            query += " AND ( EXISTS( SELECT * FROM {rights}"
                    + " WHERE user_id = %2d AND project_access = %3d AND project_id IN ( SELECT project_id FROM {projects} WHERE is_archived = 0 ) )"
                    + " OR type_id IN ( SELECT f.type_id FROM {folders} AS f"
                    + " JOIN {projects} AS p ON p.project_id = f.project_id"
                    + " WHERE p.is_archived = 0 AND ( p.project_id IN ( SELECT project_id FROM {rights} WHERE user_id = %2d ) OR p.is_public = 1 ) ) )";
        }
        
        Map<String, Object> type = new HashMap<>();
        if (type == null) {
            throw new SystemApiError(SystemApiError.UnknownType, null);
        }

        return type;
    }
}

