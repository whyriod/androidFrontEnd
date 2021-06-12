package com.cs240.shared.request;

/***
 * Creates a request to get all events related to a personID based on the
 * provided AuthToken. This is found in the reqBody.
 */
public class EventsRequest {

    private String personID;



    /***
     * Constructor.
     * 
     * @param personID - The personID passed in.
     */
    public EventsRequest(String personID) {
        this.personID = personID;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }
}
