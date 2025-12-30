package com.hospital.automation.appointment.service;

import com.hospital.automation.appointment.dto.AppointmentCreateRequest;
import com.hospital.automation.appointment.dto.AppointmentResponse;
import com.hospital.automation.appointment.entity.Appointment;
import com.hospital.automation.appointment.repository.AppointmentRepository;
import com.hospital.automation.common.exception.ConflictException;
import com.hospital.automation.common.exception.ResourceNotFoundException;
import com.hospital.automation.doctor.entity.Doctor;
import com.hospital.automation.doctor.repository.DoctorRepository;
import com.hospital.automation.patient.entity.Patient;
import com.hospital.automation.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    @Override
    public AppointmentResponse create(AppointmentCreateRequest request) {
        validateTimeRange(request.startTime(), request.endTime());

        Patient patient = patientRepository.findById(request.patientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found: " + request.patientId()));

        Doctor doctor = doctorRepository.findById(request.doctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found: " + request.doctorId()));

        // Doktor çakışma kontrolü
        boolean doctorOverlap = appointmentRepository.existsOverlappingForDoctor(
                doctor.getId(),
                request.startTime(),
                request.endTime()
        );
        if (doctorOverlap) {
            throw new ConflictException("Doctor has another appointment overlapping with the given time range.");
        }

        // Hasta çakışma kontrolü
        boolean patientOverlap = appointmentRepository.existsOverlappingForPatient(
                patient.getId(),
                request.startTime(),
                request.endTime()
        );
        if (patientOverlap) {
            throw new ConflictException("Patient has another appointment overlapping with the given time range.");
        }

        Appointment appointment = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .startTime(request.startTime())
                .endTime(request.endTime())
                .notes(request.notes())
                .build();

        Appointment saved = appointmentRepository.save(appointment);
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public AppointmentResponse getById(Long id) {
        Appointment appt = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found: " + id));
        return toResponse(appt);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getAll() {
        return appointmentRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public void delete(Long id) {
        Appointment appt = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found: " + id));
        appointmentRepository.delete(appt);
    }

    private void validateTimeRange(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            throw new ConflictException("startTime and endTime must be provided.");
        }
        if (!start.isBefore(end)) {
            throw new ConflictException("startTime must be before endTime.");
        }
    }

    private AppointmentResponse toResponse(Appointment a) {
        return new AppointmentResponse(
                a.getId(),
                a.getPatient().getId(),
                a.getDoctor().getId(),
                a.getStartTime(),
                a.getEndTime(),
                a.getNotes()
        );
    }
}
