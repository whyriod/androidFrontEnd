package com.cs240.shared.result;

import model.Person;

import java.util.ArrayList;

/***
 * Creates a GetAllPersonsResult object to return an array of person objects
 * based on db operation.
 */
public class PersonsResult {

    private ArrayList<Person> data;
    private String message;
    private boolean success;



    /***
     * Success Constructor
     * @param data - [Array of Person objects]
     * @param success - Boolean of success
     */
    public PersonsResult(ArrayList<Person> data, boolean success) {
        this.data = data;
        this.success = success;
    }



    /***
     * Error Constructor
     * @param message - “Error: [Description of the error]”
     * @param success - Boolean of success
     */
    public PersonsResult(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public ArrayList<Person> getData() {
        return data;
    }

    public void setData(ArrayList<Person> data) {
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
