package com.cs240.server.handler.User;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import request.AuthRequest;
import request.PersonsRequest;
import request.PersonRequest;
import result.AuthResult;
import result.PersonsResult;
import result.PersonResult;
import service.AuthService;
import service.PersonService;
import service.PersonsService;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;

/***
 * Handles /person and /person/[personID] requests.
 */
public class personHandler implements HttpHandler {


    /***
     * Routes to GetPersonService or GetAllPersonsService. Reports result.
     *
     * @param exchange - Exchange object from Server
     */
    @Override
    public void handle(HttpExchange exchange) {
        try {
            try {
                //GET
                if (exchange.getRequestMethod().equalsIgnoreCase("get")) {

                    Headers header = exchange.getRequestHeaders();

                    //AuthToken
                    if (header.containsKey("Authorization")) {

                        //Validate Authtoken
                        String token = header.getFirst("Authorization");
                        AuthRequest aRequest = new AuthRequest(token);
                        AuthService aService = new AuthService();
                        AuthResult aResult = aService.authenticateUser(aRequest);

                        //Get the URL parameters/Setup
                        String urlPath = exchange.getRequestURI().toString();
                        String[] segments = urlPath.split("/");
                        int urlParams = segments.length;
                        Gson gson = new Gson();

                        //ALL USERS
                        if (urlParams == 2) {
                            PersonsResult result;

                            if(!aResult.isSuccess()){
                                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                                result = new PersonsResult(aResult.getMessage(), false);
                            }
                            else{
                                PersonsRequest request = new PersonsRequest(aResult.getPersonID());
                                PersonsService service = new PersonsService();
                                result = service.getAllPersons(request);

                                //Success: 200
                                if(result.isSuccess()){
                                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                                }
                                //Error: 400
                                else{
                                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                                }
                            }

                            Writer respBody = new OutputStreamWriter(exchange.getResponseBody());
                            gson.toJson(result, respBody);
                            respBody.close();
                        }

                        //SPECIFIC USER
                        else if (urlParams == 3) {
                            PersonResult result;

                            if(!aResult.isSuccess()){
                                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                                result = new PersonResult(aResult.getMessage(), false);
                            }
                            else{
                                String personID = segments[2];
                                PersonRequest request = new PersonRequest(personID,aResult.getPersonID());
                                PersonService service = new PersonService();
                                result = service.getPerson(request);

                                //Success: 200
                                if(result.isSuccess()){
                                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                                }
                                //Error: 400
                                else{
                                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                                }
                            }
                            Writer respBody = new OutputStreamWriter(exchange.getResponseBody());
                            gson.toJson(result, respBody);
                            respBody.close();
                        }
                    }
                    //!AuthToken: 400
                    else {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                        exchange.getResponseBody().close();
                    }
                }
                //Faulty Request: 404
                else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
                    exchange.getResponseBody().close();
                }
            }
            //Internal Error: 500
            catch (IOException e) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
                exchange.getResponseBody().close();
                e.printStackTrace();
            }
        }
        //IOException
        catch(IOException e){
            System.out.println("IOException in personHandler: " + e);
            e.printStackTrace();
        }
    }
}
