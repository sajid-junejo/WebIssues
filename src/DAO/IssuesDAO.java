package DAO;
 
import java.util.Map;
import javax.swing.table.DefaultTableModel;
public interface IssuesDAO {
    DefaultTableModel getTableData(int folderId); 
    Map<String, Object> getAttributes(int issueId);
    Map<String, Object> getIssueDetails(int issueId);
    Map<String, String> getHistory(int issueId);
    String getDescription(int IssueID);
    String deleteIssue(int issueId);
    String markRead(int folderId);
    String unMark(int folderId);
    void unMarkIssue(int issueId);
    void editIssue();
    void addIssue();
    void getIssueResult(int issueId);
    void getFile(String id);
    void displayImage(byte[] imageData);
}
