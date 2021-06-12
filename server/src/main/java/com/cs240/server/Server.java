package com.cs240.server;

import java.io.*;
import java.net.*;
import java.sql.Connection;

import com.sun.net.httpserver.*;
import com.cs240.server.dao.*;
import com.cs240.server.handler.database.clearHandler;
import com.cs240.server.handler.database.fileHandler;
import com.cs240.server.handler.database.fillHandler;
import com.cs240.server.handler.database.loadHandler;
import com.cs240.server.handler.User.eventHandler;
import com.cs240.server.handler.User.loginHandler;
import com.cs240.server.handler.User.personHandler;
import com.cs240.server.handler.User.registerHandler;


/***
 * From Class Example:
 * 13-web-api server example
 */
public class Server {

    private static final int MAX_WAITING_CONNECTIONS = 12;
    private HttpServer server;



    /***
     * Creates the needed server Tables and clears them of old data.
     *
     * @throws DataAccessException - If data cannot be cleared, then die.
     */
    public void setup() throws DataAccessException, ClassNotFoundException {
        final String driver = "org.sqlite.JDBC";
        Class.forName(driver);

        System.out.println("Setting up Server Tables");

        //Set Up Database connections:
        Database db = new Database();
        Connection c = db.getConnection();

        AuthTokenDAO aDAO = new AuthTokenDAO(c);
        EventDAO eDAO = new EventDAO(c);
        PersonDAO pDAO = new PersonDAO(c);
        UserDAO uDAO = new UserDAO(c);

        //Create Tables and clear.
        aDAO.createTable();
        eDAO.createTable();
        pDAO.createTable();
        uDAO.createTable();

        db.closeConnection(true);
    }



    /***
     * Sets Up Java server listening on the port passed in.
     *
     * @param portNumber - The port to listen on.
     */
    private void run(String portNumber) {

        System.out.println("Initializing HTTP Server");

        //Set Up Server
        try {
            server = HttpServer.create(
                    new InetSocketAddress(Integer.parseInt(portNumber)),
                    MAX_WAITING_CONNECTIONS);
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }
        //Something Important...
        server.setExecutor(null);

        System.out.println("Creating contexts");

        //Database Handlers
        server.createContext("/", new fileHandler());
        server.createContext("/clear", new clearHandler());
        server.createContext("/load", new loadHandler());
        server.createContext("/fill", new fillHandler());

        //User Handlers
        server.createContext("/user/login", new loginHandler());
        server.createContext("/user/register", new registerHandler());
        server.createContext("/event", new eventHandler());
        server.createContext("/person", new personHandler());

        System.out.println("Starting server");
        server.start();
        System.out.println("Server started");
    }



    /***
     * Main argument. Setup server tables and then runs server.
     * @param args - Command Line args. The first is the port.
     */
    public static void main(String[] args) {
        try{
            String portNumber = args[0];
            Server server = new Server();
            server.setup();
            server.run(portNumber);
        }
        catch (DataAccessException | ClassNotFoundException e) {
            System.out.println("Unable to setup Server tables. Exiting.");
            e.printStackTrace();
        }
    }
}
