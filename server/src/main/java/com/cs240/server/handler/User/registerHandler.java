package com.cs240.server.handler.User;

import cereal.Cereal;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import request.RegisterRequest;
import result.RegisterResult;
import service.RegisterService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;

/***
 * Handles /user/register requests
 */
public class registerHandler implements HttpHandler {



    /***
     * Routes to RegisterService. Reports result.
     *
     * @param exchange - Exchange object from Server
     */
    @Override
    public void handle(HttpExchange exchange) {

        try {
            try {
                //POST
                if (exchange.getRequestMethod().equalsIgnoreCase("post")) {

                    // Get the request body
                    InputStream reqBody = exchange.getRequestBody();
                    Cereal cereal = new Cereal();
                    String reqData = cereal.readString(reqBody);

                    //Create the request and attempt the service
                    Gson gson = new Gson();
                    RegisterRequest request = gson.fromJson(reqData, RegisterRequest.class);
                    RegisterService register = new RegisterService();
                    RegisterResult result = register.registerUser(request);

                    //Register: 200
                    if(result.isSuccess()){
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    }
                    //Register: 400
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
            System.out.println("IOException in registerHandler: " + e);
            e.printStackTrace();
        }
    }
}