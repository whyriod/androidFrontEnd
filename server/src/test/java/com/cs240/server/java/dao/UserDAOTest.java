package com.cs240.server.java.dao;

import model.Authtoken;
import model.Person;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

/***
 * Tests all public UserDAO functions for a pass and a fail, other than
 * clear and delete table.
 */
class UserDAOTest {

    private Database db;
    private UserDAO uDAO;



    /***
     * Load Test data for User tests
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
        UserDAO uDAO = new UserDAO(c);

        //Set up blank tables
        uDAO.createTable();
        uDAO.clear();

        //Test data
        User user1 = new User("Bob","Bob","Bob","Bob",
                "Bob","Bob","M");
        User user2 = new User("username","username","password","username",
                "me","you","M");
        User user3 = new User("Katie","Katie","I<3Me","Katie",
                "Katie","Smiths","F");

        //Insert test data
        uDAO.insertUser(user1);
        uDAO.insertUser(user2);
        uDAO.insertUser(user3);

        //Close connection - Commit
        db.closeConnection(true);
    }



    /***
     * Creates a new connection to the database and initializes uDAO
     *
     * @throws DataAccessException - If this fails, they all fail
     * @throws ClassNotFoundException - If this fails, they all fail
     */
    @BeforeEach
    public void setUp() throws DataAccessException, ClassNotFoundException {
        db = new Database();
        Connection c = db.getConnection();
        uDAO = new UserDAO(c);
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
     * Nate should have been loaded into the new table.
     *
     * @throws DataAccessException - Tried to find Nate and did
     */
    @Test
    void createTablePass() throws DataAccessException {
        uDAO.dropTable();
        uDAO.createTable();
        User user1 = new User("Nate","Nate","coolbro","Nate",
                "Nate","Thomas","M");
        uDAO.insertUser(user1);
        assertEquals(uDAO.fetchUser("Nate","coolbro").getLastName(), "Thomas");
    }



    /***
     * Test that a new table has been created.
     * Nate should not be in the new table.
     *
     * @throws DataAccessException - Tried to find Nate and didnt
     */
    @Test
    void createTablefail() throws DataAccessException {
        uDAO.dropTable();
        uDAO.createTable();
        assertNull(uDAO.fetchUser("Nate","coolbro"));
    }



    /***
     * Test that the database fetched Katie
     *
     * @throws DataAccessException - Ignore
     */
    @Test
    void fetchUserPass() throws DataAccessException {
        User user = uDAO.fetchUser("Katie","I<3Me");
        assertEquals("Smiths",user.getLastName());
    }



    /***
     * Test that the database did not fetch BrighamYoung
     *
     * @throws DataAccessException - Ignore
     */
    @Test
    void fetchUserFail() throws DataAccessException {
        User user = uDAO.fetchUser("BrighamYoung","BYU");
        assertNull(user);
    }



    /***
     * Test that the database inserted Tim Jones
     *
     * @throws DataAccessException - Ignore
     */
    @Test
    void insertUserPass() throws DataAccessException {
        User user = new User("person","Robot","IROBOT","google",
                "Tim","Jones","M");
        uDAO.insertUser(user);
        User actual = uDAO.fetchUser("Robot","IROBOT");
        assertEquals("Jones",actual.getLastName());

    }



    /***
     * Test that database rejects null users.
     *
     * @throws DataAccessException - Ignore
     */
    @Test
    void insertUserFail() throws DataAccessException {
        User actual = new User("Darth",null,"Im your father","Skywalker",
                "Darth","Vader","M");
        assertThrows(DataAccessException.class, () ->{uDAO.insertUser(actual);});
    }



    /***
     * Tried to find username, but table was cleared.
     *
     * @throws DataAccessException - Ignore
     */
    @Test
    void clear() throws DataAccessException {
        uDAO.clear();
        assertNull(uDAO.fetchUser("username","password"));
    }
}