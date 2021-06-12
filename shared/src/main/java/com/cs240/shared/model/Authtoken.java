package com.cs240.shared.model;

/***
* The Authtoken class is a representation of the Authtoken sqlite table
*/
public class Authtoken {

    private String authtoken;
    private String username;



    /***
     * Constructor.
     * @param associatedUsername - Username of person logging in
     * @param authtoken - AuthToken for that user
     */
    public Authtoken(String authtoken, String associatedUsername) {
        this.authtoken = authtoken;
        this.username = associatedUsername;
    }

    public String getusername() {
        return username;
    }

    public void setusername(String username) {
        this.username = username;
    }

    public String getauthtoken() {
        return authtoken;
    }

    public void setauthtoken(String authtoken) {
        this.authtoken = authtoken;
    }
}
