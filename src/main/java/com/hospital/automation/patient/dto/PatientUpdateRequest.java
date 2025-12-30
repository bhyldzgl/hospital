package com.hospital.automation.patient.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record PatientUpdateRequest(
        @NotBlank(message = "firstName is required")
        String firstName,

        @NotBlank(message = "lastName is required")
        String lastName,

        @NotBlank(message = "phone is required")
        String phone,

        @NotBlank(message = "email is required")
        @Email(message = "email must be valid")
        String email
) {
}
