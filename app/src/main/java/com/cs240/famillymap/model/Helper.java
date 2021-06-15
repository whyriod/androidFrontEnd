package com.cs240.famillymap.model;

import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Event;
import model.Person;

public class Helper {

    DataCache cache;


    /***
     * Grab the Cache
     */
    private void setup(){
        cache = DataCache.getInstance();
    }



    /***
     * Apply Filters from Settings.
     */
    public void applyFilter(){
        setup();
        //Clear the filtered people maps
        cache.getFilterPeopleMap().clear();
        cache.getFilterEventMap().clear();
        cache.getFilterEvents().clear();

        //Create new replacement maps.
        Map<String, Person> filterPeopleMap = new HashMap<String, Person>();
        Map<String, Event> filterEventsMap = new HashMap<String, Event>();
        Map<String, List<Event>> filterEvents = new HashMap<String, List<Event>>();

        //Go through each filer.
        sideFilter(filterPeopleMap);
        genderFilter(filterPeopleMap);
        eventFilter(filterPeopleMap,filterEventsMap);
        eventListFilter(filterPeopleMap,filterEventsMap,filterEvents);

        //And reset the Cache.
        cache.setFilterPeople(filterPeopleMap);
        cache.setFilterEventMap(filterEventsMap);
        cache.setFilterEvents(filterEvents);
        //Now filter the Map locations.
        filterMap();
    }



    /***
     * Goes through and uses precomputed maps to determine the first filter.
     * copyPeople is called to make a deep copy of the data for the filterPeopleMap.
     * @param filterPeopleMap - The newFilterPeople map.
     */
    private void sideFilter(Map<String,Person> filterPeopleMap){
        //Current User always shown.
        filterPeopleMap.put(cache.getPersonID(), cache.getPeopleMap().get(cache.getPersonID()));
        Person person = cache.getPeopleMap().get(cache.getPersonID());
        Person spouse = cache.getPeopleMap().get(person.getSpouseID());
        if(spouse != null){
            filterPeopleMap.put(spouse.getPersonID(), spouse);
        }

        //Filter out fathers side
        if(!cache.isFathersSide() && cache.isMothersSide()){
            copyPeople(filterPeopleMap,cache.getMaternal());
        }
        //Filter out mothers side
        else if(cache.isFathersSide() && !cache.isMothersSide()) {
            copyPeople(filterPeopleMap,cache.getPaternal());
        }
        //Keep Both sides
        else if(cache.isFathersSide() && cache.isMothersSide()){
            copyPeople(filterPeopleMap,cache.getPeopleMap());
        }
        //Otherwise, you filtered out both, so only the user should appear.
    }



    /***
     * The second round of filtering. Further filters the filterPeopleMap to
     * only contain genders of the specified type.
     *
     * @param filterPeopleMap - The half-filtered (maternal/paternal) map.
     */
    private void genderFilter(Map<String,Person> filterPeopleMap){
        //Filter out male and female events, so empty.
        ArrayList<String> removeID = new ArrayList<>();

        if(!cache.isMaleEvents() && !cache.isFemaleEvents()){
            filterPeopleMap.clear();
        }
        //Filter out male events
        else if(!cache.isMaleEvents() && cache.isFemaleEvents()){
            for(String s : filterPeopleMap.keySet()){
                if(filterPeopleMap.get(s).getGender().equals("m")){
                    removeID.add(s);
                }
            }

            for(String s: removeID){
                filterPeopleMap.remove(s);
            }
        }

        //Filter out female events.
        else if(cache.isMaleEvents() && !cache.isFemaleEvents()){
            for(String s : filterPeopleMap.keySet()){
                if(filterPeopleMap.get(s).getGender().equals("f")){
                    removeID.add(s);
                }
            }

            for(String s: removeID){
                filterPeopleMap.remove(s);
            }
        }

        //Otherwise, do not filter out anything else
    }



    /***
     * Deep copy people.
     */
    private void copyPeople(Map<String, Person> copy, Map<String, Person> toCopy){
        for(String s: toCopy.keySet()){
            copy.put(s,toCopy.get(s));
        }
    }



    /***
     * Uses the new filteredPeopleMap to filter copy events that pass over to the
     * filterEventsMap.
     *
     * @param filterPeopleMap - The fully filtered map
     * @param filterEventsMap - The brand new filterEventsMap
     */
    private void eventFilter(Map<String,Person> filterPeopleMap, Map<String,Event> filterEventsMap){
        Event event;

        //Populate Filtered Events
        for(String s: cache.getEventsMap().keySet()){
            event = cache.getEventsMap().get(s);
            if(filterPeopleMap.get(event.getPersonID()) != null){
                filterEventsMap.put(event.getEventID(),event);
            }
        }
    }



