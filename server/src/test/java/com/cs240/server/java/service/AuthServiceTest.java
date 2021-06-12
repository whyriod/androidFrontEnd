package com.cs240.server.java.service;

import dao.*;
import model.Authtoken;
import model.Person;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.AuthRequest;
import result.AuthResult;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {

    private Database db;
    private AuthService service;



    /***
     * Load Test data for Authtoken tests
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
        PersonDAO pDAO = new PersonDAO(c);
        UserDAO uDAO = new UserDAO(c);

        //Set up blank tables
        aDAO.createTable();
        aDAO.clear();
        pDAO.createTable();
        pDAO.clear();
        uDAO.createTable();
        uDAO.clear();

        //Test data
        Authtoken token1 = new Authtoken("1111","Bob");
        Authtoken token2 = new Authtoken("2222","John");
        Authtoken token3 = new Authtoken("3333","Bob");

        //Person data
        Person person = new Person("Bob","Bob","Bob","Robert",
                "M","John",null,null);

        //User data
        User user = new User("Bob","Bob","Bob","Bob",
                "Bob","Bob","M");


        //Insert test data
        aDAO.insertToken(token1);
        aDAO.insertToken(token2);
        aDAO.insertToken(token3);

        pDAO.insertPerson(person);

        uDAO.insertUser(user);


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
        AuthTokenDAO aDAO = new AuthTokenDAO(c);
        service = new AuthService();
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
     * Test that authenticate is returning the correct authToken
     * for Bob.
     */
    @Test
    void authenticateUserPass() {
        AuthRequest request = new AuthRequest("1111");
        AuthResult result = service.authenticateUser(request);

        assertEquals("Bob",result.getPersonID());
        assertEquals("1111",result.getAuthtoken());
        assertTrue(result.isSuccess());
    }


    /***
     * Test that there is no user for authtoken 4444
     * and that the message is correct.
     */
    @Test
    void authenticateUserFail() {
        AuthRequest request = new AuthRequest("4444");
        AuthResult result = service.authenticateUser(request);

        assertNull(result.getAuthtoken());
        assertEquals("Error: Invalid auth token",result.getMessage());
        assertFalse(result.isSuccess());
    }
}