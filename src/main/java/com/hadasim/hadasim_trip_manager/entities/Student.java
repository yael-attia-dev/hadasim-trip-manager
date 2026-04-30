package com.hadasim.hadasim_trip_manager.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
public class Student {

    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String classroom;

    public Student(String id, String firstName, String lastName, String classroom) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.classroom = classroom;
    }

    public Student() {
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
