package com.cs240.server.java.service;

import dao.*;
import model.Authtoken;
import model.Event;
import model.Person;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.FillRequest;
import result.FillResult;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class FillServiceTest {

    private Database db;
    private FillService service;
    private EventDAO eDAO;
    private PersonDAO pDAO;



    /***
     * Load Test data for Clear tests
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
        AuthTokenDAO aDAO = new AuthTokenDAO(c);
        EventDAO eDAO = new EventDAO(c);
        PersonDAO pDAO = new PersonDAO(c);
        UserDAO uDAO =  new UserDAO(c);

        //Set up blank tables
        aDAO.createTable();
        aDAO.clear();

        eDAO.createTable();
        eDAO.clear();

        pDAO.createTable();
        pDAO.clear();

        uDAO.createTable();
        uDAO.clear();

        //Event data
        Event event1 = new Event("1","Birth","Bob","Bob",
                1000,"USA","New York",0,0);
        Event event2 = new Event("2","Death","Bob","Bob",
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

        //User data
        User user1 = new User("Bob","Bob","Bob","Bob",
                "Bob","Bob","M");


        eDAO.insertEvent(event1);
        eDAO.insertEvent(event2);
        eDAO.insertEvent(event3);

        pDAO.insertPerson(person1);
        pDAO.insertPerson(person2);
        pDAO.insertPerson(person3);

        uDAO.insertUser(user1);

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

        service = new FillService();
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
     * Checks that the fill service creates the proper number of users and events.
     */
    @Test
    void fillDatabasePass() throws DataAccessException {
        FillRequest request = new FillRequest("Bob", 1);
        FillResult result = service.fillDatabase(request);

        assertNotNull(pDAO.fetchPerson("Bob"));
        //Check that mother and father were created
        assertNotNull(pDAO.fetchPerson(pDAO.fetchPerson("Bob").getFatherID()));
        assertNotNull(pDAO.fetchPerson(pDAO.fetchPerson("Bob").getMotherID()));

        //Check that each have 3 events.
        assertEquals(3,eDAO.fetchEvents("Bob").size());
        assertEquals(3,eDAO.fetchEvents(pDAO.fetchPerson("Bob").getMotherID()).size());
        assertEquals(3,eDAO.fetchEvents(pDAO.fetchPerson("Bob").getFatherID()).size());
    }

    /***
     * Check that the Service fails on an unregistered user, and
     * that the service fails when the generation count is negative.
     */
    @Test
    void fillDatabaseFail() {
        FillRequest request = new FillRequest("JoeShmoe", 1);
        FillResult result = service.fillDatabase(request);

        assertFalse(result.isSuccess());
        assertEquals("Error: User does not exist", result.getMessage());

        request = new FillRequest("JoeShmoe", -1);
        result = service.fillDatabase(request);

        assertFalse(result.isSuccess());
        assertEquals("Error: Generations not positive", result.getMessage());
    }
}