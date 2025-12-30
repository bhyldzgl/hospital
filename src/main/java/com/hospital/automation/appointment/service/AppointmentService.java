package com.hospital.automation.appointment.service;

import com.hospital.automation.appointment.dto.AppointmentCreateRequest;
import com.hospital.automation.appointment.dto.AppointmentResponse;
import com.hospital.automation.appointment.dto.AppointmentUpdateRequest;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentService {
    AppointmentResponse create(AppointmentCreateRequest request);
    AppointmentResponse getById(Long id);

    List<AppointmentResponse> getAll();

    Page<AppointmentResponse> search(Long doctorId, Long patientId, LocalDateTime from, LocalDateTime to, int page, int size, String sort);

    AppointmentResponse update(Long id, AppointmentUpdateRequest request);

    void delete(Long id);
}
