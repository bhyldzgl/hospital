package com.hospital.automation.patient.service;

import com.hospital.automation.patient.dto.PatientCreateRequest;
import com.hospital.automation.patient.dto.PatientResponse;
import com.hospital.automation.patient.dto.PatientUpdateRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PatientService {
    PatientResponse create(PatientCreateRequest request);
    PatientResponse getById(Long id);
    List<PatientResponse> getAll();
    Page<PatientResponse> getPage(int page, int size, String sort);
    PatientResponse update(Long id, PatientUpdateRequest request);
    void delete(Long id);
}
