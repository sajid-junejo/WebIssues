/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pojos;

/**
 *
 * @author sajid.ali
 */
public class Sessions {

    private char session_id;
    private int user_id;
    private String session_data;
    private int last_access;

    public char getSession_id() {
        return session_id;
    }

    public void setSession_id(char session_id) {
        this.session_id = session_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getSession_data() {
        return session_data;
    }

    public void setSession_data(String session_data) {
        this.session_data = session_data;
    }

    public int getLast_access() {
        return last_access;
    }

    public void setLast_access(int last_access) {
        this.last_access = last_access;
    }
}
