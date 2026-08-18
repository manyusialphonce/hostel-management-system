package com.hostelms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * A physical hostel building, managed by a Warden and made up of many Rooms.
 */
@Entity
@Table(name = "hostels")
public class Hostel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Hostel name is required")
    @Column(nullable = false, unique = true)
    private String name;

    private String location;

    @Column(name = "total_rooms")
    private int totalRooms;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warden_id")
    private Warden warden;

    @JsonIgnore
    @OneToMany(mappedBy = "hostel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Room> rooms = new ArrayList<>();

    public Hostel() {
    }

    public Hostel(String name, String location, Warden warden) {
        this.name = name;
        this.location = location;
        this.warden = warden;
    }

    /** Business method: how many rooms in this hostel are currently marked AVAILABLE. */
    @JsonProperty("availableRoomsCount")
    public long availableRoomsCount() {
        return rooms.stream().filter(r -> r.getStatus() == RoomStatus.AVAILABLE).count();
    }

    /** Business method: total bed capacity across every room in this hostel. */
    @JsonProperty("totalCapacity")
    public int totalCapacity() {
        return rooms.stream().mapToInt(Room::getCapacity).sum();
    }

    /** Business method: how many beds are currently occupied by approved bookings. */
    @JsonProperty("totalOccupants")
    public long totalOccupants() {
        return rooms.stream().mapToLong(Room::occupantsCount).sum();
    }

    /** Business method: how many beds are still free across the whole hostel. */
    @JsonProperty("availableSpaces")
    public long availableSpaces() {
        return totalCapacity() - totalOccupants();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getTotalRooms() {
        return totalRooms;
    }

    public void setTotalRooms(int totalRooms) {
        this.totalRooms = totalRooms;
    }

    public Warden getWarden() {
        return warden;
    }

    public void setWarden(Warden warden) {
        this.warden = warden;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    @Override
    public String toString() {
        return name;
    }
}
