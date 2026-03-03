package com.kaif.meetingroombooking.controller;

import com.kaif.meetingroombooking.model.Booking;
import com.kaif.meetingroombooking.service.BookingService;
import com.kaif.meetingroombooking.dto.JoinMeetingRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService service;
    public BookingController(BookingService service) { this.service = service; }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Booking> createBooking(@Valid @RequestBody Booking booking) {
        Booking savedBooking = service.createBooking(booking);
        return ResponseEntity.ok(savedBooking);
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Booking>> listBookings() {
        return ResponseEntity.ok(service.getAllBookings());
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getBooking(@PathVariable String id) {
        return service.getBooking(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/room/{roomId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<?>> bookingsForRoom(@PathVariable String roomId) {
        return ResponseEntity.ok(service.getBookingsForRoom(roomId));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<?>> bookingsForUser(@PathVariable String userId) {
        return ResponseEntity.ok(service.getBookingsForUser(userId));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> cancel(@PathVariable String id) {
        service.cancelBooking(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/join")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Booking> joinMeeting(@PathVariable String id, @Valid @RequestBody JoinMeetingRequest request) {
        return ResponseEntity.ok(service.joinBooking(id, request.getUserId()));
    }

    @PostMapping("/{id}/leave")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Booking> leaveMeeting(@PathVariable String id, @Valid @RequestBody JoinMeetingRequest request) {
        return ResponseEntity.ok(service.leaveBooking(id, request.getUserId()));
    }
}
