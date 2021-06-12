package com.cs240.shared.request;

/***
 * Creates a request object for fill requests. Parameters found in the url passed in.
 */
public class FillRequest {

    private String username;
    private int generations;



    /***
     * Constructor.
     *
     * @param username - Found in URL
     * @param generations - Found in URL after username, or blank. Default to 4
     */
    public FillRequest(String username, int generations) {
        this.username = username;
        this.generations = generations;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getGenerations() {
        return generations;
    }

    public void setGenerations(int generations) {
        this.generations = generations;
    }
}
