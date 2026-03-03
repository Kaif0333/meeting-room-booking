package com.kaif.meetingroombooking.controller;

import com.kaif.meetingroombooking.model.Room;
import com.kaif.meetingroombooking.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {
    private final RoomService service;
    public RoomController(RoomService service) { this.service = service; }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Room> create(@Valid @RequestBody Room room) {
        return ResponseEntity.ok(service.addRoom(room));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Room>> list() {
        return ResponseEntity.ok(service.getAllRooms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> get(@PathVariable String id) {
        return service.getRoom(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }
}
