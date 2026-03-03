package com.kaif.meetingroombooking.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class JoinMeetingRequest {
    @NotBlank(message = "userId is required")
    private String userId;
}
