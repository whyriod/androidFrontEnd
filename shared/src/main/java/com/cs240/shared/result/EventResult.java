package com.cs240.shared.result;

/***
 * Creates a GetEventResult object to return a specific event
 * based on db operation.
 */
public class EventResult {

    private String eventID;
    private String eventType;
    private String personID;
    private String associatedUsername;
    private int year;
    private String country;
    private String city;
    private float latitude;
    private float longitude;
    private String message;
    private boolean success;



    /***
     * Success Constructor
     * @param eventID - Id of event.
     * @param eventType - EventType of event
     * @param personID - PersonID of event
     * @param associatedUsername - Username of event
     * @param year - Year event occurred
     * @param country - Country of event
     * @param city - City of event
     * @param latitude - Coordinates of event
     * @param longitude - Coordinates of event
     * @param success - Boolean of success
     */
    public EventResult(String eventID, String eventType, String personID, String associatedUsername, int year,
                       String country, String city, float latitude, float longitude, boolean success) {
        this.eventID = eventID;
        this.eventType = eventType;

        this.personID = personID;
        this.associatedUsername = associatedUsername;
        this.year = year;
        this.country = country;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
        this.success = success;
    }



    /***
     * Error Constructor
     * @param message - “Error: [Description of the error]”
     * @param success - Boolean of success
     */
    public EventResult(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public float getlatitude() {
        return latitude;
    }

    public void setlatitude(float latitude) {
        latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        longitude = longitude;
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
