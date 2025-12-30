package com.hospital.automation.appointment.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AppointmentUpdateRequest(
        @NotNull(message = "patientId is required")
        Long patientId,

        @NotNull(message = "doctorId is required")
        Long doctorId,

        @NotNull(message = "startTime is required")
        LocalDateTime startTime,

        @NotNull(message = "endTime is required")
        LocalDateTime endTime,

        String notes
) {
}
