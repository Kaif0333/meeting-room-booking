package com.kaif.meetingroombooking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("rooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    @Id
    private String id;
    private String name;
    private String location;
    private int capacity;
    private String description;
}
