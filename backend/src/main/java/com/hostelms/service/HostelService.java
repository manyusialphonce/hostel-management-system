package com.hostelms.service;

import com.hostelms.model.Hostel;
import com.hostelms.repository.HostelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HostelService {

    private final HostelRepository hostelRepository;

    public HostelService(HostelRepository hostelRepository) {
        this.hostelRepository = hostelRepository;
    }

    public List<Hostel> findAll() {
        return hostelRepository.findAll();
    }

    public Hostel findById(Long id) {
        return hostelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Hostel not found with id " + id));
    }

    public Hostel save(Hostel hostel) {
        return hostelRepository.save(hostel);
    }

    public void deleteById(Long id) {
        hostelRepository.deleteById(id);
    }
}
