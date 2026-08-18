package com.hostelms.controller;

import com.hostelms.dto.BookingRequest;
import com.hostelms.model.Booking;
import com.hostelms.model.Room;
import com.hostelms.model.Student;
import com.hostelms.model.Warden;
import com.hostelms.service.BookingService;
import com.hostelms.service.RoomService;
import com.hostelms.service.StudentService;
import com.hostelms.service.WardenService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
public class BookingApiController {

    private final BookingService bookingService;
    private final StudentService studentService;
    private final RoomService roomService;
    private final WardenService wardenService;

    public BookingApiController(BookingService bookingService, StudentService studentService,
                                 RoomService roomService, WardenService wardenService) {
        this.bookingService = bookingService;
        this.studentService = studentService;
        this.roomService = roomService;
        this.wardenService = wardenService;
    }

    @GetMapping
    public List<Booking> list() {
        return bookingService.findAll();
    }

    @GetMapping("/{id}")
    public Booking get(@PathVariable Long id) {
        return bookingService.findById(id);
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody BookingRequest request) {
        Student student = studentService.findById(request.getStudentId());
        Room room = roomService.findById(request.getRoomId());

        Booking booking = new Booking(student, room, request.getCheckInDate());
        booking.setCheckOutDate(request.getCheckOutDate());
        booking.setFeeAmount(request.getFeeAmount());
        booking.setFeePaid(request.isFeePaid());

        try {
            Booking saved = bookingService.save(booking);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", ex.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public Booking update(@PathVariable Long id, @Valid @RequestBody BookingRequest request) {
        Booking booking = bookingService.findById(id);
        booking.setStudent(studentService.findById(request.getStudentId()));
        booking.setRoom(roomService.findById(request.getRoomId()));
        booking.setCheckInDate(request.getCheckInDate());
        booking.setCheckOutDate(request.getCheckOutDate());
        booking.setFeeAmount(request.getFeeAmount());
        booking.setFeePaid(request.isFeePaid());
        return bookingService.save(booking);
    }

    @PostMapping("/{id}/approve")
    public Booking approve(@PathVariable Long id, @RequestParam Long wardenId) {
        return bookingService.approve(id, wardenService.findById(wardenId));
    }

    @PostMapping("/{id}/reject")
    public Booking reject(@PathVariable Long id, @RequestParam Long wardenId) {
        return bookingService.reject(id, wardenService.findById(wardenId));
    }

    @PostMapping("/{id}/cancel")
    public Booking cancel(@PathVariable Long id) {
        return bookingService.cancel(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookingService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
