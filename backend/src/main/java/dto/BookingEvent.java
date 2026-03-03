package com.kaif.meetingroombooking.dto;

import com.kaif.meetingroombooking.model.Booking;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookingEvent {
    private String type;
    private Booking booking;
}
