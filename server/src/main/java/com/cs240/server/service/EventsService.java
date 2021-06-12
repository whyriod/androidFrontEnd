package com.cs240.server.service;

import dao.*;
import model.Event;
import model.Person;
import request.EventsRequest;
import result.EventsResult;
import service.shared.Relatives;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Set;

/***
 * Uses a recursive function to get all persons who are related to said person.
 */
public class EventsService {

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
     * Description:
     * @param r
     * @return
     */
    public EventsResult getAllEvents (EventsRequest r){

        EventsResult result;

        try{
            try{
                //Setup and get relatives
                setUp();
                Person person = pDAO.fetchPerson(r.getPersonID());
                Relatives rel = new Relatives();
                ArrayList<Person> relatives = rel.getRelatives(person, pDAO);

                //Get events of the relatives
                ArrayList<Event> events = rel.getFamilyEvents(relatives, eDAO);
                result = new EventsResult(events,true);

                //Close Connection
                db.closeConnection(true);
            }
            //Event Failed
            catch (ClassNotFoundException | DataAccessException e) {
                //Rollback changes
                db.closeConnection(false);
                result = new EventsResult(("Error: " + e.getMessage()),false);
                e.printStackTrace();
            }
        }
        //Connection close failed
        catch(DataAccessException e){
            result = new EventsResult(("Error: " + e.getMessage()), false);
            e.printStackTrace();
        }
        return result;
    }
}
