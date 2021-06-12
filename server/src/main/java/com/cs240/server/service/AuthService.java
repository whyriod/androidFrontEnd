package com.cs240.server.service;

import dao.*;
import model.Authtoken;
import model.Person;
import model.User;
import request.AuthRequest;
import result.AuthResult;
import java.sql.Connection;

/***
 * Used in Event and Person services to authenticate that the user
 * has the correct authToken.
 */
public class AuthService {

    private Database db;
    private AuthTokenDAO aDAO;
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
     * Authenticate User with their Authtoken.
     *
     * @param r - The authRequest containing the Authtoken.
     * @return - AuthResult Object.
     */
    public AuthResult authenticateUser(AuthRequest r){

        AuthResult result;

        try{
            try{
                setUp();
                Authtoken token = aDAO.fetchToken(r.getAuthtoken());

                //Authenticated
                if(token != null){
                    User user = uDAO.fetchUser(token.getusername());
                    Person person = pDAO.fetchPerson(user.getPersonID());

                    result = new AuthResult(user.getUsername(),person.getPersonID(),
                            r.getAuthtoken(),true);
                }
                //!Authenticated
                else{
                    result = new AuthResult("Error: Invalid auth token",false);
                }
                //Close Connection
                db.closeConnection(false);
            }

            //Authentication failed
            catch (ClassNotFoundException | DataAccessException e) {
                //Rollback changes
                result = new AuthResult("Error: " + e,false);
                db.closeConnection(false);
                e.printStackTrace();
            }
        }
        //Connection close failed
        catch(DataAccessException e){
            result = new AuthResult("Error: " + e,false);
            e.printStackTrace();
        }
        return result;
    }
}
