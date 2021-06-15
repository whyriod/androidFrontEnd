package com.cs240.famillymap.model;

import android.provider.Contacts;
import android.provider.ContactsContract;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Event;
import model.Person;

import static org.junit.jupiter.api.Assertions.*;

class HelperTest {

    private Helper helper;
    private DataCache cache;
    private Map<String, Person> peopleMap;
    private Map<String, Event> eventsMap;
    private Map<String,List<Event>> events;
    private ArrayList<Person> testPeople;
    private ArrayList<Event> testEvents;
    private Map<String, Event> filterEventsMap;
    private Map<String, Person> filterPerson;
    private ArrayList<Event> feederEvents;
    private Map<String,List<Event>> filterEvents;
    private Map<String, Person> paternal;
    private Map<String, Person> maternal;
    private Map<String, Person> male;
    private Map<String, Person> female;



    @BeforeEach
    private void setup(){
        helper = new Helper();
        cache = DataCache.getInstance();

        //Make Test People
        makeTestPeople();
        makeTestEvents();
        makeEvents();
        cacheSetup();
    }


    /***
     * For settings Filter
     */
    @Test
    void applyFilterPass() {
        cache.setPersonID("LilBob");
        //Nothing was changed.
        helper.applyFilter();
        assertEquals(8,cache.getFilterEventMap().size());

        //Test filter out Father side
        cache.setFathersSide(false);
        cache.setMothersSide(true);
        helper.applyFilter();
        assertEquals(2,cache.getFilterEventMap().size());

        //Test filter out mother side.
        cache.setFathersSide(true);
        cache.setMothersSide(false);
        helper.applyFilter();
        assertEquals(7,cache.getFilterEventMap().size());

        cache.setMothersSide(true);

        //Test filter out Male side
        cache.setMaleEvents(false);
        cache.setFemaleEvents(true);
        helper.applyFilter();
        assertEquals(2,cache.getFilterEventMap().size());

        //Test filter out Female side.
        cache.setMaleEvents(true);
        cache.setFemaleEvents(false);
        helper.applyFilter();
        assertEquals(6,cache.getFilterEventMap().size());
    }

    /***
     * Should die when the cache is missing important data.
     */
    @Test
    void applyFilterFail() {
        cache.setPersonID("LilBob");
        //Primary Event data
        cache.setEventsMap(null);
        assertThrows(Exception.class, () ->{helper.applyFilter();});
        cache.setEventsMap(eventsMap);

        //Primary People data
        cache.setPeopleMap(null);
        assertThrows(Exception.class, () ->{helper.applyFilter();});
        cache.setPeopleMap(peopleMap);

        //Primary People lists
        cache.setEvents(null);
        assertThrows(Exception.class, () ->{helper.applyFilter();});
        cache.setEvents(events);

    }


    /***
     * I have made some test data that is out of order. After a call to sortYear,
     * they should be in this order. I use a second array because sortYear destroys
     * the old array and returns a new one.
     */
    @Test
    void sortYearPass() {
        ArrayList <Event> events = helper.sortYear(feederEvents);

        assertEquals(8,events.size());
        assertEquals(events.get(0).getEventID(),testEvents.get(0).getEventID());
        assertEquals(events.get(1).getEventID(),testEvents.get(7).getEventID());
        assertEquals(events.get(2).getEventID(),testEvents.get(5).getEventID());
        assertEquals(events.get(3).getEventID(),testEvents.get(4).getEventID());

        assertEquals(events.get(4).getEventID(),testEvents.get(3).getEventID());
        assertEquals(events.get(5).getEventID(),testEvents.get(2).getEventID());
        assertEquals(events.get(6).getEventID(),testEvents.get(1).getEventID());
        assertEquals(events.get(7).getEventID(),testEvents.get(6).getEventID());
    }



    /***
     * Sort Year should return nothing if I pass in nothing. Not sure how else this could fai...
     */
    @Test
    void sortYearFail() {
        ArrayList<Event> events = helper.sortYear(null);
        ArrayList<Event> empty = new ArrayList<>();
        assertEquals(empty, events);
    }


