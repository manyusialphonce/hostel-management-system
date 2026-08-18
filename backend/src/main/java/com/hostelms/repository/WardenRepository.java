package com.hostelms.repository;

import com.hostelms.model.Warden;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WardenRepository extends JpaRepository<Warden, Long> {
    boolean existsByStaffNumber(String staffNumber);
}
