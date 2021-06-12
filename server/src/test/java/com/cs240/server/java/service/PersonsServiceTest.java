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
import request.EventsRequest;
import request.PersonRequest;
import request.PersonsRequest;
import result.EventResult;
import result.EventsResult;
import result.PersonResult;
import result.PersonsResult;

import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PersonsServiceTest {


    private Database db;
    private PersonsService service;
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

        service = new PersonsService();
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
        Person person1 = new Person("Bob","Bob","Bob","Robert",
                "M","John",null,null);
        Person person2 = new Person("John","Bob","John","Johnson",
                "M",null,null,null);
        Person person3 = new Person("Liz","Liz","Liz","Elizabeth",
                "F",null,null,null);

        PersonsRequest request = new PersonsRequest("Bob");
        PersonsResult result = service.getAllPersons(request);

        assertTrue(result.isSuccess());
        assertEquals(2,result.getData().size());

        assertTrue(contains(result.getData(),person1));
        assertTrue(contains(result.getData(),person2));
        assertFalse(contains(result.getData(),person3));
    }


    /***
     * Test that Bob cannot get Liz's baptism event (They are not related)
     */
    @Test
    void getPersonFail() {
        PersonsRequest request = new PersonsRequest("Bubba");
        PersonsResult result = service.getAllPersons(request);

        assertEquals(0,result.getData().size());
        assertTrue(result.isSuccess());
    }



    private boolean contains(ArrayList<Person> persons, Person person){
        for(Person p : persons){
            if(p.equals(person)){
                return true;
            }
        }
        return false;
    }
}