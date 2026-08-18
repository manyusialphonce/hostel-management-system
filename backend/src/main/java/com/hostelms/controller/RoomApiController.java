package com.hostelms.controller;

import com.hostelms.dto.RoomRequest;
import com.hostelms.model.Hostel;
import com.hostelms.model.Room;
import com.hostelms.model.RoomStatus;
import com.hostelms.service.HostelService;
import com.hostelms.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomApiController {

    private final RoomService roomService;
    private final HostelService hostelService;

    public RoomApiController(RoomService roomService, HostelService hostelService) {
        this.roomService = roomService;
        this.hostelService = hostelService;
    }

    @GetMapping
    public List<Room> list() {
        return roomService.findAll();
    }

    @GetMapping("/{id}")
    public Room get(@PathVariable Long id) {
        return roomService.findById(id);
    }

    @PostMapping
    public ResponseEntity<Room> create(@Valid @RequestBody RoomRequest request) {
        Hostel hostel = hostelService.findById(request.getHostelId());
        Room room = new Room(request.getRoomNumber(), request.getRoomType(), request.getCapacity(), hostel);
        if (request.getStatus() != null) {
            room.setStatus(request.getStatus());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(roomService.save(room));
    }

    @PutMapping("/{id}")
    public Room update(@PathVariable Long id, @Valid @RequestBody RoomRequest request) {
        Room room = roomService.findById(id);
        room.setRoomNumber(request.getRoomNumber());
        room.setRoomType(request.getRoomType());
        room.setCapacity(request.getCapacity());
        room.setHostel(hostelService.findById(request.getHostelId()));
        room.setStatus(request.getStatus() != null ? request.getStatus() : RoomStatus.AVAILABLE);
        return roomService.save(room);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        roomService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
