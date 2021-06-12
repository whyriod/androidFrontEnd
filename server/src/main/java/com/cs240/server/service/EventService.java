package com.cs240.server.service;

import dao.*;
import model.Event;
import model.Person;
import request.EventRequest;
import result.EventResult;
import service.shared.Relatives;

import java.sql.Connection;
import java.util.ArrayList;

/***
 * Uses a recursive function to get all persons who are related to said person.
 * Then, if the user has rights to that person, it returns their data.
 */
public class EventService {

    private Database db;
    private PersonDAO pDAO;
    private EventDAO eDAO;



    /***
     * Sets up Database Connection, and initialize needed DAO's.
     *
     * @throws DataAccessException - Database Connection errors
     */
    private void setUp() throws DataAccessException, ClassNotFoundException {

        db = new Database();
        Connection c = db.getConnection();
        pDAO = new PersonDAO(c);
        eDAO = new EventDAO(c);
    }



    /***
     * Description: Returns the single Event object with the specified ID.
     * @param r getAllEventsRequest
     * @return getAllEventsResult object
     */
    public EventResult getEvent (EventRequest r){

        EventResult result;

        try{
            try{
                //Setup and get relatives
                setUp();
                Person person = pDAO.fetchPerson(r.getUserPersonID());
                Relatives rel = new Relatives();
                ArrayList<Person> relatives = rel.getRelatives(person, pDAO);

                //See if the wanted person is a relative
                Event wanted = eDAO.fetchEvent(r.getEventID());
                Person wantedPerson = pDAO.fetchPerson(wanted.getPersonID());

                if(rel.contains(relatives,wantedPerson)){
                    result =
                            new EventResult(wanted.getEventID(),wanted.getEventType(),wanted.getPersonID(),
                                    wanted.getAssociatedUsername(), wanted.getYear(),wanted.getCountry(),
                                    wanted.getCity(),wanted.getLatitude(),wanted.getLongitude(),true);
                }
                else{
                    result =
                            new EventResult("Error: Unable to get event",false);
                }

                //Close Connection
                db.closeConnection(true);
            }
            //Event Failed
            catch (ClassNotFoundException | DataAccessException e) {
                //Rollback changes
                db.closeConnection(false);
                result = new EventResult(("Error: " + e.getMessage()),false);
                e.printStackTrace();
            }
        }
        //Connection close failed
        catch(DataAccessException e){
            result = new EventResult(("Error: " + e.getMessage()), false);
            e.printStackTrace();
        }
        return result;
    }
}
