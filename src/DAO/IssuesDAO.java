/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.util.List;
import javax.swing.table.DefaultTableModel;
public interface IssuesDAO {
    //DefaultTableModel getIssuesByTypeId1();
    DefaultTableModel getIssuesByTypeId(int typeId, String folderName);
    //DefaultTableModel getIssuesByTypeId3(String folderName);
     DefaultTableModel createTableModel(String[] columnNames, List<Object[]> data);
     //DefaultTableModel getIssuesByTypeId3(String folderName,List<String> selectedColumns);
     List<String> getColumnNames();
     void deleteIssue(int issueId);
}
