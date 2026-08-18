package com.hostelms;

import com.hostelms.model.Role;
import com.hostelms.repository.AppUserRepository;
import com.hostelms.service.AppUserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Entry point of the Hostel Management System.
 *
 * Starts an embedded Tomcat server, connects to PostgreSQL using the
 * settings in application.properties, and serves both the REST API and
 * the built React frontend (from src/main/resources/static) on one port.
 */
@SpringBootApplication
public class HostelmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(HostelmsApplication.class, args);
    }

    /**
     * Creates a default "admin" login account the very first time the app
     * runs against an empty database, so there is always a way to log in
     * and start adding real users afterwards.
     *
     * Default credentials: username "admin", password "admin123".
     * Change this password immediately after your first login.
     */
    @Bean
    public CommandLineRunner seedDefaultAdmin(AppUserRepository appUserRepository, AppUserService appUserService) {
        return args -> {
            if (appUserRepository.count() == 0) {
                appUserService.createUser("admin", "admin123", "System Administrator", Role.ADMIN);
                System.out.println("Created default admin account -> username: admin / password: admin123");
            }
        };
    }
}