    /***
     * Uses the filteredPeople and filteredEvents maps to filter through the pre-existing
     * eventsMap from the cache. Because events are already ordered, no ordering is necessary.
     *
     * @param filterPeopleMap
     * @param filterEventsMap
     * @param filterEvents
     */
    private void eventListFilter(Map<String,Person> filterPeopleMap,
                                 Map<String,Event> filterEventsMap,
                                 Map<String,List<Event>> filterEvents){
        //Populate person filtered events
        for(String s: cache.getEvents().keySet()){
            if(filterPeopleMap.get(s) != null){
                ArrayList<Event> e = new ArrayList<>();
                for(String currEvent: filterEventsMap.keySet()){
                    if(filterEventsMap.get(currEvent).getPersonID().equals(s)){
                        e.add(filterEventsMap.get(currEvent));
                    }
                }
                filterEvents.put(s,sortYear(e));
            }
        }
    }



    /***
     * If a setting has been changed, go through all the markers. If they are
     * found in the filtered list of events, enable them, otherwise disable them.
     */
    private void filterMap(){
        Map<String, Marker> markers = cache.getMapMarkers();
        Map<String, Event> events = cache.getFilterEventMap();
        if(markers != null){
            for(String s: markers.keySet()){
                Event e = (Event) markers.get(s).getTag();
                markers.get(s).setVisible(events.get(e.getEventID()) != null);
            }
        }
    }



    /***
     * Sort the events for a person. What is earliest comes first.
     *
     * @param personEvents - The array to sort
     * @return - A new array that is sorted.
     */
    public ArrayList<Event> sortYear(ArrayList<Event> personEvents){
        ArrayList<Event> sortedList = new ArrayList<>();
        int min; //min date
        int index; //to add and remove

        //For all the Events in the personEvents
        while(personEvents != null && personEvents.size() > 0){
            index = 0;
            min = personEvents.get(0).getYear();
            for(int j = 0; j < personEvents.size(); j++){
                if(personEvents.get(j).getYear() <=  min){
                    index = j;
                    min = personEvents.get(j).getYear();
                }
            }
            sortedList.add(personEvents.get(index));
            personEvents.remove(index);
        }
        return sortedList;
    }



    /***
     * Filter events and persons by the id found.
     *
     * @param query - the string to search for.
     */
    public void filter(String query, ArrayList<Event> events, ArrayList<Person> relatives){
        setup();
        relatives.clear();
        events.clear();

        query = query.toUpperCase();

        if(!query.equals(null) && !query.equals("")){

            //Filtered.
            Map <String, Person> peopleMap = cache.getPeopleMap();
            //Note, people should be displayed, even if not shown in settings.
            Map <String, Event> eventMap = cache.getFilterEventMap();

            //Intermediate
            ArrayList<Event> eve = new ArrayList<>();
            ArrayList<Person> per = new ArrayList<>();



            //Populate Relatives
            for(String s: peopleMap.keySet()){
                per.add(peopleMap.get(s));
            }

            //Populate Events
            for(String s: eventMap.keySet()){
                eve.add(eventMap.get(s));
            }

            //filter
            for(Person p: per){
                if(p.getFirstName().toUpperCase().contains(query) ||
                        p.getLastName().toUpperCase().contains(query)){
                    relatives.add(p);
                }
            }

            for(Event e: eve){
                if( e.getCountry().toUpperCase().contains(query) ||
                        e.getCity().toUpperCase().contains(query) ||
                        e.getEventType().toUpperCase().contains(query) ||
                        Integer.toString(e.getYear()).contains(query)){
                    events.add(e);
                }
            }
        }
    };



    /***
     * Takes a person, and the personID of the current position. If its the father,
     * mother, or spouse then return that respective string. Otherwise, it must be a
     * child.
     *
     * @param currentPerson - A person object for the current Person.
     * @param personID - The PersonID for the person in this item view.
     * @return - The proper string for what familial relationship it is.
     */
    public String familyRelationship(Person currentPerson, String personID ){
        if(currentPerson.getFatherID() != null &&
                currentPerson.getFatherID().equals(personID)){
            return "Father";
        }
        else if(currentPerson.getMotherID() != null &&
                currentPerson.getMotherID().equals(personID)){
            return "Mother";
        }
        else if(currentPerson.getSpouseID() != null &&
                currentPerson.getSpouseID().equals(personID)){
            return "Spouse";
        }
        else if(personID != null){
            return "Child";
        }
        else{
            return "ERROR";
        }
    }
}

