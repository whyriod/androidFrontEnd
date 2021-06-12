package com.cs240.shared.request;

/***
 * Creates a request to register a new User given a: username, password
 * email, firstName, lastName, and gender. These are found in the reqBody.
 */
public class RegisterRequest {

    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String gender;



    /***
     * Constructor.
     *
     * @param username - New username to register.
     * @param password - Password for new account.
     * @param email - Email for new account.
     * @param firstName - Firstname of user.
     * @param lastName - Lastname of user.
     * @param gender - Sex of user.
     */
    public RegisterRequest(String username, String password, String email, String firstName, String lastName, String gender) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    };

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
