
/*
 * *
 *  @author <Raphael Collin> <rapphaelmanhaes2017@hotmail.com>
 *  @copyright (c) 2019
 * /
 */

package com.raphaelcollin.contacts.model;

public class Contact {

    private int idContact = -1;
    private String name;
    private String gender;
    private String phoneNumber;
    private String email;
    private String description;

    public Contact(int idContact, String name, String gender, String phoneNumber, String email, String description) {
        this.idContact = idContact;
        this.name = name;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.description = description;
    }

    public Contact(String name, String gender, String phoneNumber, String email, String description) {
        this (-1, name, gender, phoneNumber, email, description);
    }

    public Contact(int idContact) {
        this (idContact, null, null, null, null, null);
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getDescription() {
        return description;
    }

    public int getIdContact() {
        return idContact;
    }

    public void setIdContact(int idContact) {
        this.idContact = idContact;
    }

    public void setName(String name) {
        this.name = name;
    }
}
