package com.cs240.server.dao;

import model.Authtoken;
import java.sql.*;

/***
 * Performs the following SQL Statements on the Authtoken Table:
 * Create Table
 * Drop Table
 * Select (Single)
 * Insert (Single)
 * Delete (Associated User)
 * Delete (All)
 */
public class AuthTokenDAO {

    private final Connection c;



    /***
     * Constructor.
     *
     * @param c - Connection from Database class.
     */
    public AuthTokenDAO(Connection c)
    {
        this.c = c;
    }



    /***
     * Creates the Authtoken Table.
     *
     * @throws DataAccessException - Unable to create Authtoken Table: + e.
     */
    public void createTable() throws DataAccessException {

        //Authtoken Table
        String sqlA = "Create Table IF NOT EXISTS Authtoken(\n" +
                      "AuthToken              varchar(20) PRIMARY KEY NOT NULL,\n" +
                      "AssociatedUsername     varchar(20) NOT NULL);";

        try (Statement stmt = c.createStatement()){
            //Create Table
            stmt.executeUpdate(sqlA);
        }
        //SQL Error
        catch (SQLException e) {
            throw new DataAccessException("Unable to create Authtoken Table: " + e);
        }
    }



    /***
     * For Testing Purposes, I need to make sure that the
     * Authtoken table is properly created. So dropping it is
     * useful.
     *
     * @throws DataAccessException - Unable to drop Authtoken table: + e.
     */
    public void dropTable() throws DataAccessException{

        //Initialize and prepare statements.
        String sqlA = "Drop Table Authtoken";

        try (Statement stmt = c.createStatement()){
            //Execute
            stmt.executeUpdate(sqlA);
        }
        //SQL Error
        catch (SQLException e) {
            throw new DataAccessException("Unable to drop Authtoken table: " + e);
        }
    }



    /***
     * (Select)
     * Takes an authtoken String. Selects the username associated with the token,
     * and then returns both as an authtoken object. Returns null if no row found.
     *
     * @param authtoken - The authtoken to check for.
     * @return token - The authtoken object for the row found.
     * @throws DataAccessException - Unable to find token: + e.
     */
    public Authtoken fetchToken(String authtoken) throws DataAccessException {

        //Initialize and prepare statements.
        Authtoken token;
        ResultSet ur = null;
        String sql = "SELECT * FROM Authtoken WHERE Authtoken = ?;";

        //Execute the Query. If you find a row, set the Authtoken values.
        try (PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setString(1, authtoken);
            ur = stmt.executeQuery();

            //If a row is found
            if (ur.next()){
                token = new Authtoken(
                        ur.getString("AuthToken"), ur.getString("AssociatedUsername")
                );
                return token;
            }
        }
        //SQL Error
        catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to find token: " + e);
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
     * Takes a username String. Inserts a new authtoken value for the user.
     *
     * @param token - Authtoken to put into database
     * @throws DataAccessException - Unable to create token: + e.
     */
    public void insertToken(Authtoken token) throws DataAccessException {

        //Prepare Statements
        String sql = "INSERT INTO Authtoken (Authtoken, AssociatedUsername) VALUES(?,?);";

        try (PreparedStatement stmt = c.prepareStatement(sql)) {
            //Set Username and token (A random UUID)
            stmt.setString(1, token.getauthtoken());
            stmt.setString(2, token.getusername());
            //Execute Query
            stmt.executeUpdate();
        }
        //SQL Error
        catch (SQLException e) {
            throw new DataAccessException("Unable to create token: " + e);
        }
    }

    /***
     * (Delete)
     * Takes a username. Deletes tokens who have that associated username.
     *
     * @param asscoiatedUsername - username of token to delete.
     * @throws DataAccessException - Unable to delete token: + e.
     */
    public void deleteToken(String asscoiatedUsername) throws DataAccessException {

        //Prepare Statements
        String sql = "DELETE FROM Authtoken WHERE AssociatedUsername = ?;";

        try (PreparedStatement stmt = c.prepareStatement(sql)) {
            //Set Person values.
            stmt.setString(1, asscoiatedUsername);

            //Execute Query
            stmt.executeUpdate();
        }
        //SQL Error
        catch (SQLException e) {
            throw new DataAccessException("Unable to delete token: " + e);
        }
    }



    /***
     * (Delete)
     * Clears all Authtokens from the Authtoken table.
     *
     * @throws DataAccessException - Unable to clear tokens: + e.
     */
    public void clear() throws DataAccessException {

        //Prepare Statements
        String sql = "DELETE FROM Authtoken";

        try (Statement stmt = c.createStatement()){
            //Execute Query
            stmt.executeUpdate(sql);
        }
        //SQL Error
        catch (SQLException e) {
            throw new DataAccessException("Unable to clear tokens: " + e);
        }
    }
}
