package com.hostelms.service;

import com.hostelms.model.Booking;
import com.hostelms.model.Room;
import com.hostelms.model.Warden;
import com.hostelms.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }

    public Booking findById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with id " + id));
    }

    /**
     * Creates a new booking, but only if the target room still has space -
     * enforcing the business rule at the service layer as well as in Room itself.
     */
    public Booking save(Booking booking) {
        Room room = booking.getRoom();
        if (booking.getId() == null && !room.hasSpace()) {
            throw new IllegalStateException("Room " + room.getRoomNumber() + " has no available space");
        }
        return bookingRepository.save(booking);
    }

    public Booking approve(Long bookingId, Warden warden) {
        Booking booking = findById(bookingId);
        booking.approve(warden);
        return bookingRepository.save(booking);
    }

    public Booking reject(Long bookingId, Warden warden) {
        Booking booking = findById(bookingId);
        booking.reject(warden);
        return bookingRepository.save(booking);
    }

    public Booking cancel(Long bookingId) {
        Booking booking = findById(bookingId);
        booking.cancel();
        return bookingRepository.save(booking);
    }

    public void deleteById(Long id) {
        bookingRepository.deleteById(id);
    }
}
