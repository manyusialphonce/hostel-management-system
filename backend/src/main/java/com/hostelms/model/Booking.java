package com.hostelms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * A Student's request to occupy a Room, reviewed by a Warden.
 * This is the central entity tying the whole system together.
 */
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "booking_date", updatable = false)
    private LocalDateTime bookingDate;

    @NotNull(message = "Check-in date is required")
    @Column(name = "check_in_date", nullable = false)
    private LocalDate checkInDate;

    @Column(name = "check_out_date")
    private LocalDate checkOutDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status = BookingStatus.PENDING;

    @PositiveOrZero(message = "Fee amount cannot be negative")
    @Column(name = "fee_amount", precision = 12, scale = 2)
    private BigDecimal feeAmount = BigDecimal.ZERO;

    @Column(name = "fee_paid", nullable = false)
    private boolean feePaid = false;

    @NotNull(message = "Student is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @NotNull(message = "Room is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private Warden approvedBy;

    public Booking() {
    }

    public Booking(Student student, Room room, LocalDate checkInDate) {
        this.student = student;
        this.room = room;
        this.checkInDate = checkInDate;
        this.status = BookingStatus.PENDING;
    }

    @PrePersist
    protected void onCreate() {
        this.bookingDate = LocalDateTime.now();
    }

    /** Business method: a Warden approves the booking, which also fills the room. */
    public void approve(Warden warden) {
        this.status = BookingStatus.APPROVED;
        this.approvedBy = warden;
        this.room.refreshStatus();
    }

    /** Business method: a Warden rejects the booking. */
    public void reject(Warden warden) {
        this.status = BookingStatus.REJECTED;
        this.approvedBy = warden;
    }

    /** Business method: student or warden cancels a previously approved booking. */
    public void cancel() {
        this.status = BookingStatus.CANCELLED;
        this.room.refreshStatus();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public BigDecimal getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(BigDecimal feeAmount) {
        this.feeAmount = feeAmount;
    }

    public boolean isFeePaid() {
        return feePaid;
    }

    public void setFeePaid(boolean feePaid) {
        this.feePaid = feePaid;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Warden getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(Warden approvedBy) {
        this.approvedBy = approvedBy;
    }
}
