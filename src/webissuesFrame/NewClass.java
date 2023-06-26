/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webissuesFrame;

import DAO.IssuesDAO;
import DAOImpl.IssuesDAOImpl;

/**
 *
 * @author sajid.ali
 */
public class NewClass {
    public static void main(String[] args) {
        IssuesDAOImpl isues = new IssuesDAOImpl();
        System.out.println(isues.getColumnNames());
    }
}
