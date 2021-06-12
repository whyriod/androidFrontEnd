package com.cs240.server.handler.User;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import request.*;
import result.*;
import service.*;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;

/***
 * Handles /event and /event/[eventID] requests.
 */
public class eventHandler implements HttpHandler {


    /***
     * Routes to GetEventService or GetAllEventsService. Reports result.
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

                        //ALL Events
                        if (urlParams == 2) {
                            EventsResult result;

                            if(!aResult.isSuccess()){
                                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                                result = new EventsResult(aResult.getMessage(), false);
                            }
                            else{
                                EventsRequest request = new EventsRequest(aResult.getPersonID());
                                EventsService service = new EventsService();
                                result = service.getAllEvents(request);

                                //Success:200
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

                        //Single Event
                        else if (urlParams == 3) {
                            EventResult result;

                            if(!aResult.isSuccess()){
                                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                                result = new EventResult(aResult.getMessage(), false);
                            }
                            else{
                                String eventID = segments[2];
                                EventRequest request = new EventRequest(eventID,aResult.getPersonID());
                                EventService service = new EventService();
                                result = service.getEvent(request);

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
            System.out.println("IOException in eventHandler: " + e);
            e.printStackTrace();
        }
    }
}
