package com.hostelms.repository;

import com.hostelms.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsByRegistrationNumber(String registrationNumber);
}
