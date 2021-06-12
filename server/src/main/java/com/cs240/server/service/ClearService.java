package com.cs240.server.service;

import dao.*;
import result.ClearResult;
import java.sql.Connection;

/***
 * Creates a database connection and DAO objects for each table.
 * Clears each table and report the result.
 */
public class ClearService {

    private Database db;
    private AuthTokenDAO aDAO;
    private EventDAO eDAO;
    private PersonDAO pDAO;
    private UserDAO uDAO;



    /***
     * Sets up Database Connection, and initialize needed DAO's.
     *
     * @throws DataAccessException - Database Connection errors
     */
    private void setUp() throws DataAccessException {

        db = new Database();
        Connection c = db.getConnection();

        aDAO = new AuthTokenDAO(c);
        eDAO = new EventDAO(c);
        pDAO = new PersonDAO(c);
        uDAO = new UserDAO(c);
    }



    /***
     * Description: Deletes ALL data from the database: user, authTokens,
     * persons, and events.
     *
     * @return clearResult object.
     */
    public ClearResult clearDatabase() {

        ClearResult clear;

        try{
            try{
                //Setup and clear tables
                setUp();
                aDAO.clear();
                eDAO.clear();
                pDAO.clear();
                uDAO.clear();

                //Close Connection
                db.closeConnection(true);
                clear = new ClearResult("Clear Succeeded.",true);
            }
            //Clear Failed
            catch (DataAccessException e) {
                //Rollback changes
                db.closeConnection(false);
                clear = new ClearResult(("Error: " + e.getMessage()),false);
                e.printStackTrace();
            }
        }
        //Connection close failed
        catch(DataAccessException e){
            clear = new ClearResult(("Error: " + e.getMessage()), false);
            e.printStackTrace();
        }
        return clear;
    }
}
