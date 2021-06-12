package com.cs240.shared.result;

/***
 * Creates a ClearResult object to return a message based on db operation.
 */
public class ClearResult {

    private String message;
    private boolean success;



    /***
     * Constructor - Success and Failure.
     *
     * @param message - Success: "Clear succeeded."
     *                  Error:   "Error: ..."
     * @param success - Boolean of success
     */
    public ClearResult(String message, boolean success) {
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
