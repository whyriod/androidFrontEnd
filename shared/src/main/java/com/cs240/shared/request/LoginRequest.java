package com.cs240.shared.request;

/***
 * Creates a request to login in a user given a username and password.
 * These are found in the reqBody.
 */
public class LoginRequest {

    private String username;
    private String password;



    /***
     * Constructor.
     *
     * @param username - Username to login.
     * @param password - Password of user.
     */
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    };

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
