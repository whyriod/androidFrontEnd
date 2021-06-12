package com.cs240.server.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/***
 * This is from the class notes:
 * - DAOJunit5Example
 * (Along with some of my own additions)
 */
public class Database {

    private Connection c;



    /***
     * Opens a new connection. Should only be called by getConnection.
     *
     * @return c - The new connection
     * @throws DataAccessException - Unable to open database connection: + e
     */
    public Connection openConnection() throws DataAccessException {
        try{
            //The Structure for this Connection is driver:language:path
            //The path assumes you start in the root of your project unless given a non-relative path
            final String CONNECTION_URL = "jdbc:sqlite:FamilyMap.sqlite";
            c = DriverManager.getConnection(CONNECTION_URL);

            // Start a transaction
            c.setAutoCommit(false);
        }
        //SQL Error
        catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to open database connection: " + e);
        }
        return c;
    }



    /***
     * If there is no open connection, create a new one. Otherwise return
     * the open connection.
     *
     * @return - A new connection, or the current connection.
     * @throws DataAccessException - From getConnection
     */
    public Connection getConnection() throws DataAccessException {

        //No connection
        if(c == null) {
            return openConnection();
        }
        //Existing connection.
        else {
            return c;
        }
    }



    /***
     * Close the connection or you will die.
     *
     * @param commit - Keep the changes or not.
     * @throws DataAccessException - Unable to close database connection: + e
     */
    public void closeConnection(boolean commit) throws DataAccessException {
        try {
            //Keep
            if (commit) {
                c.commit();
            }
            //Dont Keep
            else {
                c.rollback();
            }
            //Close the connection and set the global connection to null (Its closed)
            c.close();
            c = null;
        }
        //SQL Error
        catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to close database connection: " + e);
        }
    }
}
