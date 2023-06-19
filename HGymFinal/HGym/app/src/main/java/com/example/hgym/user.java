package com.example.hgym;

import java.util.ArrayList;
//a class for the object user.
public class user {
    protected String firstname;
    protected String lastname;
    protected String itImagePath;
    protected int dayOfBirth;
    protected int monthOfBirth;
    protected int yearOfBirth;
    protected String email;
    protected String roll;
    protected String password;
    protected ArrayList<equipmentExercise> equipmentExercise = new ArrayList<>();
    protected ArrayList<nonEquipmentExercise> nonEquipmentExercise = new ArrayList<>();
    //a builder for the object.
    public user(String firstname, String lastname, int dayOfBirth, int monthOfBirth, int yearOfBirth, String email, String roll, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.dayOfBirth = dayOfBirth;
        this.monthOfBirth = monthOfBirth;
        this.yearOfBirth = yearOfBirth;
        this.email = email;
        this.roll = roll;
        this.password = password;
        this.itImagePath = "";
    }
    //a builder for the object.
    public user(String firstname, String lastname, int dayOfBirth, int monthOfBirth, int yearOfBirth, String email, String roll, String password, String itImagePath) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.dayOfBirth = dayOfBirth;
        this.monthOfBirth = monthOfBirth;
        this.yearOfBirth = yearOfBirth;
        this.email = email;
        this.roll = roll;
        this.password = password;
        this.itImagePath = itImagePath;
    }
    //and empty builder.
    public user(){
        firstname = "";
        lastname = "";
        itImagePath = "";
        email = "";
        roll = "";
        password = "";
        dayOfBirth =0;
        monthOfBirth = 0;
        yearOfBirth = 0;
        equipmentExercise = new ArrayList<>();
        nonEquipmentExercise = new ArrayList<>();
    }
    //a builder for the object.
    public user(String firstname, String lastname, int dayOfBirth, int monthOfBirth, int yearOfBirth, String email, String roll, String password,ArrayList<equipmentExercise> equipmentExercise, ArrayList<nonEquipmentExercise> nonEquipmentExercise) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.dayOfBirth = dayOfBirth;
        this.monthOfBirth = monthOfBirth;
        this.yearOfBirth = yearOfBirth;
        this.email = email;
        this.roll = roll;
        this.password = password;
        this.nonEquipmentExercise=nonEquipmentExercise;
        this.equipmentExercise=equipmentExercise;
        this.itImagePath = "";
    }
    //get the first name.
    public String getFirstname() {
        return firstname;
    }
    //set the first name.
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
    //get the last name.
    public String getLastname() {
        return lastname;
    }
    //set the last name.
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    //get the day of birth.
    public int getDayOfBirth() {
        return dayOfBirth;
    }
    //set the day of birth.
    public void setDayOfBirth(int dayOfBirth) {
        this.dayOfBirth = dayOfBirth;
    }
    //get the month of birth.
    public int getMonthOfBirth() {
        return monthOfBirth;
    }
    //set the month of birth.
    public void setMonthOfBirth(int monthOfBirth) {
        this.monthOfBirth = monthOfBirth;
    }
    //get the year of birth.
    public int getYearOfBirth() {
        return yearOfBirth;
    }
    //set the year of birth.
    public void setYearOfBirth(int yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }
    //get the email.
    public String getEmail() {
        return email;
    }
    //set the email.
    public void setEmail(String email) {
        this.email = email;
    }
    //get the roll.
    public String getRoll() {
        return roll;
    }
    //set the roll.
    public void setRoll(String roll) {
        this.roll = roll;
    }
    //get the password.
    public String getPassword() {
        return password;
    }
    //set the password.
    public void setPassword(String password) {
        this.password = password;
    }
    //get the list of equipment exercises.
    public ArrayList<equipmentExercise> getEquipmentExercise() {
        return equipmentExercise;
    }
    //get the list of no equipment exercises.
    public ArrayList<nonEquipmentExercise> getNonEquipmentExercise() {
        return nonEquipmentExercise;
    }
    //set the list of the equipment exercises.
    public void setEquipmentExercise(ArrayList<equipmentExercise> equipmentExercise) {
        this.equipmentExercise = equipmentExercise;
    }
    //set the list of the no equipment exercises.
    public void setNonEquipmentExercise(ArrayList<nonEquipmentExercise> nonEquipmentExercise) {
        this.nonEquipmentExercise = nonEquipmentExercise;
    }
    //get the image path.
    public String getItImagePath(){
        return itImagePath;
    }
    //set the image path.
    public void setItImagePath(String itImagePath) {
        this.itImagePath = itImagePath;
    }
}
