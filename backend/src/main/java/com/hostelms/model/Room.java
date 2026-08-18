package com.hostelms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;

/**
 * A single room inside a Hostel that Students can book.
 */
@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Room number is required")
    @Column(name = "room_number", nullable = false)
    private String roomNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomType roomType;

    @Positive(message = "Capacity must be greater than zero")
    private int capacity;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomStatus status = RoomStatus.AVAILABLE;

    @NotNull(message = "Hostel is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hostel_id", nullable = false)
    private Hostel hostel;

    @JsonIgnore
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings = new ArrayList<>();

    public Room() {
    }

    public Room(String roomNumber, RoomType roomType, int capacity, Hostel hostel) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.capacity = capacity;
        this.hostel = hostel;
    }

    /** Number of bookings currently occupying the room (approved and not cancelled). */
    @JsonProperty("occupantsCount")
    public long occupantsCount() {
        return bookings.stream()
                .filter(b -> b.getStatus() == BookingStatus.APPROVED)
                .count();
    }

    /** Business method: a room can accept a new booking only if it still has space. */
    @JsonProperty("hasSpace")
    public boolean hasSpace() {
        return occupantsCount() < capacity && status != RoomStatus.UNDER_MAINTENANCE;
    }

    /** Recomputes AVAILABLE/FULL status from current occupancy. */
    public void refreshStatus() {
        if (status == RoomStatus.UNDER_MAINTENANCE) {
            return;
        }
        status = (occupantsCount() >= capacity) ? RoomStatus.FULL : RoomStatus.AVAILABLE;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }

    public Hostel getHostel() {
        return hostel;
    }

    public void setHostel(Hostel hostel) {
        this.hostel = hostel;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
}
