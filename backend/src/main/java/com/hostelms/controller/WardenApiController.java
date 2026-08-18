package com.hostelms.controller;

import com.hostelms.model.Warden;
import com.hostelms.service.WardenService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wardens")
public class WardenApiController {

    private final WardenService wardenService;

    public WardenApiController(WardenService wardenService) {
        this.wardenService = wardenService;
    }

    @GetMapping
    public List<Warden> list() {
        return wardenService.findAll();
    }

    @GetMapping("/{id}")
    public Warden get(@PathVariable Long id) {
        return wardenService.findById(id);
    }

    @PostMapping
    public ResponseEntity<Warden> create(@Valid @RequestBody Warden warden) {
        return ResponseEntity.status(HttpStatus.CREATED).body(wardenService.save(warden));
    }

    @PutMapping("/{id}")
    public Warden update(@PathVariable Long id, @Valid @RequestBody Warden warden) {
        warden.setId(id);
        return wardenService.save(warden);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        wardenService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
