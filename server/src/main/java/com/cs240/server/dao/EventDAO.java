package com.cs240.server.dao;

import model.Event;
import java.sql.*;
import java.util.ArrayList;

/***
 * Performs the following SQL Statements on the Event Table:
 * Create Table
 * Drop Table
 * Select (Single)
 * Select (All)
 * Insert (Single)
 * Delete (Associated User)
 * Delete (All)
 */
public class EventDAO {

    private final Connection c;



    /***
     * Constructor.
     *
     * @param c - Connection from Database class.
     */
    public EventDAO(Connection c)
    {
        this.c = c;
    }



    /***
     * Creates the Event Table
     *
     * @throws DataAccessException - Unable to create Event Table: + e
     */
    public void createTable() throws DataAccessException {

        //Event Table
        String sqlE = "Create Table IF NOT EXISTS Event(\n" +
                      "EventID                varChar(20) PRIMARY KEY NOT NULL,\n" +
                      "EventType              varChar(20) NOT NULL,\n" +
                      "PersonID               varchar(20) NOT NULL,\n" +
                      "AssociatedUsername     varchar(20),\n" +
                      "Year                   Integer NOT NULL,\n" +
                      "Country                varchar(50) NOT NULL,\n" +
                      "City                   varchar(50) NOT NULL,\n" +
                      "Latitude               float NOT NULL,\n" +
                      "Longitude              float NOT NULL);";

        try (Statement stmt = c.createStatement()){
            //Create Table
            stmt.executeUpdate(sqlE);
        }
        //SQL Error
        catch (SQLException e) {
            throw new DataAccessException("Unable to create Event Table: " + e);
        }
    }



    /***
     * For Testing Purposes, I need to make sure that the
     * Event table is properly created. So dropping it is
     * useful.
     *
     * @throws DataAccessException - Unable to drop Event table: + e
     */
    public void dropTable() throws DataAccessException{

        //Initialize and prepare statements.
        String sqlE = "Drop Table Event";

        try (Statement stmt = c.createStatement()){
            //Execute
            stmt.executeUpdate(sqlE);
        }
        //SQL Error
        catch (SQLException e) {
            throw new DataAccessException("Unable to drop Event table: " + e);
        }
    }



    /***
     * (Select - Single)
     * Takes an eventID String. Selects the event associated with the ID,
     * and then returns the row as an event object. Returns null if no row found.
     *
     * @param eventID - The eventID to check for.
     * @return event - The event object for the row found.
     * @throws DataAccessException - Unable to find event: + e
     */
    public Event fetchEvent(String eventID) throws DataAccessException {

        //Initialize and prepare statements.
        Event event = null;
        ResultSet ur = null;
        String sql = "SELECT * FROM Event WHERE EventID = ?;";

        //Execute the Query. If you find a row, set the Authtoken values.
        try (PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setString(1, eventID);
            ur = stmt.executeQuery();

            //If a row is found
            if (ur.next()) {
                event = new Event(
                        ur.getString("EventID"),
                        ur.getString("EventType"),
                        ur.getString("PersonID"),
                        ur.getString("AssociatedUsername"),
                        ur.getInt("Year"),
                        ur.getString("Country"),
                        ur.getString("City"),
                        ur.getFloat("Latitude"),
                        ur.getFloat("Longitude")
                );
                return event;
            }
        }
        //SQL Error
        catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to find event: " + e);
        }
        //Close the resultSet
        finally {
            if(ur != null) {
                try {
                    ur.close();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }



    /***
     * (Select - All)
     * Takes an personID String. Selects all events associated with the ID,
     * and then returns the rows as an array of event objects. Returns empty
     * array if none are found.
     *
     * @param personID - The personID to check for.
     * @return event - The event object for the row found.
     * @throws DataAccessException - Unable to find events: + e
     */
    public ArrayList<Event> fetchEvents(String personID) throws DataAccessException {

        //Initialize and prepare statements.
        ArrayList<Event> events = new ArrayList<Event>();
        Event event = null;
        ResultSet ur = null;
        String sql = "SELECT * FROM Event WHERE PersonID = ?;";

        //Execute the Query. If you find a row, set the Authtoken values.
        try (PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setString(1, personID);
            ur = stmt.executeQuery();

            //If a row is found

            while (ur.next()) {
                event = new Event(
                        ur.getString("EventID"),
                        ur.getString("EventType"),
                        ur.getString("PersonID"),
                        ur.getString("AssociatedUsername"),
                        ur.getInt("Year"),
                        ur.getString("Country"),
                        ur.getString("City"),
                        ur.getFloat("Latitude"),
                        ur.getFloat("Longitude")
                );
                events.add(event);
            }

        }
        //SQL Error
        catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to find events: " + e);
        }
        //Close the resultSet
        finally {
            if(ur != null) {
                try {
                    ur.close();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return events;
    }



    /***
     * (Insert)
     * Takes an event object. Inserts a new event.
     *
     * @param event - New event object to insert into the Database
     * @throws DataAccessException - Unable to create event: + e
     */
    public void insertEvent(Event event) throws DataAccessException {

        //Prepare Statements
        String sql = "INSERT INTO Event (EventID, EventType, PersonID, AssociatedUsername, " +
                "Year, Country, City, Latitude, Longitude) VALUES(?,?,?,?,?,?,?,?,?);";

        try (PreparedStatement stmt = c.prepareStatement(sql)) {
            //Set Event values.
            stmt.setString(1, event.getEventID());
            stmt.setString(2, event.getEventType());
            stmt.setString(3, event.getPersonID());
            stmt.setString(4, event.getAssociatedUsername());
            stmt.setInt(5, event.getYear());
            stmt.setString(6, event.getCountry());
            stmt.setString(7, event.getCity());
            stmt.setFloat(8, event.getLatitude());
            stmt.setFloat(9, event.getLongitude());
            //Execute Query
            stmt.executeUpdate();
        }
        //SQL Error
        catch (SQLException e) {
            throw new DataAccessException("Unable to create event: " + e);
        }
    }



    /***
     * (Delete)
     * Takes a username. Deletes Events who have that associated username.
     *
     * @param asscoiatedUsername - username of event to delete.
     * @throws DataAccessException - Unable to delete event: + e
     */
    public void deleteEvent(String asscoiatedUsername) throws DataAccessException {

        //Prepare Statements
        String sql = "DELETE FROM Event WHERE AssociatedUsername = ?;";

        try (PreparedStatement stmt = c.prepareStatement(sql)) {
            //Set Person values.
            stmt.setString(1, asscoiatedUsername);

            //Execute Query
            stmt.executeUpdate();
        }
        //SQL Error
        catch (SQLException e) {
            throw new DataAccessException("Unable to delete event: " + e);
        }
    }



    /***
     * (Delete)
     * Clears all events from the Event table.
     *
     * @throws DataAccessException - Unable to clear events: + e
     */
    public void clear() throws DataAccessException {

        //Prepare Statements
        String sql = "DELETE FROM Event";

        try (Statement stmt = c.createStatement()){
            //Execute Query
            stmt.executeUpdate(sql);
        }
        //SQL Error
        catch (SQLException e) {
            throw new DataAccessException("Unable to clear events: " + e);
        }
    }
}
