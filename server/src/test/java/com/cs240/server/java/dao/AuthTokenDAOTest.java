package com.cs240.server.java.dao;

import model.Authtoken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

/***
 * Tests all public AuthTokenDAO functions for a pass and a fail, other than
 * clear and delete table.
 */
class AuthTokenDAOTest {

    private Database db;
    private AuthTokenDAO aDAO;



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

        //Set up blank tables
        aDAO.createTable();
        aDAO.clear();

        //Test data
        Authtoken token1 = new Authtoken("1111","Bob");
        Authtoken token2 = new Authtoken("2222","John");
        Authtoken token3 = new Authtoken("3333","Bob");

        //Insert test data
        aDAO.insertToken(token1);
        aDAO.insertToken(token2);
        aDAO.insertToken(token3);

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
     * Moses should have been loaded into the new table.
     *
     * @throws DataAccessException - Tried to find Moses and did
     */
    @Test
    void createTablePass() throws DataAccessException {
        aDAO.dropTable();
        aDAO.createTable();
        Authtoken token1 = new Authtoken("1","Moses");
        aDAO.insertToken(token1);
        assertEquals(aDAO.fetchToken("1").getusername(), "Moses");
    }



    /***
     * Test that a new table has been created.
     * Moses should not be in the new table.
     *
     * @throws DataAccessException - Tried to find Moses and didnt
     */
    @Test
    void createTablefail() throws DataAccessException {
        aDAO.dropTable();
        aDAO.createTable();
        assertNull(aDAO.fetchToken("Moses"));
    }



    /***
     * Test that Authtoken 1111 is registered to Bob.
     *
     * @throws DataAccessException - Ignore
     */
    @Test
    void fetchTokenPass() throws DataAccessException {
        Authtoken actual = aDAO.fetchToken("1111");
        assertEquals("Bob",actual.getusername());
    }



    /***
     * Test that Authtoken 4444 is registered to not in database.
     *
     * @throws DataAccessException - Ignore
     */
    @Test
    void fetchTokenFail() throws DataAccessException {
        Authtoken actual = aDAO.fetchToken("4444");
        assertNull(actual);
    }



    /***
     * Test that a new token for Caleb was inserted into the Database.
     *
     * @throws DataAccessException - Ignore
     */
    @Test
    void insertTokenPass() throws DataAccessException {
        Authtoken newToken = new Authtoken("4444","Caleb");
        aDAO.insertToken(newToken);
        Authtoken actual = aDAO.fetchToken("4444");
        assertEquals("Caleb", actual.getusername());
    }



    /***
     * Test that the database dies when a null token is inserted.
     *
     * @throws DataAccessException - Ignore
     */
    @Test
    void insertTokenFail() throws DataAccessException {
        Authtoken actual = new Authtoken(null,"Caleb");
        assertThrows(DataAccessException.class, () ->{aDAO.insertToken(actual);});
    }



    /***
     * Deletes tokens associated with Bob
     * (I only have 1 test, as it does, or it doesnt work)
     */
    @Test
    void deleteTokenPass() throws DataAccessException {
        aDAO.deleteToken("Bob");
        assertNull(aDAO.fetchToken("1111"));
        assertNull(aDAO.fetchToken("3333"));
    }



    /***
     * Deletes tokens associated with John. Checks to make sure
     * that Bobs were not deleted along with Johns.
     */
    @Test
    void deleteTokenPersist() throws DataAccessException {
        aDAO.deleteToken("John");
        assertEquals("Bob",aDAO.fetchToken("1111").getusername());
        assertEquals("Bob",aDAO.fetchToken("3333").getusername());
    }



    /***
     * Test that the database returns a null when looking for
     * Bobs token "3333" after clearning the database.
     *
     * @throws DataAccessException
     */
    @Test
    void clear() throws DataAccessException {
        aDAO.clear();
        assertNull(aDAO.fetchToken("3333"));
    }
}