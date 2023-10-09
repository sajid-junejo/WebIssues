/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.util.List;
import java.util.Map;
import javax.swing.table.DefaultTableModel;
public interface IssuesDAO {
    DefaultTableModel getTableData(int folderId); 
    Map<String, Object> getAttributes(int issueId);
    Map<String, Object> getIssueDetails(int issueId);
    Map<String, String> getHistory(int issueId);
    String getDescription(int IssueID);
    String deleteIssue(int issueId);
}
