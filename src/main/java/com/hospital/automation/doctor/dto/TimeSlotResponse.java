package com.hospital.automation.doctor.dto;

import java.time.LocalDateTime;

public record TimeSlotResponse(
        LocalDateTime start,
        LocalDateTime end
) {
}
