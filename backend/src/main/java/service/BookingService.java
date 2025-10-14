package com.kaif.meetingroombooking.service;

import com.kaif.meetingroombooking.model.Booking;
import com.kaif.meetingroombooking.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public Booking createBooking(Booking booking) {
        if (booking.getStart().isAfter(booking.getEnd()) || booking.getStart().isEqual(booking.getEnd())) {
            throw new IllegalArgumentException("Invalid booking time range");
        }

        List<Booking> conflicts = bookingRepository.findByRoomIdAndEndAfterAndStartBefore(
                booking.getRoomId(), booking.getStart(), booking.getEnd());

        if (!conflicts.isEmpty()) {
            throw new IllegalStateException("Time slot not available for the selected room");
        }
        return bookingRepository.save(booking);
    }

    public Optional<Booking> getBooking(String id) {
        return bookingRepository.findById(id);
    }

    public List<Booking> getBookingsForRoom(String roomId) {
        return bookingRepository.findByRoomId(roomId);
    }

    public List<Booking> getBookingsForUser(String userId) {
        return bookingRepository.findByUserId(userId);
    }

    public void cancelBooking(String id) {
        bookingRepository.deleteById(id);
    }
}
