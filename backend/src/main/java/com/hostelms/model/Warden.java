package com.hostelms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * A Warden is the staff member who reviews and approves/rejects Bookings
 * for the hostel(s) they are responsible for. Like Student, it extends
 * the abstract Person class and inherits its shared fields and behaviour.
 */
@Entity
@Table(name = "wardens")
@PrimaryKeyJoinColumn(name = "id")
public class Warden extends Person {

    @NotBlank(message = "Staff number is required")
    @Column(name = "staff_number", nullable = false, unique = true)
    private String staffNumber;

    @Column(name = "office_location")
    private String officeLocation;

    @JsonIgnore
    @OneToMany(mappedBy = "warden")
    private List<Hostel> hostels = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "approvedBy")
    private List<Booking> approvedBookings = new ArrayList<>();

    public Warden() {
        super();
    }

    public Warden(String fullName, String email, String phone, String staffNumber, String officeLocation) {
        super(fullName, email, phone);
        this.staffNumber = staffNumber;
        this.officeLocation = officeLocation;
    }

    @Override
    public String getRole() {
        return "Warden";
    }

    public String getStaffNumber() {
        return staffNumber;
    }

    public void setStaffNumber(String staffNumber) {
        this.staffNumber = staffNumber;
    }

    public String getOfficeLocation() {
        return officeLocation;
    }

    public void setOfficeLocation(String officeLocation) {
        this.officeLocation = officeLocation;
    }

    public List<Hostel> getHostels() {
        return hostels;
    }

    public void setHostels(List<Hostel> hostels) {
        this.hostels = hostels;
    }

    public List<Booking> getApprovedBookings() {
        return approvedBookings;
    }

    public void setApprovedBookings(List<Booking> approvedBookings) {
        this.approvedBookings = approvedBookings;
    }
}
