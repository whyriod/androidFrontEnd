package com.cs240.famillymap.net;

import android.app.Person;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ServiceConfigurationError;

import request.AuthRequest;
import request.LoginRequest;
import request.PersonsRequest;
import request.RegisterRequest;
import result.ClearResult;
import result.EventResult;
import result.EventsResult;
import result.LoginResult;
import result.PersonsResult;
import result.RegisterResult;

import static org.junit.jupiter.api.Assertions.*;

/***
 * Test each method for the server Proxy to make sure that it works.
 */
class ServerProxyTest {

    private ServerProxy proxy;
    private RegisterRequest rr;
    private LoginRequest lr;



    /***
     * Set up the host, port, and clear the DB.
     */
    @BeforeEach
    void preGame(){
        proxy = ServerProxy.getInstance();
        proxy.setHostname("localhost");
        proxy.setPort(8080);
        proxy.clear(); //Make sure to clear DB each time.
    }



    /***
     * Personal text to make sure that clear works as
     * it is integral to the other tests.
     */
    @Test
    void clear(){
        ClearResult result = proxy.clear();
        assertTrue(result.isSuccess());
    }



    /***
     * Relatively straightforward. If it works, then you were registered.
     */
    @Test
    void registerNewUser() {
        RegisterRequest request =
                new RegisterRequest("Bob","Bob","Bob",
                        "Bob","Robert","m");

        assertTrue(proxy.register(request).isSuccess());
    }



    /***
     * If you were registered a second time, we should succeed the first time,
     * and fail the second with a message that you already registered.
     */
    @Test
    void registerExisting() {
        RegisterRequest request =
                new RegisterRequest("John","John","John",
                        "John","Johnathan","m");


        RegisterResult resultOne = proxy.register(request);
        RegisterResult resultTwo = proxy.register(request);

        assertTrue(resultOne.isSuccess());
        assertFalse(resultTwo.isSuccess());
        assertEquals("Error: User already registered",resultTwo.getMessage());
    }



    /***
     * If you login to a person who was just created, you should get back success.
     * Also, If you login again, you should get back a new AuthToken.
     */
    @Test
    void loginExistingUser() {
        //Register Bob
        RegisterRequest rr =
                new RegisterRequest("Bob","Bob","Bob",
                        "Bob","Robert","m");

        assertTrue(proxy.register(rr).isSuccess());

        LoginRequest lrOne = new LoginRequest("Bob","Bob");
        LoginResult r1 = proxy.login(lrOne);

        assertTrue(r1.isSuccess());

        LoginRequest lrTwo = new LoginRequest("Bob","Bob");
        LoginResult r2 = proxy.login(lrTwo);

        assertTrue(r2.isSuccess());

        //Check that authTokens differ. This shows Login is working properly
        assertNotNull(r1.getAuthtoken());
        assertNotNull(r2.getAuthtoken());
        assertNotEquals(r1.getAuthtoken(),r2.getAuthtoken());
    }



    /***
     * If you try to login to an empty database, you should get back an error,
     * and no important information.
     */
    @Test
    void loginNonExistingUser() {
        LoginRequest lrOne = new LoginRequest("Bob","Bob");
        LoginResult r1 = proxy.login(lrOne);

        assertFalse(r1.isSuccess());
        assertNull(r1.getAuthtoken());
        assertNull(r1.getPersonID());
        assertNull(r1.getUsername());
    }



    /***
     * If you register a person, and then ask to retrieve those people,
     * You should be authorized and receive 31 people.
     */
    @Test
    void personsAuthorized() {
        RegisterRequest rRequest =
                new RegisterRequest("Bob","Bob","Bob",
                        "Bob","Robert","m");

        //Check to make sure it succeeded.
        RegisterResult rResult = proxy.register(rRequest);
        assertTrue(rResult.isSuccess());
        assertNotNull(rResult.getPersonID());

        //Setup a Person Request.
        AuthRequest request = new AuthRequest(rResult.getAuthtoken());
        PersonsResult result = proxy.persons(request);
        assertTrue(result.isSuccess());
        assertEquals(31,result.getData().size());
    }


    /***
     * If you have attempt to use an invalid token to request person information
     * You should be rejected, and get and Invalid auth Token error.
     */
    @Test
    void personsInvalidAuthToken() {
        AuthRequest request = new AuthRequest("1234567890987654321");
        PersonsResult result = proxy.persons(request);
        assertFalse(result.isSuccess());
        assertNull(result.getData());
        assertEquals("Error: Invalid auth token",result.getMessage());
    }



    /***
     * If you register a person, and then ask to retrieve those events,
     * You should be authorized and receive 93 events.
     */
    @Test
    void eventsAuthorized() {
        RegisterRequest rRequest =
                new RegisterRequest("Bob","Bob","Bob",
                        "Bob","Robert","m");

        //Check to make sure it succeeded.
        RegisterResult rResult = proxy.register(rRequest);
        assertTrue(rResult.isSuccess());
        assertNotNull(rResult.getPersonID());

        //Setup a Person Request.
        AuthRequest request = new AuthRequest(rResult.getAuthtoken());
        EventsResult result = proxy.events(request);
        assertTrue(result.isSuccess());
        assertEquals(93,result.getData().size());
    }



    /***
     * If you have attempt to use an invalid token to request event information
     * You should be rejected, and get and Invalid auth Token error.
     */
    @Test
    void eventsInvalidAuthToken() {
        AuthRequest request = new AuthRequest("1234567890987654321");
        EventsResult result = proxy.events(request);
        assertFalse(result.isSuccess());
        assertNull(result.getData());
        assertEquals("Error: Invalid auth token",result.getMessage());
    }
}