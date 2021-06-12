package com.cs240.server.service;

import dao.*;
import model.Person;
import request.PersonRequest;
import result.PersonResult;
import service.shared.Relatives;

import java.sql.Connection;
import java.util.ArrayList;

/***
 * Uses a recursive function to get all persons who are related to said person.
 * Then, if the user has rights to that person, it returns their data.
 */
public class PersonService {

    private Database db;
    private PersonDAO pDAO;



    /***
     * Sets up Database Connection, and initialize needed DAO's.
     *
     * @throws DataAccessException - Database Connection errors
     */
    private void setUp() throws DataAccessException, ClassNotFoundException {

        db = new Database();
        Connection c = db.getConnection();
        pDAO = new PersonDAO(c);
    }



    /***
     * Description: Returns the single Person object with the specified ID.
     * @param r - GetPersonRequest
     * @return result - GetPersonResult object
     */
    public PersonResult getPerson (PersonRequest r){

        PersonResult result;

        try{
            try{
                //Setup and get relatives
                setUp();
                Person person = pDAO.fetchPerson(r.getUserPersonID());
                Relatives rel = new Relatives();
                ArrayList<Person> relatives = rel.getRelatives(person, pDAO);
                Person wanted = pDAO.fetchPerson(r.getPersonID());

                //Relative
                if(rel.contains(relatives,wanted)){
                    result =
                            new PersonResult(wanted.getPersonID(),wanted.getAssociatedUsername(),
                                    wanted.getFirstName(), wanted.getLastName(),wanted.getGender(),
                                    wanted.getFatherID(),wanted.getMotherID(),wanted.getSpouseID(),
                            true);
                }
                //!Relative
                else{
                    result =
                            new PersonResult("Error: Unable to get person",false);
                }

                //Close Connection
                db.closeConnection(true);
            }
            //Person Failed
            catch (ClassNotFoundException | DataAccessException e) {
                //Rollback changes
                db.closeConnection(false);
                result = new PersonResult(("Error: " + e.getMessage()),false);
                e.printStackTrace();
            }
        }
        //Connection close failed
        catch(DataAccessException e){
            result = new PersonResult(("Error: " + e.getMessage()), false);
            e.printStackTrace();
        }
        return result;
    }
}
