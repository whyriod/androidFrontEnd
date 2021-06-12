package com.cs240.server.java.service;

import dao.DataAccessException;
import dao.Database;
import dao.EventDAO;
import dao.PersonDAO;
import model.Event;
import model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.EventRequest;
import request.PersonRequest;
import result.EventResult;
import result.PersonResult;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class PersonServiceTest {


    private Database db;
    private PersonService service;
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
        PersonDAO pDAO = new PersonDAO(c);

        //Creat Blank Tables
        pDAO.createTable();
        pDAO.clear();

        //Person data
        Person person1 = new Person("Bob","Bob","Bob","Robert",
                "M","John",null,null);
        Person person2 = new Person("John","Bob","John","Johnson",
                "M",null,null,null);
        Person person3 = new Person("Liz","Liz","Liz","Elizabeth",
                "F",null,null,null);

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
        pDAO = new PersonDAO(c);

        service = new PersonService();
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
     * Test that Bob can get Johns person info. (They are relatives)
     */
    @Test
    void getPersonPass() {
        PersonRequest request = new PersonRequest("John","Bob");
        PersonResult result = service.getPerson(request);

        assertTrue(result.isSuccess());
        assertEquals("Johnson",result.getLastName());
        assertEquals("M",result.getGender());
    }


    /***
     * Test that Bob cannot get Liz's baptism event (They are not related)
     */
    @Test
    void getPersonFail() {
        PersonRequest request = new PersonRequest("Liz","Bob");
        PersonResult result = service.getPerson(request);

        assertFalse(result.isSuccess());
        assertNull(result.getLastName());
        assertNull(result.getPersonID());
        assertEquals("Error: Unable to get person",result.getMessage());
    }
}