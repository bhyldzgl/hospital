package com.hospital.automation.appointment.dto;

import java.time.LocalDateTime;

public record AppointmentResponse(
        Long id,
        Long patientId,
        Long doctorId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String notes
) {
}
