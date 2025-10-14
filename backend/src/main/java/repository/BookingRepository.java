package com.kaif.meetingroombooking.repository;

import com.kaif.meetingroombooking.model.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends MongoRepository<Booking, String> {
    // find bookings where existing.end > requested.start AND existing.start < requested.end (overlap)
    List<Booking> findByRoomIdAndEndAfterAndStartBefore(String roomId, LocalDateTime start, LocalDateTime end);

    List<Booking> findByRoomId(String roomId);
    List<Booking> findByUserId(String userId);
}
