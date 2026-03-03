package com.kaif.meetingroombooking.service;

import com.kaif.meetingroombooking.dto.BookingEvent;
import com.kaif.meetingroombooking.model.Booking;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class BookingRealtimeService {
    private final SimpMessagingTemplate messagingTemplate;

    public BookingRealtimeService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void publish(String type, Booking booking) {
        messagingTemplate.convertAndSend("/topic/bookings", new BookingEvent(type, booking));
    }
}
