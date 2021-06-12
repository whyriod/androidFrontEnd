package com.cs240.server.handler.database;

import java.io.*;
import java.net.*;

import com.google.gson.Gson;
import com.sun.net.httpserver.*;
import result.ClearResult;
import service.ClearService;

/***
 * Handles /clear requests
 */
public class clearHandler implements HttpHandler {



    /***
     * Routes to ClearService. Reports result
     *
     * @param exchange - The exchange object passed by the server.
     */
    @Override
    public void handle(HttpExchange exchange) {

        try {
            try {
                //POST
                if (exchange.getRequestMethod().equalsIgnoreCase("post")) {
                    //Access the Clear service.
                    ClearService clear = new ClearService();
                    ClearResult result = clear.clearDatabase();

                    //Success: 200
                    if(result.isSuccess()){
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        Writer respBody = new OutputStreamWriter(exchange.getResponseBody());
                        Gson gson = new Gson();
                        gson.toJson(result, respBody);
                        respBody.close();
                    }
                    else{
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
                        exchange.getResponseBody().close();
                    }
                }

                //Faulty request: 404
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
            System.out.println("Error: IOException in clearHandler: " + e);
            e.printStackTrace();
        }
    }
}