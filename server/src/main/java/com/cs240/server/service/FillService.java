package com.cs240.server.service;

import cereal.Cereal;
import com.google.gson.Gson;
import dao.*;
import model.*;
import request.FillRequest;
import result.FillResult;

import java.io.*;
import java.sql.Connection;
import java.util.Random;

/***
 * Creates a family tree with the requested generational depth.
 * (Default is 4 generations). Also creates 3 events for each
 * person.
 */
public class FillService {

    private Database db;
    private AuthTokenDAO aDAO;
    private EventDAO eDAO;
    private PersonDAO pDAO;
    private UserDAO uDAO;
    private Names fNames;
    private Names mNames;
    private Names sNames;
    private Locations locations;
    private Random rand = new Random();



    /***
     * Sets up Database Connection, and initialize needed DAO's.
     *
     * @throws DataAccessException - Database Connection errors
     */
    private void setUp() throws DataAccessException, IOException {

        db = new Database();
        Connection c = db.getConnection();

        aDAO = new AuthTokenDAO(c);
        eDAO = new EventDAO(c);
        pDAO = new PersonDAO(c);
        uDAO = new UserDAO(c);

        readInJson();
    }



    /***
     * Read in Json data for filling data;
     *
     * @throws IOException - File not found, etc.
     */
    private void readInJson() throws IOException {
        //Stuff
        Gson gson = new Gson();
        Cereal cereal = new Cereal();
        InputStream filebody;
        String body;

        //Files
        File fFile = new File("json/fnames.json");
        File mFile = new File("json/mnames.json");
        File sFile = new File("json/snames.json");
        File lFile = new File("json/locations.json");

        //Read in Female names
        filebody = new FileInputStream(fFile);
        body = cereal.readString(filebody);
        fNames = gson.fromJson(body, Names.class);

        //Read in Male names;
        filebody = new FileInputStream(mFile);
        body = cereal.readString(filebody);
        mNames = gson.fromJson(body, Names.class);

        //Read in Surnames
        filebody = new FileInputStream(sFile);
        body = cereal.readString(filebody);
        sNames = gson.fromJson(body, Names.class);

        //Read in locations.
        filebody = new FileInputStream(lFile);
        body = cereal.readString(filebody);
        locations = gson.fromJson(body, Locations.class);
    }



    /***
     * Description: Deletes ALL data from the database: user, authTokens,
     * persons, and events.
     *
     * @return clearResult object.
     */
    public FillResult fillDatabase(FillRequest r) {

        FillResult result;

        try{
            try{
                //Generations Negative
                if(r.getGenerations() < 0){
                    result = new FillResult("Error: Generations not positive" ,false);
                }
                //Generations Positive
                else{
                    setUp();
                    User user = uDAO.fetchUser(r.getUsername());
                    //User exists
                    if(user != null){

                        //Delete existing person, event, and authToken data.
                        aDAO.deleteToken(user.getUsername());
                        eDAO.deleteEvent(user.getUsername());
                        pDAO.deletePerson(user.getUsername());

                        int people = 0;
                        for(int i = 0; i <= r.getGenerations(); i++){
                            people += Math.pow(2,i);
                        }

                        int events = people * 3;

                        generatePeople(user, r.getGenerations());
                        result = new FillResult("Successfully added " + people + " persons and " +
                                + events + " events to the database.",true);
                    }
                    //User does not exist
                    else{
                        result = new FillResult("Error: User does not exist",false);
                    }

                    //Close Connection
                    db.closeConnection(true);
                }
            }
            //Clear Failed
            catch (DataAccessException | IOException e) {
                //Rollback changes
                db.closeConnection(false);
                result = new FillResult(("Error: " + e.getMessage()),false);
                e.printStackTrace();
            }
        }
        //Connection close failed
        catch(DataAccessException e){
            result = new FillResult(("Error: " + e.getMessage()), false);
            e.printStackTrace();
        }
        return result;
    }


    /***
     *
     * @param user - user to generate for
     * @param gen - generation count
     * @throws DataAccessException - Problem with insertion occured.
     */
    private void generatePeople(User user, int gen) throws DataAccessException {

        Person person = new Person(user.getPersonID(),user.getUsername(),user.getFirstName(),
                user.getLastName(),user.getGender(),null,null,null);

        pDAO.insertPerson(person);

        //Atleast 18, But not older than 30.
        int id = rand.nextInt(1000);
        int year = 2021 - (rand.nextInt(13) + 18);
        int location = rand.nextInt(locations.getData().size());
        Event birth =
                new Event(person.getPersonID() + "_" + "birth" + id, "birth", person.getPersonID(),
                person.getAssociatedUsername(),year,locations.getData().get(location).getCountry(),
                locations.getData().get(location).getCity(),locations.getData().get(location).getLatitude(),
                locations.getData().get(location).getLongitude());

        //Baptism, 8 years later.

        year = birth.getYear() + 8;
        location = rand.nextInt(locations.getData().size());

        Event baptism =
                new Event(person.getPersonID() + "_" + "baptism", "baptism", person.getPersonID(),
                        person.getAssociatedUsername(),year,locations.getData().get(location).getCountry(),
                        locations.getData().get(location).getCity(),locations.getData().get(location).getLatitude(),
                        locations.getData().get(location).getLongitude());

        //Graduate at 18 years old.
        year = birth.getYear() + 18;
        location = rand.nextInt(locations.getData().size());

        Event highSchool =
                new Event(person.getPersonID() + "_" + "graduate_high_school", "graduate_high_school",
                        person.getPersonID(), person.getAssociatedUsername(),year,locations.getData().get(location).getCountry(),
                        locations.getData().get(location).getCity(),locations.getData().get(location).getLatitude(),
                        locations.getData().get(location).getLongitude());

        eDAO.insertEvent(birth);
        eDAO.insertEvent(baptism);
        eDAO.insertEvent(highSchool);

        generatePersons(person, gen, birth);
    }


