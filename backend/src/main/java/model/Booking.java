package com.kaif.meetingroombooking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    @Id
    private String id;
    private String roomId;
    private String userId;
    private LocalDateTime start;
    private LocalDateTime end;
    private String purpose;
}
