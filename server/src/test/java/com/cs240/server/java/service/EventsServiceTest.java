package com.cs240.server.java.service;

import dao.*;
import model.Event;
import model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.EventRequest;
import request.EventsRequest;
import result.EventResult;
import result.EventsResult;

import java.sql.Array;
import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class EventsServiceTest {

    private Database db;
    private EventsService service;
    private EventDAO eDAO;
    private PersonDAO pDAO;



    /***
     * Load Test data for event tests
     *
     * @throws DataAccessException - If this fails, they all fail
     * @throws ClassNotFoundException - If this fails, they all fail
     */
    @BeforeAll
    public static void load() throws DataAccessException, ClassNotFoundException {
        final String driver = "org.sqlite.JDBC";
        Class.forName(driver);
        //Create Database. And get the connection.
        Database db = new Database();
        Connection c = db.getConnection();
        EventDAO eDAO = new EventDAO(c);
        PersonDAO pDAO = new PersonDAO(c);

        //Set up blank tables
        eDAO.createTable();
        eDAO.clear();

        pDAO.createTable();
        pDAO.clear();

        //Event Data
        Event event1 = new Event("1","Birth","Bob","Bob",
                1000,"USA","New York",0,0);
        Event event2 = new Event("2","Death","John","John",
                1100,"JPN","Tokyo",100,100);
        Event event3 = new Event("3","Baptism","Liz","Liz",
                1200,"GER","Berlin",-400,-400);

        //Person data
        Person person1 = new Person("Bob","Bob","Bob","Robert",
                "M","John",null,null);
        Person person2 = new Person("John","Bob","John","Johnson",
                "M",null,null,null);
        Person person3 = new Person("Liz","Liz","Liz","Elizabeth",
                "F",null,null,null);

        //Insert test data
        eDAO.insertEvent(event1);
        eDAO.insertEvent(event2);
        eDAO.insertEvent(event3);

        pDAO.insertPerson(person1);
        pDAO.insertPerson(person2);
        pDAO.insertPerson(person3);

        //Close connection - Commit
        db.closeConnection(true);
    }


    /***
     * Creates a new connection to the database and initializes aDAO
     *
     * @throws DataAccessException - If this fails, they all fail
     * @throws ClassNotFoundException - If this fails, they all fail
     */
    @BeforeEach
    public void setUp() throws DataAccessException, ClassNotFoundException {
        db = new Database();
        Connection c = db.getConnection();
        eDAO = new EventDAO(c);
        pDAO = new PersonDAO(c);

        service = new EventsService();
    }



    /***
     * Closes connection and does not commit changes
     *
     * @throws DataAccessException - If this fails, they all fail
     */
    @AfterEach
    public void tearDown() throws DataAccessException {
        db.closeConnection(false);
    }



    /***
     * Test that get Events for Bob gets both Bobs 1 event and Johns 1 event.
     */
    @Test
    void getEventPass() {
        Event event1 = new Event("1","Birth","Bob","Bob",
                1000,"USA","New York",0,0);
        Event event2 = new Event("2","Death","John","John",
                1100,"JPN","Tokyo",100,100);
        Event event3 = new Event("3","Baptism","Liz","Liz",
                1200,"GER","Berlin",-400,-400);

        EventsRequest request = new EventsRequest("Bob");
        EventsResult result = service.getAllEvents(request);

        assertTrue(result.isSuccess());
        assertEquals(2,result.getData().size());

        assertTrue(contains(result.getData(),event1));
        assertTrue(contains(result.getData(),event2));
        assertFalse(contains(result.getData(),event3));
    }


    /***
     * Test that no events come back when looking for Jim
     */
    @Test
    void getEventFail() {
        EventsRequest request = new EventsRequest("Jim");
        EventsResult result = service.getAllEvents(request);

        assertEquals(0,result.getData().size());
        assertTrue(result.isSuccess());
    }

    private boolean contains(ArrayList<Event> events, Event event){
        for(Event e : events){
            if(e.equals(event)){
                return true;
            }
        }
        return false;
    }
}