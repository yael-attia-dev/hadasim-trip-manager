package com.hadasim.hadasim_trip_manager.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Teacher {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String classroom;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }
}
