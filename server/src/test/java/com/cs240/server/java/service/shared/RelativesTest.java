package com.cs240.server.java.service.shared;

import dao.*;
import model.Authtoken;
import model.Event;
import model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class RelativesTest {

    private Database db;
    private EventDAO eDAO;
    private PersonDAO pDAO;
    private Relatives relatives;

    /***
     * Load Test data for relative tests
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
        PersonDAO pDAO = new PersonDAO(c);
        EventDAO eDAO = new EventDAO(c);

        //Set up blank tables
        pDAO.createTable();
        pDAO.clear();
        eDAO.createTable();
        eDAO.clear();

        //Person data
        Person person1 = new Person("Bob","Bob","Bob","Robert",
                "M","John",null,null);
        Person person2 = new Person("John","Bob","John","Johnson",
                "M",null,null,null);
        Person person3 = new Person("Liz","Liz","Liz","Elizabeth",
                "F",null,null,null);

        //Event data
        Event event1 = new Event("1","Birth","Bob","Bob",
                1000,"USA","New York",0,0);
        Event event2 = new Event("2","Death","Bob","Bob",
                1100,"JPN","Tokyo",100,100);
        Event event3 = new Event("3","Baptism","Liz","Liz",
                1200,"GER","Berlin",-400,-400);

        //Insert test data
        pDAO.insertPerson(person1);
        pDAO.insertPerson(person2);
        pDAO.insertPerson(person3);

        eDAO.insertEvent(event1);
        eDAO.insertEvent(event2);
        eDAO.insertEvent(event3);

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
        pDAO = new PersonDAO(c);
        eDAO = new EventDAO(c);
        relatives = new Relatives();
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
     * Check that Bobs relatives include Bob and John.
     *
     * @throws DataAccessException - Ignore.
     */
    @Test
    void getRelativesPass() throws DataAccessException {
        ArrayList<Person> family = relatives.getRelatives(pDAO.fetchPerson("Bob"),pDAO);
        assertTrue(relatives.contains(family,pDAO.fetchPerson("John")));
        assertTrue(relatives.contains(family,pDAO.fetchPerson("Bob")));
    }


    /***
     * Check that Bobs relatives does not include Liz.
     *
     * @throws DataAccessException - Ignore.
     */
    @Test
    void getRelativesFail() throws DataAccessException {
        ArrayList<Person> family = relatives.getRelatives(pDAO.fetchPerson("Bob"),pDAO);
        assertFalse(relatives.contains(family,pDAO.fetchPerson("Liz")));
    }



    /***
     * Check that The new array contains John Cena
     */
    @Test
    void containsPass() {
        ArrayList<Person> family = new ArrayList<>();

        Person person = new Person("John","Cena","DahDah","DAHHHH",
                "M","John",null,null);

        family.add(person);
        assertTrue(relatives.contains(family,person));
    }



    /***
     * Check that the new array does not contain John Cena
     */
    @Test
    void containsFail() {
        ArrayList<Person> family = new ArrayList<>();

        Person person = new Person("John","Cena","DahDah","DAHHHH",
                "M","John",null,null);

        assertFalse(relatives.contains(family,person));
    }



    /***
     * Check that Bob has 2 events from getFamilyEvents.
     * @throws DataAccessException
     */
    @Test
    void getFamilyEventsPass() throws DataAccessException {
        ArrayList<Person> family = relatives.getRelatives(pDAO.fetchPerson("Bob"),pDAO);
        assertEquals(2,relatives.getFamilyEvents(family, eDAO).size());
    }



    /***
     * Check that Bobs father, John, has no familyevents from his relatives
     * (Only looking above him in the family tree)
     * @throws DataAccessException
     */
    @Test
    void getFamilyEventsFail() throws DataAccessException {
        ArrayList<Person> family = relatives.getRelatives(pDAO.fetchPerson("John"),pDAO);
        assertEquals(0,relatives.getFamilyEvents(family, eDAO).size());
    }
}