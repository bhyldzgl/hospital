package com.hospital.automation.doctor.service;

import com.hospital.automation.doctor.dto.DoctorCreateRequest;
import com.hospital.automation.doctor.dto.DoctorResponse;
import com.hospital.automation.doctor.dto.DoctorUpdateRequest;
import com.hospital.automation.doctor.dto.TimeSlotResponse;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface DoctorService {
    DoctorResponse create(DoctorCreateRequest request);
    DoctorResponse getById(Long id);
    List<DoctorResponse> getAll();
    Page<DoctorResponse> getPage(int page, int size, String sort);
    DoctorResponse update(Long id, DoctorUpdateRequest request);
    void delete(Long id);

    List<TimeSlotResponse> getAvailability(Long doctorId, LocalDate date, LocalTime workStart, LocalTime workEnd, int slotMinutes);
}
