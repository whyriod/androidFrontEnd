package com.cs240.server.service;


import dao.*;
import model.Authtoken;
import model.User;
import request.FillRequest;
import request.RegisterRequest;
import result.FillResult;
import result.RegisterResult;

import java.sql.Connection;
import java.util.UUID;

/***
 * Attempts to add User to User and Person table, and to
 * create a new authtoken. If it succeeds, they register.
 * If not, they fail.
 */
public class RegisterService {

    private Database db;
    private AuthTokenDAO aDAO;
    private EventDAO eDAO;
    private PersonDAO pDAO;
    private UserDAO uDAO;



    /***
     * Sets up Database Connection, and initialize needed DAO's.
     *
     * @throws DataAccessException - Database Connection errors
     */
    private void setUp() throws DataAccessException, ClassNotFoundException {

        db = new Database();
        Connection c = db.getConnection();
        aDAO = new AuthTokenDAO(c);
        pDAO = new PersonDAO(c);
        uDAO = new UserDAO(c);
    }



    /***
     * Creates a new User, Person, and Authtoken row.
     * @param r registerRequest Object
     * @return registerResult Object
     */
    public RegisterResult registerUser(RegisterRequest r){

        RegisterResult register;

        try{
            try{
                //Setup DB connections and find user
                setUp();
                User exist = uDAO.fetchUser(r.getUsername(),r.getPassword());

                //New User
                if(exist == null){
                    //Create User, Person, and Authtoken objects
                    User newUser =
                            new User(UUID.randomUUID().toString(),r.getUsername(),
                                    r.getPassword(),r.getEmail(),r.getFirstName(),
                                    r.getLastName(),r.getGender());


                    Authtoken newToken =
                            new Authtoken(UUID.randomUUID().toString(),newUser.getUsername());

                    //Insert and commit changes.
                    uDAO.insertUser(newUser);
                    db.closeConnection(true);

                    FillRequest fillRequest = new FillRequest(newUser.getUsername(),4);
                    FillService fillService = new FillService();
                    FillResult fillResult = fillService.fillDatabase(fillRequest);

                    if(fillResult.isSuccess()){
                        setUp();
                        aDAO.insertToken(newToken);
                        db.closeConnection(true);

                        register = new RegisterResult(newToken.getauthtoken(),newUser.getUsername(),
                                newUser.getPersonID(),true);
                    }
                    else{
                        register = new RegisterResult("Error: Could not create 4 generations",false);
                    }
                }
                //!New User
                else{
                    db.closeConnection(false);
                    register = new RegisterResult("Error: User already registered", false);
                }

            }
            //Register Failed
            catch (ClassNotFoundException | DataAccessException e) {
                //Rollback changes
                db.closeConnection(false);
                register = new RegisterResult(("Error: " + e.getMessage()),false);
                e.printStackTrace();
            }
        }
        //Connection close failed
        catch(DataAccessException e){
            register = new RegisterResult(("Error: " + e.getMessage()), false);
            e.printStackTrace();
        }
        return register;
    }
}
