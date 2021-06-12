package com.cs240.shared.request;

import model.Event;
import model.Person;
import model.User;

/***
 * Creates a request to load an array of users, persons, and events into
 * the Database. These are found in the reqBody.
 */
public class LoadRequest {


    private User[] users;
    private Person[] persons;
    private Event[] events ;



    /***
     * Constructor.
     *
     * @param users - Array of users from ReqBody.
     * @param persons - Array of persons from ReqBody.
     * @param events - Array of events from ReqBody.
     */
    public LoadRequest(User[] users, Person[] persons, Event[] events) {
        this.users = users;
        this.persons = persons;
        this.events = events;
    }

    public User[] getUsers() {
        return users;
    }

    public void setUsers(User[] users) {
        this.users = users;
    }

    public Person[] getPersons() {
        return persons;
    }

    public void setPersons(Person[] persons) {
        this.persons = persons;
    }

    public Event[] getEvents() {
        return events;
    }

    public void setEvents(Event[] events) {
        this.events = events;
    }
}
