package com.example.androidexample.features.user.data;

import com.example.androidexample.features.user.enums.AccessLevel;

/**
 * A Class that manages the user's session using static variables
 *
 * The session stores the user's token, ID, and access level. When the user logs
 * in, the session is saved. When the user logs out, the session is cleared. Getter
 * methods are provided to retrieve session information globally.
 *
 * @author Olivia Blais
 */
public class SessionManager {

    private static String USER_NAME;
    /**
     * The user's token for authentication.
     */
    private static String USER_TOKEN;
    /**
     * The user's ID.
     */
    private static int CURRENT_USER_ID;
    /**
     * The user's access level providing privileges.
     */
    private static AccessLevel USER_ACCESS_LEVEL;
    /**
     * Indicates whether the user is logged in or not.
     */
    private static boolean LOGGED_IN = false;

    /**
     * Saves the user's session by storing their token, ID, and access level.
     *
     * @param token       the user's token for authentication
     * @param userId      the user's ID
     * @param accessLevel the user's access level
     */
    public static void saveSession(String token, int userId, AccessLevel accessLevel, String username) {
        USER_NAME = username;
        USER_TOKEN = token;
        CURRENT_USER_ID = userId;
        LOGGED_IN = true;
        USER_ACCESS_LEVEL = accessLevel;
    }

    /**
     * Clears the session by resetting the user's token, ID, and access level.
     */
    public static void clearSession() {
        USER_TOKEN = null;
        CURRENT_USER_ID = 0;
        LOGGED_IN = false;
        USER_ACCESS_LEVEL = null;
    }

    /**
     * Returns true if the user is logged in, false otherwise.
     *
     * @return true if the user is logged in, false otherwise
     */
    public static boolean isLoggedIn() {
        return LOGGED_IN;
    }

    /**
     * Returns the user's access level.
     *
     * @return the user's access level
     */
    public static AccessLevel getAccessLevel() {
        return USER_ACCESS_LEVEL;
    }

    /**
     * Returns the user's token.
     *
     * @return the user's token
     */
    public static String getToken() {return USER_TOKEN;}

    /**
     * Returns the user's ID.
     *
     * @return the user's ID
     */
    public static int getUserId() {
        return CURRENT_USER_ID;
    }
    /**
     * Returns the user's username.
     *
     * @return the user's username
     */
    public static String getUsername() {return USER_NAME;}

}