    /***
     *
     * @param person - current person
     * @param gen - current generation cound
     * @param birth - The birth event for the current person
     *              (prevents having to make another db call)
     *
     * @throws DataAccessException - Error with insertion
     */
    private void generatePersons(Person person, int gen, Event birth) throws DataAccessException {

        //If there are more generations to create.
        if(gen > 0){
            //Find random names.
            int fName = rand.nextInt(fNames.getData().size());
            int mName = rand.nextInt(mNames.getData().size());
            int sName = rand.nextInt(sNames.getData().size());

            //Create Female traits
            int id = rand.nextInt(1000);
            String fFirstName = fNames.getData().get(fName);
            String fMaidenName = sNames.getData().get(sName);
            String fGender = "F";
            String fPersonID = fFirstName + "_" + fMaidenName + id;

            //Create Male traits
            id = rand.nextInt(1000);
            String mFirstName = mNames.getData().get(mName);
            String mLastName = person.getLastName();
            String mGender =  "M";
            String mPersonID = mFirstName + "_" + mLastName + id;

            Person female =
                    new Person(fPersonID,person.getAssociatedUsername(),fFirstName,
                            fMaidenName,fGender,null,null,mPersonID);

            Person male =
                    new Person(mPersonID,person.getAssociatedUsername(),mFirstName,
                            mLastName,mGender,null,null,fPersonID);

            //Birth
            //Mother is atleast 18, and no older than 50 when she gave birth.
            int year = birth.getYear() - (rand.nextInt(33) + 18);
            int location = rand.nextInt(locations.getData().size());
            Event fBirth =
                    new Event(female.getPersonID() + "_" + "birth", "birth", female.getPersonID(),
                            person.getAssociatedUsername(),year,locations.getData().get(location).getCountry(),
                            locations.getData().get(location).getCity(),locations.getData().get(location).getLatitude(),
                            locations.getData().get(location).getLongitude());

            //Was at max 6 years older than his wife, or at least 3 years younger.
            year = year + (rand.nextInt(7) - rand.nextInt(4));
            location = rand.nextInt(locations.getData().size());
            Event mBirth =
                    new Event(male.getPersonID() + "_" + "birth", "birth", male.getPersonID(),
                            person.getAssociatedUsername(),year,locations.getData().get(location).getCountry(),
                            locations.getData().get(location).getCity(),locations.getData().get(location).getLatitude(),
                            locations.getData().get(location).getLongitude());

            //Marriage
            //Mother was married after 18 sometime before her child's birth.
            year =  (fBirth.getYear() + 18) + rand.nextInt((birth.getYear() - fBirth.getYear() + 18));
            location = rand.nextInt(locations.getData().size());

            Event fMarriage =
                    new Event(female.getPersonID() + "_" + "marriage", "marriage", female.getPersonID(),
                            person.getAssociatedUsername(),year,locations.getData().get(location).getCountry(),
                            locations.getData().get(location).getCity(),locations.getData().get(location).getLatitude(),
                            locations.getData().get(location).getLongitude());

            Event mMarriage =
                    new Event(male.getPersonID() + "_" + "marriage", "marriage", male.getPersonID(),
                            person.getAssociatedUsername(),year,locations.getData().get(location).getCountry(),
                            locations.getData().get(location).getCity(),locations.getData().get(location).getLatitude(),
                            locations.getData().get(location).getLongitude());

            //Death
            //Died after first birth, sometime before they were 95
            year = birth.getYear() + rand.nextInt((95 - (birth.getYear() - fBirth.getYear())));
            if(year > 2021){
                year = 2021;
            }
            location = rand.nextInt(locations.getData().size());
            Event fDeath =
                    new Event(female.getPersonID() + "_" + "death", "death", female.getPersonID(),
                            person.getAssociatedUsername(),year,locations.getData().get(location).getCountry(),
                            locations.getData().get(location).getCity(),locations.getData().get(location).getLatitude(),
                            locations.getData().get(location).getLongitude());

            //Death
            //Died after first birth, sometime before they were 90 (Men  live alittle shorter)
            year = birth.getYear() + rand.nextInt((90 - (birth.getYear() - mBirth.getYear())));
            if(year > 2021){
                year = 2021;
            }
            location = rand.nextInt(locations.getData().size());
            Event mDeath =
                    new Event(male.getPersonID() + "_" + "death", "death", male.getPersonID(),
                            person.getAssociatedUsername(),year,locations.getData().get(location).getCountry(),
                            locations.getData().get(location).getCity(),locations.getData().get(location).getLatitude(),
                            locations.getData().get(location).getLongitude());

            pDAO.insertPerson(female);
            pDAO.insertPerson(male);

            pDAO.updateParents(person.getPersonID(), male.getPersonID(),female.getPersonID());

            eDAO.insertEvent(fBirth);
            eDAO.insertEvent(fMarriage);
            eDAO.insertEvent(fDeath);

            eDAO.insertEvent(mBirth);
            eDAO.insertEvent(mMarriage);
            eDAO.insertEvent(mDeath);

            generatePersons(male,gen-1,mBirth);
            generatePersons(female,gen-1,fBirth);
        }
    }
}
