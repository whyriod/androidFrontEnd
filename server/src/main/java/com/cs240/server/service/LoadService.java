package com.cs240.server.service;

import dao.*;
import model.Event;
import model.Person;
import model.User;
import request.LoadRequest;
import result.ClearResult;
import result.LoadResult;

import java.sql.Connection;

/***
 * Attempts to clear database data. Then attempts to load an
 * array of events, persons, and users into the database.
 */
public class LoadService {

    Database db;
    EventDAO eDAO;
    PersonDAO pDAO;
    UserDAO uDAO;



    /***
     * Sets up Database Connection, and initialize needed DAO's.
     *
     * @throws DataAccessException - Database Connection errors
     */
    private void setUp() throws DataAccessException, ClassNotFoundException {

        db = new Database();
        Connection c = db.getConnection();
        eDAO = new EventDAO(c);
        pDAO = new PersonDAO(c);
        uDAO = new UserDAO(c);
    }



    /***
     * Description: Clears all data from the database (ClearService), and then
     * loads the posted user, person, and event data into the database.
     * @param r loadRequest Object
     * @return loadResult Objest
     */
    public LoadResult loadDatabase(LoadRequest r){

        LoadResult load;

        try {
            try {
                //Clear Database
                ClearService cService = new ClearService();
                ClearResult cResult = cService.clearDatabase();

                //Database Cleared
                if(cResult.isSuccess()){
                    setUp();
                    Event[] events = r.getEvents();
                    Person[] persons = r.getPersons();
                    User[] users = r.getUsers();
                    int eventCount = 0;
                    int personCount = 0;
                    int userCount = 0;

                    //Load events
                    for (int i = 0; i < events.length; i++) {
                        eDAO.insertEvent(events[i]);
                        eventCount++;
                    }

                    //Load persons
                    for (int i = 0; i < persons.length; i++) {
                        pDAO.insertPerson(persons[i]);
                        personCount++;
                    }

                    //Load users
                    for (int i = 0; i < users.length; i++) {
                        uDAO.insertUser(users[i]);
                        userCount++;
                    }

                    //Close Connection
                    db.closeConnection(true);

                    //Create result object
                    load = new LoadResult("Successfully added " +
                            userCount + " users, " +
                            personCount + " persons, and " +
                            eventCount + " events to the database.", true);
                }
                //!Database Cleared
                else{
                    load = new LoadResult("Error: Could not clear database",false);
                }
            }
            //Load Failed
            catch (DataAccessException | ClassNotFoundException e) {
                //Rollback
                db.closeConnection(false);
                load = new LoadResult(("Error: " + e.getMessage()), false);
                e.printStackTrace();
            }
        }
        //Connection close failed
        catch(DataAccessException e){
            load = new LoadResult(("Error: " + e.getMessage()), false);
            e.printStackTrace();
        }
        return load;
    }
}
