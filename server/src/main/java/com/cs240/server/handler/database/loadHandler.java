package com.cs240.server.handler.database;

import java.io.*;
import java.net.*;

import com.google.gson.Gson;
import com.sun.net.httpserver.*;
import request.LoadRequest;
import result.LoadResult;
import service.LoadService;
import cereal.*;

/***
 * Handles /load request
 */
public class loadHandler implements HttpHandler {



    /***
     * Routes to LoadService. Reports Result;
     *
     * @param exchange - The exchange object passed by the server.
     */
    @Override
    public void handle(HttpExchange exchange) {

        try{
            try {
                //POST
                if(exchange.getRequestMethod().equalsIgnoreCase("post")) {

                    // Get the request body
                    InputStream reqBody = exchange.getRequestBody();
                    Cereal cereal = new Cereal();
                    String reqData = cereal.readString(reqBody);

                    //Create the request and attempt the service
                    Gson gson = new Gson();
                    LoadRequest request = gson.fromJson(reqData,LoadRequest.class);
                    LoadService load = new LoadService();
                    LoadResult result = load.loadDatabase(request);

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
                else{
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
                    exchange.getResponseBody().close();
                }
            }
            //Internal Error: 500
            catch(IOException e) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
                exchange.getResponseBody().close();
                e.printStackTrace();
            }
        }
        //IOException
        catch(IOException e){
            System.out.println("IOException in loadHandler: " + e);
            e.printStackTrace();
        }
    }
}