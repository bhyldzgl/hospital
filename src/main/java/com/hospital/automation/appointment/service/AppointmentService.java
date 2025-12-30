package com.hospital.automation.appointment.service;

import com.hospital.automation.appointment.dto.AppointmentCreateRequest;
import com.hospital.automation.appointment.dto.AppointmentResponse;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentService {
    AppointmentResponse create(AppointmentCreateRequest request);
    AppointmentResponse getById(Long id);

    // eski liste (kalsın)
    List<AppointmentResponse> getAll();

    // yeni sayfalı liste
    Page<AppointmentResponse> search(Long doctorId, Long patientId, LocalDateTime from, LocalDateTime to, int page, int size, String sort);

    void delete(Long id);
}
