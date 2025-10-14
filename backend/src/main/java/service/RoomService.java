package com.kaif.meetingroombooking.service;

import com.kaif.meetingroombooking.model.Room;
import com.kaif.meetingroombooking.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {
    private final RoomRepository repo;
    public RoomService(RoomRepository repo) { this.repo = repo; }

    public Room addRoom(Room r) { return repo.save(r); }
    public List<Room> getAllRooms() { return repo.findAll(); }
    public Optional<Room> getRoom(String id) { return repo.findById(id); }
    public void deleteRoom(String id) { repo.deleteById(id); }
}
