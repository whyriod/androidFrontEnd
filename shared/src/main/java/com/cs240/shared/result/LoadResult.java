package com.cs240.shared.result;

/***
 * Creates a LoadResult object to return a message
 * based on db operation.
 */
public class LoadResult {

    private String message;
    private boolean success;



    /***
     * Constructor
     * @param message - Success: “Successfully added X users, Y persons, and Z events to the database.”
     *                  Error:   “Error: [Description of the error]”
     * @param success - Boolean of success
     */
    public LoadResult(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
