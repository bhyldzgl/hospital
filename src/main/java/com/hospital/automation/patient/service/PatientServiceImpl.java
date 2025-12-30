package com.hospital.automation.patient.service;

import com.hospital.automation.common.exception.ConflictException;
import com.hospital.automation.common.exception.ResourceNotFoundException;
import com.hospital.automation.patient.dto.PatientCreateRequest;
import com.hospital.automation.patient.dto.PatientResponse;
import com.hospital.automation.patient.dto.PatientUpdateRequest;
import com.hospital.automation.patient.entity.Patient;
import com.hospital.automation.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    @Override
    public PatientResponse create(PatientCreateRequest request) {
        if (patientRepository.existsByEmail(request.email())) {
            throw new ConflictException("Patient email already exists: " + request.email());
        }

        Patient patient = Patient.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .phone(request.phone())
                .email(request.email())
                .build();

        Patient saved = patientRepository.save(patient);
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PatientResponse getById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found: " + id));
        return toResponse(patient);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PatientResponse> getAll() {
        return patientRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PatientResponse> getPage(int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, parseSort(sort));
        return patientRepository.findAll(pageable).map(this::toResponse);
    }

    @Override
    public PatientResponse update(Long id, PatientUpdateRequest request) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found: " + id));

        if (patientRepository.existsByEmailAndIdNot(request.email(), id)) {
            throw new ConflictException("Patient email already exists: " + request.email());
        }

        patient.setFirstName(request.firstName());
        patient.setLastName(request.lastName());
        patient.setPhone(request.phone());
        patient.setEmail(request.email());

        Patient saved = patientRepository.save(patient);
        return toResponse(saved);
    }

    @Override
    public void delete(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found: " + id));
        patientRepository.delete(patient);
    }

    private Sort parseSort(String sort) {
        if (sort == null || sort.isBlank()) {
            return Sort.by(Sort.Direction.ASC, "lastName");
        }
        String[] parts = sort.split(",");
        String field = parts[0].trim();
        Sort.Direction direction = Sort.Direction.ASC;
        if (parts.length > 1) {
            direction = "desc".equalsIgnoreCase(parts[1].trim()) ? Sort.Direction.DESC : Sort.Direction.ASC;
        }
        return Sort.by(direction, field);
    }

    private PatientResponse toResponse(Patient p) {
        return new PatientResponse(p.getId(), p.getFirstName(), p.getLastName(), p.getPhone(), p.getEmail());
    }
}
