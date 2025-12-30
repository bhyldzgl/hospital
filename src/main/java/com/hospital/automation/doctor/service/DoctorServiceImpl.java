package com.hospital.automation.doctor.service;

import com.hospital.automation.common.exception.ResourceNotFoundException;
import com.hospital.automation.doctor.dto.DoctorCreateRequest;
import com.hospital.automation.doctor.dto.DoctorResponse;
import com.hospital.automation.doctor.dto.DoctorUpdateRequest;
import com.hospital.automation.doctor.entity.Doctor;
import com.hospital.automation.doctor.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;

    @Override
    public DoctorResponse create(DoctorCreateRequest request) {
        Doctor doctor = Doctor.builder()
                .fullName(request.fullName())
                .specialty(request.specialty())
                .phone(request.phone())
                .email(request.email())
                .build();

        Doctor saved = doctorRepository.save(doctor);
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public DoctorResponse getById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found: " + id));
        return toResponse(doctor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorResponse> getAll() {
        return doctorRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public DoctorResponse update(Long id, DoctorUpdateRequest request) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found: " + id));

        doctor.setFullName(request.fullName());
        doctor.setSpecialty(request.specialty());
        doctor.setPhone(request.phone());
        doctor.setEmail(request.email());

        Doctor saved = doctorRepository.save(doctor);
        return toResponse(saved);
    }

    @Override
    public void delete(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found: " + id));
        doctorRepository.delete(doctor);
    }

    private DoctorResponse toResponse(Doctor d) {
        return new DoctorResponse(d.getId(), d.getFullName(), d.getSpecialty(), d.getPhone(), d.getEmail());
    }
}
