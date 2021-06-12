package com.cs240.server.java.dao;

import model.Authtoken;
import model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

/***
 * Tests all public PersonDAO functions for a pass and a fail, other than
 * clear and delete table.
 */
class PersonDAOTest {

    private Database db;
    private PersonDAO pDAO;



    /***
     * Load Test data for Person tests
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

        //Set up blank tables
        pDAO.createTable();
        pDAO.clear();

        //Test data
        Person person1 = new Person("Bob","Bob","Bob","Robert",
                "M",null,null,null);
        Person person2 = new Person("John","Bob","John","Johnson",
                "M",null,null,null);
        Person person3 = new Person("Liz","Liz","Liz","Elizabeth",
                "F",null,null,null);

        //Insert test data
        pDAO.insertPerson(person1);
        pDAO.insertPerson(person2);
        pDAO.insertPerson(person3);

        //Close connection - Commit
        db.closeConnection(true);
    }


    /***
     * Creates a new connection to the database and initializes pDAO
     *
     * @throws DataAccessException - If this fails, they all fail
     * @throws ClassNotFoundException - If this fails, they all fail
     */
    @BeforeEach
    public void setUp() throws DataAccessException, ClassNotFoundException {
        db = new Database();
        Connection c = db.getConnection();
        pDAO = new PersonDAO(c);
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
     * Nephi should have been loaded into the new table.
     *
     * @throws DataAccessException - Tried to find Moses and did
     */
    @Test
    void createTablePass() throws DataAccessException {
        pDAO.dropTable();
        pDAO.createTable();
        Person person = new Person("Nephi","Nephi","Nephi","Nephihah",
                "M",null,null,null);
        pDAO.insertPerson(person);
        assertEquals(pDAO.fetchPerson("Nephi").getGender(), "M");
    }



    /***
     * Test that a new table has been created.
     * Nephi should not be in the new table.
     *
     * @throws DataAccessException - Tried to find Nephi and didnt
     */
    @Test
    void createTablefail() throws DataAccessException {
        pDAO.dropTable();
        pDAO.createTable();
        assertNull(pDAO.fetchPerson("Nephi"));
    }



    /***
     * Test that Liz is in the database
     *
     * @throws DataAccessException - Ignore
     */
    @Test
    void fetchPass() throws DataAccessException {
        Person actual = pDAO.fetchPerson("Liz");
        assertEquals("F",actual.getGender());
    }



    /***
     * Test that la- is not in the database.
     *
     * @throws DataAccessException - Ignore
     */
    @Test
    void fetchFail() throws DataAccessException {
        Person actual = pDAO.fetchPerson("la-");
        assertNull(actual);
    }


    /***
     * Test that Luke was inserted into the database.
     *
     * @throws DataAccessException - Ignore
     */
    @Test
    void insertPersonPass() throws DataAccessException {
        Person person = new Person("Luke","Luke","Luke","Skywalker",
                "M",null,null,null);
        pDAO.insertPerson(person);
        Person actual = pDAO.fetchPerson("Luke");
        assertEquals("Skywalker",actual.getLastName());
    }


    /***
     * Test that database rejects null personIDs.
     *
     * @throws DataAccessException - Ignore
     */
    @Test
    void insertPersonFail() throws DataAccessException {
        Person actual = new Person(null,"Luke","Luke","Skywalker",
                "M",null,null,null);
        assertThrows(DataAccessException.class, () ->{pDAO.insertPerson(actual);});
    }


    /***
     * Deletes persons associated with Bob
     */
    @Test
    void deletePerson() throws DataAccessException {
        pDAO.deletePerson("Bob");
        assertNull(pDAO.fetchPerson("Bob"));
        assertNull(pDAO.fetchPerson("John"));
    }



    /***
     * Deletes persons associated with Bob. Checks to make sure
     * that Elizabeth's were not deleted along with Bobs.
     */
    @Test
    void deletePersonPersist() throws DataAccessException {
        pDAO.deletePerson("Bob");
        assertEquals("Elizabeth",pDAO.fetchPerson("Liz").getLastName());
    }



    /***
     * Updates the mother and father of Bob and checks that it worked.
     * (I only have 1 test, as it works or doesnt. If you try to update
     * someone that doesnt exist, no error is throw, and null values are accepted)
     */
    @Test
    void updatePerson() throws DataAccessException {
        pDAO.updateParents("Bob","John","Liz");
        assertEquals("John",pDAO.fetchPerson("Bob").getFatherID());
        assertEquals("Liz",pDAO.fetchPerson("Bob").getMotherID());
    }



    /***
     * Updates the mother and father of Bob and checks that it worked.
     * Checks to make sure that Johns parents have not been updated
     */
    @Test
    void updatePersonPersist() throws DataAccessException {
        pDAO.updateParents("Bob","John","Liz");
        assertNull(pDAO.fetchPerson("John").getFatherID());
        assertNull(pDAO.fetchPerson("John").getMotherID());
    }



    /***
     * Tried to find Bob, but table was cleared.
     *
     * @throws DataAccessException - Ignore
     */
    @Test
    void clear() throws DataAccessException {
        pDAO.clear();
        assertNull(pDAO.fetchPerson("Bob"));
    }
}