package com.cs240.shared.result;

import model.Event;

import java.util.ArrayList;

/***
 * Creates a GetAllEventsResult object to return an array of event objects
 * based on db operation.
 */
public class EventsResult {

    private ArrayList<Event> data;
    private String message;
    private boolean success;



    /***
     * Success Constructor
     * @param data - Array of Event Objects
     * @param success - Boolean of success
     */
    public EventsResult(ArrayList<Event> data, boolean success) {
        this.data = data;
        this.success = success;
    }



    /***
     * Error Constructor
     * @param message - “Error: [Description of the error]”
     * @param success - Boolean of success
     */
    public EventsResult(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public ArrayList<Event> getData() {
        return data;
    }

    public void setData(ArrayList<Event> data) {
        this.data = data;
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
