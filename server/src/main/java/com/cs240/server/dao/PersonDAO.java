package com.cs240.server.dao;

import model.Person;
import java.sql.*;

/***
 * Performs the following SQL Statements on the Person Table:
 * Create Table
 * Drop Table
 * Select (Single)
 * Insert (Single)
 * Delete (Associated User)
 * Delete (All)
 */
public class PersonDAO {

    private final Connection c;



    /***
     * Constructor.
     *
     * @param c - Connection from Database class.
     */
    public PersonDAO(Connection c)
    {
        this.c = c;
    }



    /***
     * Creates the Person Table.
     *
     * @throws DataAccessException - Unable to create Person Table: + e
     */
    public void createTable() throws DataAccessException {

        //Person Table
        String sqlP =
                "Create Table IF NOT EXISTS Person(\n" +
                        "PersonID             varChar(20) PRIMARY KEY NOT NULL,\n" +
                        "AssociatedUsername   varchar(20),\n" +
                        "FirstName            varchar(40) NOT NULL,\n" +
                        "LastName             varChar(40) NOT NULL,\n" +
                        "Gender               varchar(1) NOT NULL,\n" +
                        "FatherID             varChar(20),\n" +
                        "MotherID             varChar(20),\n" +
                        "SpouseID             varChar(20));";

        try (Statement stmt = c.createStatement()){
            //Create Table
            stmt.executeUpdate(sqlP);
        }
        //SQL Error
        catch (SQLException e) {
            throw new DataAccessException("Unable to create Person Table: " + e);
        }
    }



    /***
     * For Testing Purposes, I need to make sure that the
     * Person table is properly created. So dropping it is
     * useful.
     *
     * @throws DataAccessException - Unable to drop person table: + e
     */
    public void dropTable() throws DataAccessException{

        //Initialize and prepare statements.
        String sqlP = "Drop Table Person";

        try (Statement stmt = c.createStatement()){
            //Execute
            stmt.executeUpdate(sqlP);
        }
        //SQL Error
        catch (SQLException e) {
            throw new DataAccessException("Unable to drop Person table: " + e);
        }
    }


    /***
     * (Select)
     * Takes an personID String. Selects the person associated with ID, and then
     * returns the Person. Null is returned If no person is found.
     *
     * @param personID - The personID to check for.
     * @return person - The person object for the row found.
     * @throws DataAccessException - Unable to find person: + e
     */
    public Person fetchPerson(String personID) throws DataAccessException {

        //Initialize and prepare statements.
        Person person;
        ResultSet pr = null;
        String sql = "SELECT * FROM Person WHERE PersonID = ?;";

        //Execute the Query. If you find a row, set the person values.
        try (PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setString(1, personID);
            pr = stmt.executeQuery();

            //If a row is found
            if (pr.next()) {
                person = new Person(pr.getString("PersonID"), pr.getString("AssociatedUsername"),
                        pr.getString("FirstName"), pr.getString("LastName"),
                        pr.getString("Gender"), pr.getString("FatherID"),
                        pr.getString("MotherID"), pr.getString("SpouseID"));
                return person;
            }
        }
        //SQL Error
        catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to find person: " + e);
        }
        //Close the resultSet
        finally {
            if(pr != null) {
                try {
                    pr.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }



    /***
     * (Insert)
     * Takes a person object. Inserts a new person.
     *
     * @param person - new person object to insert into the Database
     * @throws DataAccessException - Unable to create person: + e
     */
    public void insertPerson(Person person) throws DataAccessException {

        //Prepare Statements
        String sql = "INSERT INTO Person (PersonID, AssociatedUsername, FirstName, LastName, " +
                "Gender, FatherID, MotherID, SpouseID) VALUES(?,?,?,?,?,?,?,?);";

        try (PreparedStatement stmt = c.prepareStatement(sql)) {
            //Set Person values.
            stmt.setString(1, person.getPersonID());
            stmt.setString(2, person.getAssociatedUsername());
            stmt.setString(3, person.getFirstName());
            stmt.setString(4, person.getLastName());
            stmt.setString(5, person.getGender());
            stmt.setString(6, person.getFatherID());
            stmt.setString(7, person.getMotherID());
            stmt.setString(8, person.getSpouseID());
            //Execute Query
            stmt.executeUpdate();
        }
        //SQL Error
        catch (SQLException e) {
            throw new DataAccessException("Unable to create person: " + e);
        }
    }




    /***
     * (Update)
     *
     * @param person - new person object to insert into the Database
     * @throws DataAccessException - Unable to create person: + e
     */
    public void updateParents(String personID, String father, String mother) throws DataAccessException {

        //Prepare Statements
        String sql = "Update Person SET FatherID = ?, MotherID = ? " +
                "Where personID = ?";

        try (PreparedStatement stmt = c.prepareStatement(sql)) {
            //Set Person values.
            stmt.setString(1, father);
            stmt.setString(2, mother);
            stmt.setString(3, personID);

            //Execute Query
            stmt.executeUpdate();
        }
        //SQL Error
        catch (SQLException e) {
            throw new DataAccessException("Unable to update person: " + e);
        }
    }



    /***
     * (Delete)
     * Takes a username. Deletes persons who have that associated username.
     *
     * @param asscoiatedUsername - username of person to delete.
     * @throws DataAccessException - Unable to delete person: + e
     */
    public void deletePerson(String asscoiatedUsername) throws DataAccessException {

        //Prepare Statements
        String sql = "DELETE FROM Person WHERE AssociatedUsername = ?;";

        try (PreparedStatement stmt = c.prepareStatement(sql)) {
            //Set Person values.
            stmt.setString(1, asscoiatedUsername);

            //Execute Query
            stmt.executeUpdate();
        }
        //SQL Error
        catch (SQLException e) {
            throw new DataAccessException("Unable to delete person: " + e);
        }
    }



    /***
     * (Delete)
     * Clears all persons from the Person table.
     *
     * @throws DataAccessException - Unable to clear persons: + e
     */
    public void clear() throws DataAccessException{
        //Prepare Statements
        String sql = "DELETE FROM Person";

        try (Statement stmt = c.createStatement()){
            //Execute Query
            stmt.executeUpdate(sql);
        }
        //SQL Error
        catch (SQLException e) {
            throw new DataAccessException("Unable to clear persons: " + e);
        }
    }
}
