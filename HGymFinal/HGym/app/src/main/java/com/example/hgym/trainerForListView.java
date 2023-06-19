package com.example.hgym;
//a class for the object trainer for list view.
public class trainerForListView {
    private String firstName;
    private String LastName;
    private String Email;
    //a builder for the object.
    public trainerForListView(String firstName, String lastName, String Email){
        this.Email=Email;
        this.firstName=firstName;
        this.LastName=lastName;
    }
    //get the email.
    public String getEmail() {
        return Email;
    }
    //get the first name.
    public String getFirstName() {
        return firstName;
    }
    //get the last name.
    public String getLastName() {
        return LastName;
    }
    //set the email.
    public void setEmail(String email) {
        Email = email;
    }
    //set the first name.
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    //set the last name.
    public void setLastName(String lastName) {
        LastName = lastName;
    }
}
