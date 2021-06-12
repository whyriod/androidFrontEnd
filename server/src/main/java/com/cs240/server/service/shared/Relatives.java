package com.cs240.server.service.shared;

import dao.*;
import model.Event;
import model.Person;

import java.util.ArrayList;

/***
 * A group of shared functions that relate to personServices and eventServices
 * dealing with relatives.
 */
public class Relatives {



    /***
     * Grabs all related persons above you in the family tree by calling
     * getRelativesHelper.
     *
     * @param person - The current person object
     * @return relatives - A set of all relatives.
     * @throws DataAccessException - Database get errors
     */
    public ArrayList<Person> getRelatives(Person person, PersonDAO pDAO) throws DataAccessException {
        ArrayList<Person> relatives = new ArrayList<>();
        Relatives service = new Relatives();
        service.getRelativesHelper(relatives, person, pDAO);
        return relatives;
    }



    /***
     * Grabs all related persons above you in the family tree.
     *
     * @param relatives - A set of relatives to add found relatives to
     * @param person - The current person object
     *
     * @throws DataAccessException - Database get errors
     */
    private void getRelativesHelper(ArrayList<Person> relatives, Person person, PersonDAO pDAO) throws DataAccessException {

        //Check to make sure that the person exists.
        if(person != null && !contains(relatives,person)){
            relatives.add(person);
            Person father = pDAO.fetchPerson(person.getFatherID());
            Person mother = pDAO.fetchPerson(person.getMotherID());
            Person spouse = pDAO.fetchPerson(person.getSpouseID());

            //Check for other relatives.
            if(father != null){
                getRelativesHelper(relatives, father, pDAO);
            }
            if(mother != null){
                getRelativesHelper(relatives, mother, pDAO);
            }
            if(spouse != null){
                getRelativesHelper(relatives, spouse, pDAO);
            }
        }
    }



    /***
     * Checks to see if a set of persons contains a specific person. If
     * it does, true. Otherwise false.
     * @param relatives - The set of relatives person objects
     * @param person - The person whe are checking as a relative
     * @return - True for related, false for not.
     */
    public boolean contains(ArrayList<Person> relatives, Person person){

        //If they are the same pointer, true
        for(Person p: relatives){
            if(p.equals(person)){
                return true;
            }
        }
        //Otherwise false
        return false;
    }



    /***
     * Loops through a set of relatives. Grabs all events for each relative, and adds them to
     * an a new set and returns.
     * @param relatives - Relatives to get events for.
     * @return - A set of all events for a family.
     * @throws DataAccessException - Throws if a DB issue. Caught in Service classes.
     */
    public ArrayList<Event> getFamilyEvents(ArrayList<Person> relatives, EventDAO eDAO) throws DataAccessException {

        ArrayList<Event> familyEvents = new ArrayList<>();
        ArrayList<Event> personalEvents = new ArrayList<>();

        for(Person p: relatives){
            personalEvents.clear();
            personalEvents = eDAO.fetchEvents(p.getPersonID());
            for(Event e: personalEvents){
                familyEvents.add(e);
            }
        }
        return familyEvents;
    }
}
