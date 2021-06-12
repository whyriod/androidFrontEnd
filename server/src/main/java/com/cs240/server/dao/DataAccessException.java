package com.cs240.server.dao;

/***
 * Custom Exception for my Database operations.
 */
public class DataAccessException extends Exception {

    /***
     * Constructor with message.
     * @param message - Error Message.
     */
    DataAccessException(String message)
        {
            super(message);
        }



    /***
     * Default Constructor.
     */
    DataAccessException()
        {
            super();
        }
}

