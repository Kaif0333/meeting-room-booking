package com.kaif.meetingroombooking.service;

import com.kaif.meetingroombooking.model.Room;
import com.kaif.meetingroombooking.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {
    private final RoomRepository repo;
    private final AuditLogService auditLogService;

    public RoomService(RoomRepository repo, AuditLogService auditLogService) {
        this.repo = repo;
        this.auditLogService = auditLogService;
    }

    public Room addRoom(Room r) {
        Room saved = repo.save(r);
        auditLogService.log("ROOM_CREATED", "ROOM", saved.getId(), "Room created");
        return saved;
    }
    public List<Room> getAllRooms() { return repo.findAll(); }
    public Optional<Room> getRoom(String id) { return repo.findById(id); }
    public void deleteRoom(String id) {
        repo.deleteById(id);
        auditLogService.log("ROOM_DELETED", "ROOM", id, "Room deleted");
    }
}
