package com.cs240.server.handler.database;

import com.google.gson.Gson;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import request.FillRequest;
import result.*;
import service.FillService;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;

/***
 * Handles /fill/username and /fill/username/generations
 */
public class fillHandler implements HttpHandler {


    /***
     * Routes to fill service. Reports result.
     *
     * @param exchange - Exchange object from Server
     */
    @Override
    public void handle(HttpExchange exchange) {
        try {
            try {
                //GET
                if (exchange.getRequestMethod().equalsIgnoreCase("post")) {

                    //Get the URL parameters/Setup
                    String urlPath = exchange.getRequestURI().toString();
                    String[] segments = urlPath.split("/");
                    int urlParams = segments.length;
                    Gson gson = new Gson();
                    int gen;

                    //Gen passed in URL
                    if (urlParams == 3) {
                        gen = 4;
                    }
                    //Default generations 4
                    else {
                        gen = Integer.parseInt(segments[3]);
                    }

                    String username = segments[2];

                    FillRequest request = new FillRequest(username,gen);
                    FillService service = new FillService();
                    FillResult result = service.fillDatabase(request);

                    //Success: 200
                    if(result.isSuccess()){
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    }
                    //Error: 400
                    else{
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                    }
                    Writer respBody = new OutputStreamWriter(exchange.getResponseBody());
                    gson.toJson(result, respBody);
                    respBody.close();
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
            System.out.println("IOException in fillHandler: " + e);
            e.printStackTrace();
        }
    }
}
