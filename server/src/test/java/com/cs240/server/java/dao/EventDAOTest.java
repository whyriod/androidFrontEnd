package com.cs240.server.java.dao;

import model.Authtoken;
import model.Event;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/***
 * Tests all public EventDAO functions for a pass and a fail, other than
 * clear and delete table.
 */
class EventDAOTest {

    private Database db;
    private EventDAO eDAO;



    /***
     * Load Test data for Event tests
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

        //Set up blank tables
        eDAO.createTable();
        eDAO.clear();

        //Test data
        Event event1 = new Event("1","Birth","Bob","Bob",
                1000,"USA","New York",0,0);
        Event event2 = new Event("2","Death","Bob","Bob",
                1100,"JPN","Tokyo",100,100);
        Event event3 = new Event("3","Baptism","Ed","Ed",
                1200,"GER","Berlin",-400,-400);

        //Insert test data
        eDAO.insertEvent(event1);
        eDAO.insertEvent(event2);
        eDAO.insertEvent(event3);

        //Close connection - Commit
        db.closeConnection(true);
    }


    /***
     * Creates a new connection to the database and initializes eDAO
     *
     * @throws DataAccessException - If this fails, they all fail
     * @throws ClassNotFoundException - If this fails, they all fail
     */
    @BeforeEach
    public void setUp() throws DataAccessException, ClassNotFoundException {
        db = new Database();
        Connection c = db.getConnection();
        eDAO = new EventDAO(c);
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
     * Test that a new table has been created.
     * EventID 1 should have been loaded into the new table.
     *
     * @throws DataAccessException - Tried to find eventID 1 and did
     */
    @Test
    void createTablePass() throws DataAccessException {
        eDAO.dropTable();
        eDAO.createTable();
        Event event1 = new Event("1","The begining","0","Adam",
                -4000,"Eden","Garden",0,0);
        eDAO.insertEvent(event1);
        assertEquals("Garden", eDAO.fetchEvent("1").getCity());
    }



    /***
     * Test that a new table has been created.
     * EventID 1 should not be in the new table.
     *
     * @throws DataAccessException - Tried to find eventID 1 and didnt
     */
    @Test
    void createTablefail() throws DataAccessException {
        eDAO.dropTable();
        eDAO.createTable();
        assertNull(eDAO.fetchEvent("1"));
    }



    /***
     * Test that eventID 1 occurred in the USA.
     *
     * @throws DataAccessException - Ignore
     */
    @Test
    void fetchEventPass() throws DataAccessException {
        Event actual = eDAO.fetchEvent("1");
        assertEquals("USA",actual.getCountry());
    }



    /***
     * Test that eventID 9000 is not in database.
     *
     * @throws DataAccessException - Ignore
     */
    @Test
    void fetchEventFail() throws DataAccessException {
        Event actual = eDAO.fetchEvent("9000");
        assertNull(actual);
    }


    /***
     * Test that "Bob" has 2 events
     *
     * @throws DataAccessException - Ignore
     */
    @Test
    void fetchEventsPass() throws DataAccessException {
        ArrayList<Event> actual = eDAO.fetchEvents("Bob");
        assertEquals(2, actual.size());
    }


    /***
     * Test that Joseph has 0 events
     *
     * @throws DataAccessException - Ignore
     */
    @Test
    void fetchEventsFail() throws DataAccessException {
        ArrayList<Event> actual = eDAO.fetchEvents("Joseph");
        assertEquals(0,actual.size());
    }


    /***
     * Test that the Fredrick's baptism event was inserted into the database.
     *
     * @throws DataAccessException - Ignore
     */
    @Test
    void insertEventPass() throws DataAccessException {
        Event event = new Event("12","baptism","Fredrick",
                "Fredrick",0,"UK","London",0,0);
        eDAO.insertEvent(event);
        assertEquals("London",eDAO.fetchEvent("12").getCity());
    }


    /***
     * Test that the database dies when a null token is inserted.
     *
     * @throws DataAccessException - Ignore
     */
    @Test
    void insertEventFail() {
        Event actual = new Event(null,"baptism","Fredrick",
                "Fredrick",0,"UK","London",0,0);
        assertThrows(DataAccessException.class, () ->{eDAO.insertEvent(actual);});
    }



    /***
     * Deletes events associated with Bob
     */
    @Test
    void deleteEvent() throws DataAccessException {
        eDAO.deleteEvent("Bob");
        assertNull(eDAO.fetchEvent("1"));
        assertNull(eDAO.fetchEvent("2"));
    }



    /***
     * Deletes tokens associated with Ed. Checks to make sure
     * that Bobs were not deleted along with Ed.
     */
    @Test
    void deleteEventPersist() throws DataAccessException {
        eDAO.deleteEvent("Ed");
        assertEquals("USA",eDAO.fetchEvent("1").getCountry());
        assertEquals("JPN",eDAO.fetchEvent("2").getCountry());
    }



    /***
     * Test that the database is cleared by checking
     * to see that eventID 1 is gone.
     *
     * @throws DataAccessException - Ignore
     */
    @Test
    void clear() throws DataAccessException {
        eDAO.clear();
        assertNull(eDAO.fetchEvent("1"));
    }
}