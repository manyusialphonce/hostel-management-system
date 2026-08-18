package com.hostelms.service;

import com.hostelms.model.AppUser;
import com.hostelms.model.Role;
import com.hostelms.repository.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AppUserService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<AppUser> findAll() {
        return appUserRepository.findAll();
    }

    public AppUser findById(Long id) {
        return appUserRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id " + id));
    }

    public Optional<AppUser> findByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

    /** Creates a new login account (used by an Admin to add other users). Hashes the raw password before saving. */
    public AppUser createUser(String username, String rawPassword, String fullName, Role role) {
        if (appUserRepository.existsByUsername(username)) {
            throw new IllegalStateException("Username '" + username + "' is already taken");
        }
        AppUser user = new AppUser(username, passwordEncoder.encode(rawPassword), fullName, role);
        return appUserRepository.save(user);
    }

    public boolean passwordMatches(AppUser user, String rawPassword) {
        return passwordEncoder.matches(rawPassword, user.getPasswordHash());
    }

    public void deleteById(Long id) {
        appUserRepository.deleteById(id);
    }
}
