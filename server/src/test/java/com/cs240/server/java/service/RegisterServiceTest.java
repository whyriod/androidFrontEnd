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
import request.RegisterRequest;
import result.RegisterResult;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class RegisterServiceTest {

    private Database db;
    private RegisterService service;
    private AuthTokenDAO aDAO;
    private EventDAO eDAO;
    private PersonDAO pDAO;
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

        //Event data
        Event event1 = new Event("1","Birth","Bob","Bob",
                1000,"USA","New York",0,0);
        Event event2 = new Event("2","Death","John","Bob",
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
        User user2 = new User("username","username","password","username",
                "me","you","M");
        User user3 = new User("Katie","Katie","I<3Me","Katie",
                "Katie","Smiths","F");

        //Insert test data
        aDAO.insertToken(token1);
        aDAO.insertToken(token2);
        aDAO.insertToken(token3);

        eDAO.insertEvent(event1);
        eDAO.insertEvent(event2);
        eDAO.insertEvent(event3);

        pDAO.insertPerson(person1);
        pDAO.insertPerson(person2);
        pDAO.insertPerson(person3);

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
        eDAO = new EventDAO(c);
        pDAO = new PersonDAO(c);
        uDAO =  new UserDAO(c);

        service = new RegisterService();
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
     * Test that Bobs old information was cleared, and that bob has new relatives.
     */
    @Test
    void registerUserPass() throws DataAccessException {
        User user = new User("Bob","Bob","Bob","Bob",
                "Bob","Bob","M");
        RegisterRequest request =
                new RegisterRequest(user.getUsername(),user.getPassword(),user.getEmail(),
                                    user.getFirstName(),user.getLastName(),user.getGender());

        RegisterResult result = service.registerUser(request);
        assertTrue(result.isSuccess());
        assertEquals("Bob", aDAO.fetchToken(result.getAuthtoken()).getusername());
        //Persons
        assertNull(pDAO.fetchPerson("John"));
        assertNotNull(pDAO.fetchPerson("Liz"));

        //Events
        assertNull(eDAO.fetchEvent("1"));
        assertNull(eDAO.fetchEvent("2"));
        assertNotNull(eDAO.fetchEvent("3"));

        //Tokens
        assertNull(aDAO.fetchToken("1111"));
        assertNotNull(aDAO.fetchToken("2222"));
    }


    /***
     * Tests that a register request for a previously existing user fails.
     */
    @Test
    void registerUserAlreadyExists() {
        User user = new User("Bob","Bob","Bob","Bob",
                "Bob","Bob","M");
        RegisterRequest request =
                new RegisterRequest(user.getUsername(),user.getPassword(),user.getEmail(),
                        user.getFirstName(),user.getLastName(),user.getGender());

        RegisterResult result = service.registerUser(request);
        result = service.registerUser(request);

        assertFalse(result.isSuccess());
        assertEquals("Error: User already registered",result.getMessage());
    }
}