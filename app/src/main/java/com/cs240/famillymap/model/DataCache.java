package com.cs240.famillymap.model;

import android.view.Menu;

import com.cs240.famillymap.R;

import java.util.List;
import java.util.Map;

import model.Event;
import model.Person;

public class DataCache {

    private String personID;
    private boolean loggedIn;
    private Map<String, Person> peopleMap;
    private Map<String, Event> eventsMap;
    private Map<String, List<Event>> events;
    private Menu menu;

    private boolean lifeStoryLines;
    private boolean familyTreeLines;
    private boolean spouseLines;
    private boolean fathersSide;
    private boolean mothersSide;
    private boolean maleEvents;
    private boolean femaleEvents;
    private boolean filter;


    private static DataCache _instance;

    public static DataCache getInstance(){
        if(_instance == null){
            _instance = new DataCache();
        }
        return _instance;
    }

    private DataCache(){
        lifeStoryLines = true;
        familyTreeLines = true;
        spouseLines = true;
        loggedIn = false;
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
}
