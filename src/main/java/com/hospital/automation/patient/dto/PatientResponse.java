package com.hospital.automation.patient.dto;

public record PatientResponse(
        Long id,
        String firstName,
        String lastName,
        String phone,
        String email
) {
}
