package com.csc301.team7.era;

/**
 * Created by MuhammadAun on 2017-03-08.
 * User Profile Class
 */

public class User {

    private String first_name;
    private String last_name;
    private String email;
    private String password;
    private String gender;
    private int age;
    private String address;

    private int contact_number;
    private String emergency_name;
    private int emergency_contact;
    private String medical_condition;

    public User(String first_name, String last_name, String email, String password) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
    }

    public User(String first_name, String last_name, String email, String password, String address,
                int contact_number, int emergency_contact, String medical_condition, int age,
                String gender, String emergency_name) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.contact_number = contact_number;
        this.emergency_contact = emergency_contact;
        this.medical_condition = medical_condition;
        this.address = address;
        this.gender = gender;
        this.emergency_name = emergency_name;
        this.age = age;

    }

    /**
     * Created Getter for first_name
     *
     * @return first_name
     */
    public String getFirst_name() {
        return this.first_name;
    }

    /**
     * Created Getter for last_name
     *
     * @return last_name
     */
    public String getLast_name() {
        return this.last_name;
    }

    /**
     * Created a getter for password
     *
     * @return password
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Created a getter for email
     *
     * @return email
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Created a setter for contacts
     *
     * @param contact
     */
    public void setContact(int contact) {
        this.contact_number = contact;
    }

    /**
     * Created a getter for contact
     *
     * @return contact_number
     */
    public int getContact() {
        return this.contact_number;
    }

    /**
     * Created setter for emergency contact
     *
     * @param number
     */
    public void setEmergencyContact(int number) {
        this.emergency_contact = number;
    }

    /**
     * Created getter for emergency contact
     *
     * @return emergency_contact
     */
    public int getEmergency_contact() {
        return this.emergency_contact;
    }

    /**
     * Created setter for medical condition
     *
     * @param condition
     */
    public void setMedical_condition(String condition) {
        this.medical_condition = condition;
    }

    /**
     * Created getter for medical condition
     *
     * @return medical condition
     */
    public String getMedical_condition() {
        return this.medical_condition;
    }

    /**
     * Created setter for
     */

    public String getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    public String getAddress() {
        return address;
    }

    public int getContact_number() {
        return contact_number;
    }

    public String getEmergency_name() {
        return emergency_name;
    }


}
