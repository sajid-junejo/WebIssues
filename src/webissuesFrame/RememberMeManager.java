/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webissuesFrame;

import java.util.prefs.Preferences;

public class RememberMeManager {
    private static final String USERNAME_KEY = "username";
    private static final String AUTH_TOKEN_KEY = "authToken";

    public static void saveCredentials(String username, String authToken) {
        Preferences prefs = Preferences.userNodeForPackage(RememberMeManager.class);

        prefs.put(USERNAME_KEY, username);
        prefs.put(AUTH_TOKEN_KEY, authToken);
    }

    public static String[] getSavedCredentials() {
        Preferences prefs = Preferences.userNodeForPackage(RememberMeManager.class);

        String username = prefs.get(USERNAME_KEY, null);
        String authToken = prefs.get(AUTH_TOKEN_KEY, null);

        if (username != null && authToken != null) {
            return new String[]{username, authToken};
        } else {
            return null;
        }
    }
}

