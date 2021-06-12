package com.cs240.server.service;

import dao.*;
import model.Authtoken;
import model.User;
import request.LoginRequest;
import result.LoginResult;

import java.sql.Connection;
import java.util.UUID;

/***
 * Attempts to log user by checking that the user with that
 * name and password exist. If they do, they login in. Otherwise
 * they fail.
 */
public class LoginService {

    private Database db;
    private AuthTokenDAO aDAO;
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
        uDAO = new UserDAO(c);
    }



    /***
     * Description: Logs in the user and returns an auth token.
     * @param r loginRequest
     * @return loginResult object
     */
    public LoginResult loginUser(LoginRequest r){

        LoginResult login;

        try{
            try{
                //Setup DB connections and find user
                setUp();
                User user = uDAO.fetchUser(r.getUsername(),r.getPassword());

                //Logged in
                if(user != null){
                    //Create new Token and commit
                    Authtoken newToken = new Authtoken(UUID.randomUUID().toString(),r.getUsername());
                    aDAO.insertToken(newToken);

                    login = new LoginResult(newToken.getauthtoken(),newToken.getusername(),
                            user.getPersonID(),true);
                }
                //!Logged in
                else{
                    login = new LoginResult("Error: Invalid username or password",false);
                }

                //Close Connection
                db.closeConnection(true);
            }
            //Login failed
            catch (ClassNotFoundException | DataAccessException e) {
                //Rollback changes
                db.closeConnection(false);
                login = new LoginResult(("Error: " + e.getMessage()),false);
                e.printStackTrace();
            }
        }
        //Connection close failed
        catch(DataAccessException e){
            login = new LoginResult(("Error: " + e.getMessage()),false);
            e.printStackTrace();
        }
        return login;
    }
}
