package com.cs240.shared.request;

/***
 * Creates a request object for validating a user based on authtoken.
 */
public class AuthRequest {

    private String authtoken;



    /***
     * Constructor.
     *
     * @param authtoken - Found in URL
     */
    public AuthRequest(String authtoken) {
        this.authtoken = authtoken;
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }
}