    /***
     * For Search Filter. Checks to see that only Bob, his son, and father show up.
     * Also checks the events to make sure that there are 6, and that they are only from
     * Bob, his son, and father.
     */
    @Test
    void filterPass() {
        helper.filter("bob", feederEvents, testPeople);
        boolean bPerson = false;
        boolean lbPerson = false;
        boolean jPerson = false;
        boolean otherPerson = false;

        for(Person p: testPeople){
            if(p.getPersonID().equals("Bob")){
                bPerson = true;
            }
            else if(p.getPersonID().equals("LilBob")){
                lbPerson = true;
            }
            else if(p.getPersonID().equals("John")){
                jPerson = true;
            }
            else{
                otherPerson = true;
            }
        }

        //Check that Bob, John, and LilBob all made it.
        assertTrue(bPerson);
        assertTrue(lbPerson);
        assertTrue(jPerson);
        assertFalse(otherPerson);

        boolean bEvent = false;
        boolean lbEvent = false;
        boolean jEvent = false;
        boolean otherEvent = false;

        for(Event e: feederEvents){
            if(e.getPersonID().equals("Bob")){
                bPerson = true;
            }
            else if(e.getPersonID().equals("LilBob")){
                lbPerson = true;
            }
            else if(e.getPersonID().equals("John")){
                jPerson = true;
            }
            else{
                otherEvent = true;
            }
        }

        //Check that only Bob, John, and LilBob's events made it.
        assertEquals(6,feederEvents.size());
        assertTrue(bPerson);
        assertTrue(lbPerson);
        assertTrue(jPerson);
        assertFalse(otherEvent);
    }



    /***
     * If I pass the function a null, it should die.
     */
    @Test
    void filterFail() {
        assertThrows(Exception.class, () ->{helper.filter("bob", null, null);});
    }



    /***
     * Checks a small family to make sure that Bob is related correctly to each of them.
     */
    @Test
    void familyRelationshipPass() {
        makeTestPeople();
        //Bob vs John = Father
        assertEquals("Father",helper.familyRelationship(testPeople.get(0),"John"));
        //Bob vs Mary = Mother
        assertEquals("Mother",helper.familyRelationship(testPeople.get(0),"Mary"));
        //Bob vs Bertha = Spouse
        assertEquals("Spouse",helper.familyRelationship(testPeople.get(0),"Bertha"));
        //Bob vs LilBob = Child
        assertEquals("Child",helper.familyRelationship(testPeople.get(0),"LilBob"));
    }



    /***
     * Tests for a null pointer exception with a null person, and for ERROR in the case of
     * no personID to search for.
     */
    @Test
    void familyRelationshipFail() {
        makeTestPeople();
        Person fakePerson = null;
        //NullPointer exception as we are trying to access an attribute of fakePerson.
        assertThrows(Exception.class, () ->{helper.familyRelationship(fakePerson,null);});
        //Although its not going to throw anything, a Nice error message is nice to see.
        assertEquals("ERROR",helper.familyRelationship(testPeople.get(0),null));
    }



    /***
     * Makes a small family of Bobs for me to use in my tests.
     */
    void makeTestPeople(){
        Person bob = new Person("Bob","Bob","Bob",
                "Bob","m","John","Mary","Bertha");

        Person john = new Person("John","John","John",
                "Bob","m",null,null,"Mary");

        Person mary = new Person("Mary","Mary","Mary",
                "Mary","f",null,null,"John");

        Person bertha = new Person("Bertha","Bertha","Bertha",
                "Bertha","f",null,null,"Bob");

        Person LilBob = new Person("LilBob","LilBob","LilBob",
                "LilBob","m","Bob","bertha",null);

        ArrayList<Person> people = new ArrayList<>();
        people.add(bob);
        people.add(john);
        people.add(mary);
        people.add(bertha);
        people.add(LilBob);

        Map<String, Person> pat = new HashMap<>();
        pat.put(bob.getPersonID(),bob);
        pat.put(john.getPersonID(),john);
        pat.put(mary.getPersonID(),mary);
        paternal = pat;

        Map<String, Person> mat = new HashMap<>();
        mat.put(bertha.getPersonID(),bertha);
        maternal = mat;

        Map<String, Person> m = new HashMap<>();
        m.put(LilBob.getPersonID(),LilBob);
        m.put(bob.getPersonID(),bob);
        m.put(john.getPersonID(),john);

        male = m;

        Map<String, Person> f = new HashMap<>();
        f.put(mary.getPersonID(),mary);
        f.put(bertha.getPersonID(),bertha);

        female = f;

        testPeople = people;
        peopleMap = new HashMap<>();
        filterPerson = new HashMap<>();

        for(Person p : testPeople){
            peopleMap.put(p.getPersonID(),p);
            filterPerson.put(p.getPersonID(),p);
        }
    }



