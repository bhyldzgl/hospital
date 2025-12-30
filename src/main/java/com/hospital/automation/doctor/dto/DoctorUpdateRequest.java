package com.hospital.automation.doctor.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DoctorUpdateRequest(
        @NotBlank(message = "fullName is required")
        String fullName,

        @NotBlank(message = "specialty is required")
        String specialty,

        @NotBlank(message = "phone is required")
        String phone,

        @NotBlank(message = "email is required")
        @Email(message = "email must be valid")
        String email
) {
}
