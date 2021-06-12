package com.cs240.shared.model;


/***
 * The User class is a representation of the User sqlite table.
 */
public class User {

    private String personID;
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String gender;



    /***
     * Constructor
     * @param personID - ID of the user
     * @param userName - Username of user
     * @param password - Password of user.
     * @param email - Email of user.
     * @param firstName - First name of user.
     * @param lastName - Last name of user.
     * @param gender - sex of user.
     */
    public User(String personID, String userName, String password, String email, String firstName, String lastName, String gender){
        this.personID = personID;
        this.username = userName;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

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
