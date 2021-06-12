package com.cs240.shared.request;

/***
 * Creates a request to get a all persons based on the personID.
 * PersonID found through authentication
 */
public class PersonsRequest {

    private String personID;



    /***
     * Constructor.
     *
     * @param personID - PersonID of the requested person.
     */
    public PersonsRequest(String personID) {
        this.personID = personID;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }
}
