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
import request.LoginRequest;
import result.LoginResult;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class LoginServiceTest {


    private Database db;
    private LoginService service;
    private AuthTokenDAO aDAO;
    private UserDAO uDAO;



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

        //Auth data
        Authtoken token1 = new Authtoken("1111","Bob");
        Authtoken token2 = new Authtoken("2222","John");
        Authtoken token3 = new Authtoken("3333","Bob");

        //User data
        User user1 = new User("Bob","Bob","Bob","Bob",
                "Bob","Bob","M");
        User user2 = new User("username","username","password","username",
                "me","you","M");
        User user3 = new User("Katie","Katie","I<3Me","Katie",
                "Katie","Smiths","F");

        //Insert test data
        aDAO.insertToken(token1);
        aDAO.insertToken(token2);
        aDAO.insertToken(token3);

        uDAO.insertUser(user1);
        uDAO.insertUser(user2);
        uDAO.insertUser(user3);

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
        aDAO = new AuthTokenDAO(c);
        uDAO =  new UserDAO(c);

        service = new LoginService();
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
     * Tests that User can log in properly and
     */
    @Test
    public void loginSuccess() throws DataAccessException {
        LoginRequest request = new LoginRequest("username","password");
        LoginResult result = service.loginUser(request);

        assertNotNull(aDAO.fetchToken(result.getAuthtoken()));
        assertEquals("username",result.getUsername());
        assertEquals("username",result.getPersonID());
        assertTrue(result.isSuccess());
        assertNull(result.getMessage());

    }


    /***
     * Tests that no random Joe Shmoe can log in if they arent registerd.
     */
    @Test
    public void loginFailure(){
        LoginRequest request = new LoginRequest("Joe","Shmoe");
        LoginResult result = service.loginUser(request);

        assertNull(result.getUsername());
        assertNull(result.getPersonID());
        assertFalse(result.isSuccess());
        assertEquals("Error: Invalid username or password", result.getMessage());
    }
}