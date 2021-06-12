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
import result.ClearResult;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class ClearServiceTest {

    private Database db;
    private ClearService service;
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
        eDAO = new EventDAO(c);
        pDAO = new PersonDAO(c);
        uDAO =  new UserDAO(c);

        service = new ClearService();
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
     * The Database has been cleared.
     *
     * @throws DataAccessException - Ignore.
     */
    @Test
    void clearDatabasePass() throws DataAccessException {
        ClearResult result = service.clearDatabase();
        assertNull(aDAO.fetchToken("1111"));
        assertNull(aDAO.fetchToken("2222"));
        assertNull(aDAO.fetchToken("3333"));

        assertNull(eDAO.fetchEvent("1"));
        assertNull(eDAO.fetchEvent("2"));
        assertNull(eDAO.fetchEvent("3"));

        assertNull(pDAO.fetchPerson("Bob"));
        assertNull(pDAO.fetchPerson("John"));
        assertNull(pDAO.fetchPerson("Liz"));

        assertNull(uDAO.fetchUser("Bob"));
        assertNull(uDAO.fetchUser("username"));
        assertNull(uDAO.fetchUser("Katie"));

        assertEquals("Clear Succeeded.", result.getMessage());
        assertTrue(result.isSuccess());
    }
}