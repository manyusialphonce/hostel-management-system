package com.hostelms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * A Student is the person who applies for and holds room Bookings.
 * It inherits id, fullName, email, phone and createdAt from {@link Person}
 * and adds fields that only make sense for a student.
 */
@Entity
@Table(name = "students")
@PrimaryKeyJoinColumn(name = "id")
public class Student extends Person {

    @NotBlank(message = "Registration number is required")
    @Column(name = "registration_number", nullable = false, unique = true)
    private String registrationNumber;

    private String programme;

    private String gender;

    @JsonIgnore
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Booking> bookings = new ArrayList<>();

    public Student() {
        super();
    }

    public Student(String fullName, String email, String phone,
                   String registrationNumber, String programme, String gender) {
        super(fullName, email, phone);
        this.registrationNumber = registrationNumber;
        this.programme = programme;
        this.gender = gender;
    }

    @Override
    public String getRole() {
        return "Student";
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getProgramme() {
        return programme;
    }

    public void setProgramme(String programme) {
        this.programme = programme;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
}
