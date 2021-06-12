package com.cs240.shared.model;

/***
 * The Event class is a representation of the Event sqlite table
 */
public class Event {

    private String eventID;
    private String eventType;
    private String personID;
    private String associatedUsername;
    private int year;
    private String country;
    private String city;
    private float latitude;
    private float longitude;



    /***
     * Constructor
     *
     * @param eventID - Id of the event
     * @param eventType - Event-types: birth, baptism, christening, marriage, death
     * @param personID - ID of the person the event is for.
     * @param associatedUsername - Username of the person the event is for.
     * @param year - When the event occurred
     * @param country - Which country the event occurred in
     * @param city - Which city the event occurred in
     * @param latitude - Coordinates for city
     * @param longitude- Coordinates for city
     */
    public Event(String eventID, String eventType, String personID, String associatedUsername,
                 int year, String country, String city, float latitude, float longitude) {
        this.eventID = eventID;
        this.eventType = eventType;
        this.personID = personID;
        this.associatedUsername = associatedUsername;
        this.year = year;
        this.country = country;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        Event event = (Event) o;
        return this.eventID.equals(event.getEventID());
    }

}
