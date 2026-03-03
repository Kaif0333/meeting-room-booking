package com.kaif.meetingroombooking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document("bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    @Id
    private String id;
    @NotBlank(message = "roomId is required")
    private String roomId;
    @NotBlank(message = "userId is required")
    private String userId;
    @NotNull(message = "start is required")
    private LocalDateTime start;
    @NotNull(message = "end is required")
    private LocalDateTime end;
    @NotBlank(message = "purpose is required")
    private String purpose;
    private List<String> participantIds = new ArrayList<>();
}
