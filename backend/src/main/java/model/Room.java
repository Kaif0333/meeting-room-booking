package com.kaif.meetingroombooking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("rooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    @Id
    private String id;
    @NotBlank(message = "name is required")
    private String name;
    @NotBlank(message = "location is required")
    private String location;
    @Min(value = 1, message = "capacity must be at least 1")
    private int capacity;
    private String description;
}
