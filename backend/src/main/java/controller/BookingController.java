package com.kaif.meetingroombooking.controller;

import com.kaif.meetingroombooking.model.Booking;
import com.kaif.meetingroombooking.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService service;
    public BookingController(BookingService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody Booking booking) {
        try {
            Booking b = service.createBooking(booking);
            return ResponseEntity.ok(b);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBooking(@PathVariable String id) {
        return service.getBooking(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<?>> bookingsForRoom(@PathVariable String roomId) {
        return ResponseEntity.ok(service.getBookingsForRoom(roomId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<?>> bookingsForUser(@PathVariable String userId) {
        return ResponseEntity.ok(service.getBookingsForUser(userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancel(@PathVariable String id) {
        service.cancelBooking(id);
        return ResponseEntity.noContent().build();
    }
}
