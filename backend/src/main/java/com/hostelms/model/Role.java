package com.hostelms.model;

/**
 * Roles for system login accounts (AppUser), not to be confused with the
 * domain "roles" of Student/Warden. ADMIN can manage other accounts;
 * STAFF is a regular logged-in user (e.g. a warden using the system).
 */
public enum Role {
    ADMIN,
    STAFF
}
