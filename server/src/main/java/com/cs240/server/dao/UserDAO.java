package com.cs240.server.dao;

import model.User;
import java.sql.*;

/***
 * Performs the following SQL Statements on the User Table:
 * Create Table
 * Drop Table
 * Select (Single)
 * Insert (Single)
 * Delete (All)
 */
public class UserDAO {

    private final Connection c;



    /***
     * Constructor.
     *
     * @param c - Connection from Database class.
     */
    public UserDAO(Connection c)
    {
        this.c = c;
    }



    /***
     * Creates the User Table.
     *
     * @throws DataAccessException - Unable to create User Table: + e
     */
    public void createTable() throws DataAccessException {

        //UserTable
        String sqlU = "Create TABLE IF NOT EXISTS User (\n"+
                      "PersonID           varChar(20) UNIQUE NOT NULL,\n"+
                      "Username           varchar(20) PRIMARY KEY NOT NULL,\n"+
                      "Password           varchar(20) NOT NULL,\n"+
                      "Email              varchar(40) NOT NULL,\n"+
                      "FirstName          varchar(40) NOT NULL,\n"+
                      "LastName           varchar(40) NOT NULL,\n"+
                      "Gender             varchar(1) NOT NULL);";


        try (Statement stmt = c.createStatement()){
            //Create Table
            stmt.executeUpdate(sqlU);
        }
        //SQL Error
        catch (SQLException e) {
            throw new DataAccessException("Unable to create User Table: " + e);
        }
    }



    /***
     * For Testing Purposes, I need to make sure that the
     * User table is properly created. So dropping it is
     * useful.
     *
     * @throws DataAccessException - Unable to drop User table: + e
     */
    public void dropTable() throws DataAccessException{

        //Initialize and prepare statements.
        String sqlU = "Drop Table User";

        try (Statement stmt = c.createStatement()){
            //Execute
            stmt.executeUpdate(sqlU);
        }
        //SQL Error
        catch (SQLException e) {
            throw new DataAccessException("Unable to drop User table: " + e);
        }
    }



    /***
     * (Select)
     * Takes an user String, and a password string. Selects the user associated with
     * the username and password, and then returns both as an User object. Null is returned
     * If no object is found.
     *
     * @param username - The username to check for.
     * @param password - The password of the user.
     * @return token - The authtoken object for the row found.
     * @throws DataAccessException - Unable to find user: + e
     */
    public User fetchUser(String username, String password) throws DataAccessException{

        //Initialize and prepare statements.
        User user = null;
        ResultSet ur = null;
        String sql = "SELECT * FROM User WHERE Username = ? AND Password = ?;";

        //Execute the Query. If you find a row, set the Authtoken values.
        try (PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ur = stmt.executeQuery();

            //If a row is found
            if (ur.next()) {
                user = new User(
                        ur.getString("PersonID"), ur.getString("Username"),
                        ur.getString("Password"), ur.getString("Email"),
                        ur.getString("FirstName"), ur.getString("LastName"),
                        ur.getString("Gender")
                );
                return user;
            }
        }
        //SQL Error
        catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to find user: " + e);
        }
        //Close the resultSet
        finally {
            if(ur != null) {
                try {
                    ur.close();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }



    /***
     * (Select)
     * Takes an user String. Selects the user associated with
     * the username and then returns both as an User object. Null is returned
     * If no object is found.
     *
     * @param username - The username to check for.
     * @return token - The authtoken object for the row found.
     * @throws DataAccessException - Unable to find user: + e
     */
    public User fetchUser(String username) throws DataAccessException{

        //Initialize and prepare statements.
        User user = null;
        ResultSet ur = null;
        String sql = "SELECT * FROM User WHERE Username = ? ;";

        //Execute the Query. If you find a row, set the Authtoken values.
        try (PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setString(1, username);
            ur = stmt.executeQuery();

            //If a row is found
            if (ur.next()) {
                user = new User(
                        ur.getString("PersonID"), ur.getString("Username"),
                        ur.getString("Password"), ur.getString("Email"),
                        ur.getString("FirstName"), ur.getString("LastName"),
                        ur.getString("Gender")
                );
                return user;
            }
        }
        //SQL Error
        catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to find user: " + e);
        }
        //Close the resultSet
        finally {
            if(ur != null) {
                try {
                    ur.close();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }



    /***
     * (Insert)
     * Takes a user object. Inserts a new user.
     *
     * @param user - new user object to insert into the Database
     * @throws DataAccessException - Unable to create user: + e
     */
    public void insertUser(User user) throws DataAccessException{

        //Prepare Statements
        String sql = "INSERT INTO User (PersonID, Username, Password, Email, " +
                "FirstName, LastName, Gender) VALUES(?,?,?,?,?,?,?);";

        try (PreparedStatement stmt = c.prepareStatement(sql)) {
            //Set User values.
            stmt.setString(1, user.getPersonID());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getFirstName());
            stmt.setString(6, user.getLastName());
            stmt.setString(7, user.getGender());
            //Execute Query
            stmt.executeUpdate();
        }
        //SQL Error
        catch (SQLException e) {
            throw new DataAccessException("Unable to create user: " + e);
        }
    }



    /***
     * (Delete)
     * Clears all users from the User table.
     *
     * @throws DataAccessException - Unable to clear users: + e
     */
    public void clear() throws DataAccessException {
        //Prepare Statements
        String sql = "DELETE FROM User";

        try (Statement stmt = c.createStatement()){
            //Execute Query
            stmt.executeUpdate(sql);
        }
        //SQL Error
        catch (SQLException e) {
            throw new DataAccessException("Unable to clear users: " + e);
        }
    }
}
