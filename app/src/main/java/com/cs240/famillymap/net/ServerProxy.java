package com.cs240.famillymap.net;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import request.AuthRequest;
import request.LoginRequest;
import request.RegisterRequest;
import result.ClearResult;
import result.EventsResult;
import result.LoginResult;
import result.PersonsResult;
import result.RegisterResult;


/***
 * Proxies the Server Class to send and receive messages.
 */
public class ServerProxy {

    //The Single instance
    private static ServerProxy _instance;

    String hostname;
    int port;


    /***
     * Private Constructor
     */
    private ServerProxy(){ }



    /***
     * Singleton Principle for Server Proxy (I just wanted there to be only one proxy)
     *
     * @return - The one and only Proxy
     */
    public static ServerProxy getInstance(){
        if(_instance == null){
            _instance = new ServerProxy();
        }
        return _instance;
    }



    /***
     * Takes a RegisterRequest object and sends the request to the servers
     * /user/register endpoint. Returns the result.
     *
     * @param r - The request for the server.
     * @return - The result from the server.
     */
    public RegisterResult register(RegisterRequest r){

        RegisterResult result;
        Cereal cereal = new Cereal();
        Gson gson = new Gson();

        try {
            //Create the Url and config object.
            URL url = new URL("http://" + hostname + ":" + port + "/user/register");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            http.setRequestMethod("POST");
            http.setDoOutput(true);	// There is a request body
            http.addRequestProperty("Accept", "application/json"); //Json
            http.connect();

            String reqData = gson.toJson(r);
            OutputStream reqBody = http.getOutputStream();
            cereal.writeString(reqData, reqBody);
            reqBody.close();
            InputStream respBody;
            String respData;

            //Interpret the response.
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("Successfully registered.");
                respBody = http.getInputStream();
                respData = cereal.readString(respBody);
            }
            else {
                System.out.println("Error: " + http.getResponseMessage());
                respBody = http.getErrorStream();
                respData = cereal.readString(respBody);
            }
            result = gson.fromJson(respData, RegisterResult.class);
        }
        catch (IOException e) {
            result = new RegisterResult("Error: Failed in ServerProxy register Catch",false);
            e.printStackTrace();
        }
        return result;
    }



    /***
     * Takes a LoginRequest object and sends the request to the servers
     * /user/login endpoint. Returns the result.
     *
     * @param r - The request for the server.
     * @return - The result from the server.
     */
    public LoginResult login(LoginRequest r){

        LoginResult result;
        Cereal cereal = new Cereal();
        Gson gson = new Gson();

        try {
            //Create the Url and config object.
            URL url = new URL("http://" + hostname + ":" + port + "/user/login");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            http.setRequestMethod("POST");
            http.setDoOutput(true);	// There is a request body
            http.addRequestProperty("Accept", "application/json"); //Json
            http.connect();

            String reqData = gson.toJson(r);
            OutputStream reqBody = http.getOutputStream();
            Cereal.writeString(reqData, reqBody);
            reqBody.close();
            InputStream respBody;
            String respData;

            //Interpret the response.
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK){
                System.out.println("Successfully logged in.");
                respBody = http.getInputStream();
                respData = cereal.readString(respBody);
            }
            else {
                System.out.println("Error: " + http.getResponseMessage());
                respBody = http.getErrorStream();
                respData = cereal.readString(respBody);
            }
            result = gson.fromJson(respData, LoginResult.class);
        }
        catch (IOException e) {
            result = new LoginResult("Error: Failed in ServeryProxy login catch",false);
            e.printStackTrace();
        }
        return result;
    }



    /***
     * Takes an AuthRequest object and sends the request to the servers
     * /person endpoint. Returns the result.
     *
     * @param r - The request for the server.
     * @return - The result from the server.
     */
    public PersonsResult persons(AuthRequest r){

        PersonsResult result;
        Cereal cereal = new Cereal();
        Gson gson = new Gson();

        try {
            //Create the Url and config object.
            URL url = new URL("http://" + hostname + ":" + port + "/person");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            http.setRequestMethod("GET");
            http.setDoOutput(false);	// There is no request body
            http.addRequestProperty("Authorization", r.getAuthtoken());
            http.addRequestProperty("Accept", "application/json"); //Json
            http.connect();

            InputStream respBody;
            String respData;

            //Interpret the response.
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("Successfully retrieved persons.");
                respBody = http.getInputStream();
                respData = cereal.readString(respBody);
            }
            else {
                System.out.println("Error: " + http.getResponseMessage());
                respBody = http.getErrorStream();
                respData = cereal.readString(respBody);
            }
            result = gson.fromJson(respData,PersonsResult.class);
        }
        catch (IOException e) {
            result = new PersonsResult("Error: Failed in SeverProxy persons catch",false);
            e.printStackTrace();

        }
        return result;
    }



    /***
     * Takes an AuthRequest object and sends the request to the servers
     * /person endpoint. Returns the result.
     *
     * @param r - The request for the server.
     * @return - The result from the server.
     */
    public EventsResult events(AuthRequest r){

        EventsResult result;
        Cereal cereal = new Cereal();
        Gson gson = new Gson();

        try {
            //Create the Url and config object.
            URL url = new URL("http://" + hostname + ":" + port + "/events");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            http.setRequestMethod("GET");
            http.setDoOutput(false);	// There is no request body
            http.addRequestProperty("Authorization", r.getAuthtoken());
            http.addRequestProperty("Accept", "application/json"); //Json
            http.connect();

            InputStream respBody;
            String respData;

            //Interpret the response.
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("Successfully retrieved events.");
                respBody = http.getInputStream();
                respData = cereal.readString(respBody);
            }
            else {
                System.out.println("Error: " + http.getResponseMessage());
                respBody = http.getErrorStream();
                respData = cereal.readString(respBody);
            }
            result = gson.fromJson(respData,EventsResult.class);
        }
        catch (IOException e) {
            result = new EventsResult("Error: Failed in SeverProxy persons catch",false);
            e.printStackTrace();

        }
        return result;
    }

    ClearResult clear(){

        ClearResult result;
        Cereal cereal = new Cereal();
        Gson gson = new Gson();

        try {
            //Create the Url and config object.
            URL url = new URL("http://" + hostname + ":" + port + "/clear");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            http.setRequestMethod("POST");
            http.setDoOutput(false);	// There is no request body
            http.connect();

            InputStream respBody;
            String respData;

            //Interpret the response.
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("Successfully cleared Database.");
                respBody = http.getInputStream();
                respData = cereal.readString(respBody);
            }
            else {
                System.out.println("Error: " + http.getResponseMessage());
                respBody = http.getErrorStream();
                respData = cereal.readString(respBody);
            }
            result = gson.fromJson(respData,ClearResult.class);
        }
        catch (IOException e) {
            result = new ClearResult("Error: Failed in SeverProxy persons catch",false);
            e.printStackTrace();

        }
        return result;
    }


    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
