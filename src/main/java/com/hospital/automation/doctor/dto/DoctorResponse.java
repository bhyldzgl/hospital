package com.hospital.automation.doctor.dto;

public record DoctorResponse(
        Long id,
        String fullName,
        String specialty,
        String phone,
        String email
) {
}
