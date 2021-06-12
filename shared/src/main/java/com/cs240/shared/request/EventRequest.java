package com.cs240.shared.request;

/***
 * Creates a request to get a specific event by its eventID.
 * EventID found in the URL.
 */
public class EventRequest {

    private String eventID;
    private String userPersonID;



    /***
     * Constructor.
     *
     * @param eventID - ID of the requested event.
     */
    public EventRequest(String eventID, String userPersonID) {
        this.eventID = eventID;
        this.userPersonID = userPersonID;
    };

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getUserPersonID() {
        return userPersonID;
    }

    public void setUserPersonID(String userPersonID) {
        this.userPersonID = userPersonID;
    }
}
