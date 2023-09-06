package pojos;

import System.SystemApiError;

public class Principal {

    private static Principal current = null;
    private int userId = 0;
    private String userName = "";
    private int userAccess = 0;
    private String userEmail = null;
    private String language = null;

    public Principal(int userId) {
        // Retrieve user data using SessionManager
        SessionManager sessionManager = SessionManager.getInstance();
        Integer userID = sessionManager.getUserId(); // Use Integer for nullable types

        if (userID != null) {
            this.userId = userID;
            this.userName = sessionManager.getUserName();
            this.userAccess = sessionManager.getUserAccess();
        }
//         System.out.println("User ID: " + this.userId);
//         System.out.println("User Name: " + this.userName);
//         System.out.println("User Access: " + this.userAccess);
    }

    public static Principal getCurrent() {
        if (current == null) {
            current = new Principal(0);
        }
        return current;
    }

    /**
     * Set the current principal.
     */
    public static void setCurrent(Principal principal) {
        current = principal;
    }

    public String getUserName() {
        return userName;
    }

    public boolean isAuthenticated() {
        return userId != 0;
    }

    public int getUserAccess() {
        return userAccess;
    }

    public boolean isAdministrator() {
        
        return userAccess == SystemConst.AdministratorAccess;  
    }
    public void checkAuthenticated() throws SystemApiError {
        if (!isAuthenticated()) {
            throw new SystemApiError(SystemApiError.LoginRequired, null);
        }
    }

    public void checkAdministrator() throws SystemApiError {
        checkAuthenticated();
        if (!isAdministrator()) {
            throw new SystemApiError(SystemApiError.AccessDenied, null);
        }
    }

}
