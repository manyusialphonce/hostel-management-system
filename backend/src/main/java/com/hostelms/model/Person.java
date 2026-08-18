package com.hostelms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * Abstract base class for every person in the system.
 *
 * This is the root of the OOP inheritance hierarchy: {@link Student} and
 * {@link Warden} both extend Person and inherit its fields and behaviour.
 * Because it is declared abstract, Person can never be instantiated or
 * saved directly - only through one of its concrete subclasses.
 *
 * Table-per-subclass ("JOINED") inheritance is used: Hibernate creates a
 * "persons" table for the shared columns, plus a "students" table and a
 * "wardens" table for the columns that are specific to each subclass,
 * joined back to "persons" on id.
 */
@Entity
@Table(name = "persons")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Full name is required")
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Phone number is required")
    @Column(nullable = false)
    private String phone;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    protected Person() {
    }

    protected Person(String fullName, String email, String phone) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Every concrete subclass must describe what role it plays in the
     * system ("Student" or "Warden"). Because it is abstract here, each
     * subclass is forced to provide its own implementation - a classic
     * example of polymorphism working alongside inheritance.
     */
    public abstract String getRole();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return getRole() + "{id=" + id + ", fullName='" + fullName + "'}";
    }
}
