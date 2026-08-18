package com.hostelms.controller;

import com.hostelms.dto.HostelRequest;
import com.hostelms.model.Hostel;
import com.hostelms.model.Warden;
import com.hostelms.service.HostelService;
import com.hostelms.service.WardenService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hostels")
public class HostelApiController {

    private final HostelService hostelService;
    private final WardenService wardenService;

    public HostelApiController(HostelService hostelService, WardenService wardenService) {
        this.hostelService = hostelService;
        this.wardenService = wardenService;
    }

    @GetMapping
    public List<Hostel> list() {
        return hostelService.findAll();
    }

    @GetMapping("/{id}")
    public Hostel get(@PathVariable Long id) {
        return hostelService.findById(id);
    }

    @PostMapping
    public ResponseEntity<Hostel> create(@Valid @RequestBody HostelRequest request) {
        Hostel hostel = new Hostel(request.getName(), request.getLocation(), resolveWarden(request.getWardenId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(hostelService.save(hostel));
    }

    @PutMapping("/{id}")
    public Hostel update(@PathVariable Long id, @Valid @RequestBody HostelRequest request) {
        Hostel hostel = hostelService.findById(id);
        hostel.setName(request.getName());
        hostel.setLocation(request.getLocation());
        hostel.setWarden(resolveWarden(request.getWardenId()));
        return hostelService.save(hostel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        hostelService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private Warden resolveWarden(Long wardenId) {
        return wardenId == null ? null : wardenService.findById(wardenId);
    }
}
