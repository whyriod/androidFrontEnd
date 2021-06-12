package com.cs240.server.java.service;

import dao.*;
import model.Event;
import model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.EventRequest;
import result.EventResult;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class EventServiceTest {

    private Database db;
    private EventService service;
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

        service = new EventService();
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
     * Test that Bob can get Johns Death event in Tokyo. (They are relatives)
     */
    @Test
    void getEventPass() {
        EventRequest request = new EventRequest("2","Bob");
        EventResult result = service.getEvent(request);

        assertTrue(result.isSuccess());
        assertEquals("Tokyo",result.getCity());
        assertEquals("Death",result.getEventType());
    }


    /***
     * Test that Bob cannot get Liz's baptism event (They are not related)
     */
    @Test
    void getEventFail() {
        EventRequest request = new EventRequest("3","Bob");
        EventResult result = service.getEvent(request);

        assertFalse(result.isSuccess());
        assertNull(result.getEventID());
        assertEquals("Error: Unable to get event",result.getMessage());
    }
}