    /***
     * Makes fake event test date to coincide with the small family made in makeTestPeople();
     */
    void makeTestEvents(){
        Event bobBirth =
                new Event("bob_birth","bob_birth","Bob",
                "Bob", 1800,"USA","New York",0,0);

        Event bobMarriage =
                new Event("bob_marriage","bob_marriage","Bob",
                        "Bob", 1906,"USA","New York",0,0);

        Event bobFun =
                new Event("bob_fun","bob_had_fun","Bob",
                        "Bob", 1905,"USA","New York",0,0);

        Event bobDeath =
                new Event("bob_death","bob_death","Bob",
                        "Bob", 1904,"USA","New York",0,0);

        Event johnBirth =
                new Event("john_birth","birth","John",
                        "John", 1903,"WithBob","New York",0,0);

        Event maryBirth =
                new Event("mary_birth","birth","Mary",
                        "Mary", 1902,"USA","New York",0,0);

        Event berthaBirth =
                new Event("bertha_birth","birth","Bertha",
                        "Bertha", 2000,"USA","New York",0,0);

        Event LilBobBirth =
                new Event("LilBob_birth","birth","LilBob",
                        "LilBob", 1900,"USA","Bobville",0,0);

        ArrayList<Event> events = new ArrayList<>();
        events.add(bobBirth);
        events.add(bobMarriage);
        events.add(bobFun);
        events.add(bobDeath);
        events.add(johnBirth);
        events.add(maryBirth);
        events.add(berthaBirth);
        events.add(LilBobBirth);

        ArrayList<Event> feeder = new ArrayList<>();
        feeder.add(bobBirth);
        feeder.add(bobMarriage);
        feeder.add(bobFun);
        feeder.add(bobDeath);
        feeder.add(johnBirth);
        feeder.add(maryBirth);
        feeder.add(berthaBirth);
        feeder.add(LilBobBirth);

        Map<String, Event> filter = new HashMap<>();

        testEvents = events;
        feederEvents = feeder;
        eventsMap = new HashMap<>();

        for(Event e: events){
            eventsMap.put(e.getEventID(),e);
        }

        for(Event e : feederEvents){
            filter.put(e.getEventID(),e);
        }

        filterEventsMap = filter;
    }



    /***
     * Create events for testing.
     */
    private void makeEvents(){
        Map<String,List<Event>> peopleEvents = new HashMap<>();
        Map<String,List<Event>> filterEve = new HashMap<>();
        for(Person p: testPeople){
            List<Event> eve = new ArrayList<Event>();
            for(Event e: testEvents){
                eve.add(e);
            }
            peopleEvents.put(p.getPersonID(),eve);
            filterEve.put(p.getPersonID(),eve);
        }
        events = peopleEvents;
        filterEvents = filterEve;
    }


    /***
     * Sets up Datacache for proper Junit testing.
     */
    private void cacheSetup(){
        cache.setEventsMap(eventsMap);
        cache.setPeopleMap(peopleMap);
        cache.setEvents(events);

        cache.setFilterEventMap(filterEventsMap);
        cache.setFilterPeople(peopleMap);
        cache.setFilterEvents(events);

        cache.setPaternal(paternal);
        cache.setMaternal(maternal);
        cache.setMale(male);
        cache.setFilterPeople(female);
    }
}