package com.hospital.automation.patient.service;

import com.hospital.automation.patient.dto.PatientCreateRequest;
import com.hospital.automation.patient.dto.PatientResponse;
import com.hospital.automation.patient.dto.PatientUpdateRequest;

import java.util.List;

public interface PatientService {
    PatientResponse create(PatientCreateRequest request);
    PatientResponse getById(Long id);
    List<PatientResponse> getAll();
    PatientResponse update(Long id, PatientUpdateRequest request);
    void delete(Long id);
}
