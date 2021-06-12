package com.cs240.server.handler.database;

import java.io.*;
import java.net.*;
import java.nio.file.Files;

import com.sun.net.httpserver.*;

/***
 * Handles / requests
 */
public class fileHandler implements HttpHandler {



    /***
     * Serves the client the webpage (web/index.html)
     *
     * @param exchange - The exchange object passed by the server.
     */
    @Override
    public void handle(HttpExchange exchange){

        try{
            try {
                //GET
                if(exchange.getRequestMethod().equalsIgnoreCase("get")) {

                    //Fix the URl
                    String urlPath = exchange.getRequestURI().toString();

                    if(urlPath.equals("/") || urlPath.equals(null)){
                        urlPath = "/index.html";
                    }
                    String filePath = "web" + urlPath;
                    File file = new File(filePath);

                    //File Found: 200
                    if(file.exists()){
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    }
                    //!File: 404
                    else{
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
                        filePath = "web/HTML/404.html";
                        file = new File(filePath);
                    }

                    OutputStream respBody = exchange.getResponseBody();
                    Files.copy(file.toPath(),respBody);
                    respBody.close();
                }
                //Faulty request: 404
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
            System.out.println("Error: IOException in fileHandler: " + e);
            e.printStackTrace();
        }
    }
}