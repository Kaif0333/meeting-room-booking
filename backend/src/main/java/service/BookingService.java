package com.kaif.meetingroombooking.service;

import com.kaif.meetingroombooking.model.Booking;
import com.kaif.meetingroombooking.repository.BookingRepository;
import com.kaif.meetingroombooking.repository.RoomRepository;
import com.kaif.meetingroombooking.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final AuditLogService auditLogService;
    private final BookingRealtimeService bookingRealtimeService;

    public BookingService(
            BookingRepository bookingRepository,
            UserRepository userRepository,
            RoomRepository roomRepository,
            AuditLogService auditLogService,
            BookingRealtimeService bookingRealtimeService
    ) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.auditLogService = auditLogService;
        this.bookingRealtimeService = bookingRealtimeService;
    }

    public Booking createBooking(Booking booking) {
        if (booking == null) {
            throw new IllegalArgumentException("Booking payload is required");
        }

        if (isBlank(booking.getRoomId()) || isBlank(booking.getUserId()) || booking.getStart() == null || booking.getEnd() == null) {
            throw new IllegalArgumentException("roomId, userId, start, and end are required");
        }

        if (!roomRepository.existsById(booking.getRoomId())) {
            throw new IllegalArgumentException("Room not found");
        }

        if (!userRepository.existsById(booking.getUserId())) {
            throw new IllegalArgumentException("User not found");
        }

        if (booking.getStart().isAfter(booking.getEnd()) || booking.getStart().isEqual(booking.getEnd())) {
            throw new IllegalArgumentException("Invalid booking time range");
        }

        List<Booking> conflicts = bookingRepository.findByRoomIdAndEndAfterAndStartBefore(
                booking.getRoomId(), booking.getStart(), booking.getEnd());

        if (!conflicts.isEmpty()) {
            throw new IllegalStateException("Time slot not available for the selected room");
        }

        if (booking.getStart().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Booking start time must be in the future");
        }

        List<String> participants = booking.getParticipantIds();
        if (participants == null) {
            participants = new ArrayList<>();
        }
        if (!participants.contains(booking.getUserId())) {
            participants.add(booking.getUserId());
        }
        booking.setParticipantIds(participants);

        Booking saved = bookingRepository.save(booking);
        auditLogService.log("BOOKING_CREATED", "BOOKING", saved.getId(), "Meeting booked");
        bookingRealtimeService.publish("BOOKING_CREATED", saved);
        return saved;
    }

    public Optional<Booking> getBooking(String id) {
        return bookingRepository.findById(id);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public List<Booking> getBookingsForRoom(String roomId) {
        return bookingRepository.findByRoomId(roomId);
    }

    public List<Booking> getBookingsForUser(String userId) {
        return bookingRepository.findByUserId(userId);
    }

    public void cancelBooking(String id) {
        Booking existing = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        bookingRepository.deleteById(id);
        auditLogService.log("BOOKING_CANCELED", "BOOKING", id, "Meeting canceled");
        bookingRealtimeService.publish("BOOKING_CANCELED", existing);
    }

    public Booking joinBooking(String bookingId, String userId) {
        if (isBlank(userId)) {
            throw new IllegalArgumentException("userId is required");
        }

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found");
        }

        if (booking.getEnd().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Cannot join a meeting that has already ended");
        }

        List<String> participants = booking.getParticipantIds();
        if (participants == null) {
            participants = new ArrayList<>();
        }
        if (!participants.contains(booking.getUserId())) {
            participants.add(booking.getUserId());
        }
        if (!participants.contains(userId)) {
            participants.add(userId);
        }
        booking.setParticipantIds(participants);

        Booking saved = bookingRepository.save(booking);
        auditLogService.log("MEETING_JOINED", "BOOKING", bookingId, "User joined meeting");
        bookingRealtimeService.publish("MEETING_JOINED", saved);
        return saved;
    }

    public Booking leaveBooking(String bookingId, String userId) {
        if (isBlank(userId)) {
            throw new IllegalArgumentException("userId is required");
        }

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        if (booking.getUserId().equals(userId)) {
            throw new IllegalStateException("Organizer cannot leave their own meeting");
        }

        List<String> participants = booking.getParticipantIds();
        if (participants == null) {
            participants = new ArrayList<>();
        }
        participants.remove(userId);
        booking.setParticipantIds(participants);

        Booking saved = bookingRepository.save(booking);
        auditLogService.log("MEETING_LEFT", "BOOKING", bookingId, "User left meeting");
        bookingRealtimeService.publish("MEETING_LEFT", saved);
        return saved;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
