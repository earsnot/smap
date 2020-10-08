package com.earsnot.rooms.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Person {
    @PrimaryKey(autoGenerate = true)
    private int uid;

    private String firstName, lastName, email;

    public Person(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = firstName + "." + lastName +"@jubii.dk";
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
