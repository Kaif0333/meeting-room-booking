package com.kaif.meetingroombooking.repository;

import com.kaif.meetingroombooking.model.Room;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends MongoRepository<Room, String> { }
