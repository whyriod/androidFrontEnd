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
import request.LoadRequest;
import result.LoadResult;

import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class LoadServiceTest {


    private Database db;
    private LoadService service;
    private AuthTokenDAO aDAO;
    private EventDAO eDAO;
    private PersonDAO pDAO;
    private UserDAO uDAO;
    private LoadRequest request;


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

        //SETUP Load request.
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

        Event[] e = new Event[3];
        Person[] p = new Person[3];
        User[] u = new User[3];

        e[0] = event1;
        e[1] = event2;
        e[2] = event3;

        p[0] = person1;
        p[1] = person2;
        p[2] = person3;

        u[0] = user1;
        u[1] = user2;
        u[2] = user3;

        request = new LoadRequest(u,p,e);

        service = new LoadService();
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
     * Check that all persons, users, and events made it into the database.
     * @throws DataAccessException
     */
    @Test
    void loadDatabasePass() throws DataAccessException {
        LoadResult result = service.loadDatabase(request);

        assertTrue(result.isSuccess());
        assertNotNull(result.getMessage());

        assertEquals("Robert", pDAO.fetchPerson("Bob").getLastName());
        assertEquals("Johnson", pDAO.fetchPerson("John").getLastName());
        assertEquals("Elizabeth", pDAO.fetchPerson("Liz").getLastName());

        assertEquals("New York",eDAO.fetchEvent("1").getCity());
        assertEquals("Tokyo",eDAO.fetchEvent("2").getCity());
        assertEquals("Berlin",eDAO.fetchEvent("3").getCity());

        assertEquals("Bob",uDAO.fetchUser("Bob").getPassword());
        assertEquals("password",uDAO.fetchUser("username").getPassword());
        assertEquals("I<3Me",uDAO.fetchUser("Katie").getPassword());
    }



    /***
     * Checks to make sure that load request throws an error if
     * there are no events, persons, or users.
     */
    @Test
    void loadDatabaseMessage() {
        request = new LoadRequest(null,null,null);
        assertThrows(NullPointerException.class,() -> {service.loadDatabase(request);});
    }
}