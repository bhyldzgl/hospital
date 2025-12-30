package com.hospital.automation.doctor.service;

import com.hospital.automation.doctor.dto.DoctorCreateRequest;
import com.hospital.automation.doctor.dto.DoctorResponse;
import com.hospital.automation.doctor.dto.DoctorUpdateRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DoctorService {
    DoctorResponse create(DoctorCreateRequest request);
    DoctorResponse getById(Long id);
    List<DoctorResponse> getAll();
    Page<DoctorResponse> getPage(int page, int size, String sort);
    DoctorResponse update(Long id, DoctorUpdateRequest request);
    void delete(Long id);
}
