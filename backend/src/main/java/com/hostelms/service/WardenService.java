package com.hostelms.service;

import com.hostelms.model.Warden;
import com.hostelms.repository.WardenRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WardenService {

    private final WardenRepository wardenRepository;

    public WardenService(WardenRepository wardenRepository) {
        this.wardenRepository = wardenRepository;
    }

    public List<Warden> findAll() {
        return wardenRepository.findAll();
    }

    public Warden findById(Long id) {
        return wardenRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Warden not found with id " + id));
    }

    public Warden save(Warden warden) {
        return wardenRepository.save(warden);
    }

    public void deleteById(Long id) {
        wardenRepository.deleteById(id);
    }
}
