package com.cs240.shared.request;

/***
 * Creates a request to get a specific person based on the personID.
 * PersonID found in the URL.
 */
public class PersonRequest {

    private String personID;
    private String userPersonID;



    /***
     * Constructor.
     *
     * @param personID - PersonID of the requested person.
     * @param userPersonID - The clients personID
     */
    public PersonRequest(String personID, String userPersonID) {
        this.personID = personID;
        this.userPersonID = userPersonID;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getUserPersonID() {
        return userPersonID;
    }

    public void setUserPersonID(String userPersonID) {
        this.userPersonID = userPersonID;
    }
}
