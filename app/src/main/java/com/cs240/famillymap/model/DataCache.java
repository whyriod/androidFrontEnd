package com.cs240.famillymap.model;

import android.view.Menu;

import androidx.core.content.ContextCompat;

import com.cs240.famillymap.R;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.Event;
import model.Person;


/***
 * Data Cache for entire program. Loaded with data from ServerProxy in Login fragment.
 * Filtered in main fragment after settings are changed.
 */
public class DataCache {

    //Main Maps
    private Map<String, Person> peopleMap;
    private Map<String, Event> eventsMap;
    private Map<String, List<Event>> events;
    private Map<String,Float> eventColors;
    private ArrayList<Float> colors;

    //Preset Filter Maps
    private Map<String, Person> paternal;
    private Map<String, Person> maternal;
    private Map<String, Person> male;
    private Map<String, Person> female;

    //ComputedFilter Maps
    private Map<String, Person> filterPeopleMap;
    private Map<String, Event> filterEventMap;
    private Map<String, List<Event>> filterEvents;

    //Google Maps
    private Map<String,Marker> mapMarkers;
    private ArrayList<Polyline> maplines;

    //Current Person logged in, and menu object to manipulate
    private String personID;
    private boolean loggedIn;
    private Menu menu;

    //Settings
    private boolean lifeStoryLines;
    private boolean familyTreeLines;
    private boolean spouseLines;
    private boolean fathersSide;
    private boolean mothersSide;
    private boolean maleEvents;
    private boolean femaleEvents;
    private boolean filter;

    //The single instance
    private static DataCache _instance;



    /***
     * Singleton Principle
     *
     * @return - The One and only DataCache.
     */
    public static DataCache getInstance(){
        if(_instance == null){
            _instance = new DataCache();
        }
        return _instance;
    }



    /**
     * Set all lines to on, no one is logged in, and set up Color array for markers.
     */
    private DataCache(){
        lifeStoryLines = true;
        familyTreeLines = true;
        spouseLines = true;
        fathersSide = true;
        mothersSide = true;
        maleEvents = true;
        femaleEvents = true;
        loggedIn = false;

        //Add Colors to Array
        colors = new ArrayList<>();
        colors.add(BitmapDescriptorFactory.HUE_BLUE);
        colors.add(BitmapDescriptorFactory.HUE_RED);
        colors.add(BitmapDescriptorFactory.HUE_YELLOW);
        colors.add(BitmapDescriptorFactory.HUE_VIOLET);
        colors.add(BitmapDescriptorFactory.HUE_ORANGE);
        colors.add(BitmapDescriptorFactory.HUE_GREEN);
        colors.add(BitmapDescriptorFactory.HUE_MAGENTA);
        colors.add(BitmapDescriptorFactory.HUE_AZURE);
        colors.add(BitmapDescriptorFactory.HUE_ROSE);
        colors.add(BitmapDescriptorFactory.HUE_CYAN);

        maplines = new ArrayList<>();
    }

    public Map<String, Person> getPeopleMap() {
        return peopleMap;
    }

    public void setPeopleMap(Map<String, Person> peopleMap) {
        this.peopleMap = peopleMap;
    }

    public Map<String, Event> getEventsMap() {
        return eventsMap;
    }

    public void setEventsMap(Map<String, Event> eventsMap) {
        this.eventsMap = eventsMap;
    }

    public Map<String, List<Event>> getEvents() {
        return events;
    }

    public void setEvents(Map<String, List<Event>> events) {
        this.events = events;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public boolean isLifeStoryLines() {
        return lifeStoryLines;
    }

    public void setLifeStoryLines(boolean lifeStoryLines) {
        this.lifeStoryLines = lifeStoryLines;
    }

    public boolean isFamilyTreeLines() {
        return familyTreeLines;
    }

    public void setFamilyTreeLines(boolean familyTreeLines) {
        this.familyTreeLines = familyTreeLines;
    }

    public boolean isSpouseLines() {
        return spouseLines;
    }

    public void setSpouseLines(boolean spouseLines) {
        this.spouseLines = spouseLines;
    }

    public boolean isFathersSide() {
        return fathersSide;
    }

    public void setFathersSide(boolean fathersSide) {
        this.fathersSide = fathersSide;
    }

    public boolean isMothersSide() {
        return mothersSide;
    }

    public void setMothersSide(boolean mothersSide) {
        this.mothersSide = mothersSide;
    }

    public boolean isMaleEvents() {
        return maleEvents;
    }

    public void setMaleEvents(boolean maleEvents) {
        this.maleEvents = maleEvents;
    }

    public boolean isFemaleEvents() {
        return femaleEvents;
    }

    public void setFemaleEvents(boolean femaleEvents) {
        this.femaleEvents = femaleEvents;
    }

    public boolean isFilter() {
        return filter;
    }

    public void setFilter(boolean filter) {
        this.filter = filter;
    }

    public ArrayList<Float> getColors() {
        return colors;
    }

    public Map<String, Float> geteventColors() {
        return eventColors;
    }

    public void seteventColors(Map<String, Float> eventColors) {
        this.eventColors = eventColors;
    }

    public Map<String, List<Event>> getFilterEvents() {
        return filterEvents;
    }

    public void setFilterEvents(Map<String, List<Event>> filterEvents) {
        this.filterEvents = filterEvents;
    }

    public Map<String, Person> getFilterPeopleMap() {
        return filterPeopleMap;
    }

    public void setFilterPeople(Map<String, Person> filterPeople) {
        this.filterPeopleMap = filterPeople;
    }

    public Map<String, Person> getPaternal() {
        return paternal;
    }

    public void setPaternal(Map<String, Person> paternal) {
        this.paternal = paternal;
    }

    public Map<String, Person> getMaternal() {
        return maternal;
    }

    public void setMaternal(Map<String, Person> maternal) {
        this.maternal = maternal;
    }

    public Map<String, Person> getMale() {
        return male;
    }

    public void setMale(Map<String, Person> male) {
        this.male = male;
    }

    public Map<String, Person> getFemale() {
        return female;
    }

    public void setFemale(Map<String, Person> female) {
        this.female = female;
    }

    public Map<String, Event> getFilterEventMap() {
        return filterEventMap;
    }

    public void setFilterEventMap(Map<String, Event> filterEventMap) {
        this.filterEventMap = filterEventMap;
    }

    public Map<String,Marker> getMapMarkers() {
        return mapMarkers;
    }

    public void setMapMarkers(Map<String,Marker> mapMarkers) {
        this.mapMarkers = mapMarkers;
    }

    public ArrayList<Polyline> getMaplines() {
        return maplines;
    }

    //My Setter is more of just a pushback function.
    public void addLine(Polyline line){
        this.maplines.add(line);
    }
}
