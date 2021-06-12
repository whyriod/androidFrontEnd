package com.cs240.shared.result;

/***
 * Creates a request object for validating a username based on authtoken.
 * This is found in the reqBody.
 */
public class AuthResult {

    private String username;
    private String personID;
    private String authtoken;
    private String message;
    private boolean success;



    /**
     * Success Constructor.
     *
     * @param username - Users username.
     * @param personID - Users personID.
     * @param authtoken - Authtoken from AuthRequest.
     * @param success - True.
     */
    public AuthResult(String username, String personID, String authtoken, boolean success) {
        this.username = username;
        this.personID = personID;
        this.authtoken = authtoken;
        this.success = success;
    }



    /***
     * Failure Constructor
     *
     * @param message - “Error: [Description of the error]”.
     * @param success - False.
     */
    public AuthResult(String message, boolean success){
        this.message = message;
        this.success = success;
    }

    public String getusername() {
        return username;
    }

    public void setusername(String username) {
        this.username = username;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
