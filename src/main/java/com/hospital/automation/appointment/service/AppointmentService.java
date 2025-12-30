package com.hospital.automation.appointment.service;

import com.hospital.automation.appointment.dto.AppointmentCreateRequest;
import com.hospital.automation.appointment.dto.AppointmentResponse;

import java.util.List;

public interface AppointmentService {
    AppointmentResponse create(AppointmentCreateRequest request);
    AppointmentResponse getById(Long id);
    List<AppointmentResponse> getAll();
    void delete(Long id);
}
