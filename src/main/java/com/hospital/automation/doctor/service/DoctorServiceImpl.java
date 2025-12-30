package com.hospital.automation.doctor.service;

import com.hospital.automation.appointment.entity.Appointment;
import com.hospital.automation.appointment.repository.AppointmentRepository;
import com.hospital.automation.common.exception.ConflictException;
import com.hospital.automation.common.exception.ResourceNotFoundException;
import com.hospital.automation.doctor.dto.DoctorCreateRequest;
import com.hospital.automation.doctor.dto.DoctorResponse;
import com.hospital.automation.doctor.dto.DoctorUpdateRequest;
import com.hospital.automation.doctor.dto.TimeSlotResponse;
import com.hospital.automation.doctor.entity.Doctor;
import com.hospital.automation.doctor.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    public DoctorResponse create(DoctorCreateRequest request) {
        if (doctorRepository.existsByEmail(request.email())) {
            throw new ConflictException("Doctor email already exists: " + request.email());
        }

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
    @Transactional(readOnly = true)
    public Page<DoctorResponse> getPage(int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, parseSort(sort));
        return doctorRepository.findAll(pageable).map(this::toResponse);
    }

    @Override
    public DoctorResponse update(Long id, DoctorUpdateRequest request) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found: " + id));

        if (doctorRepository.existsByEmailAndIdNot(request.email(), id)) {
            throw new ConflictException("Doctor email already exists: " + request.email());
        }

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

    @Override
    @Transactional(readOnly = true)
    public List<TimeSlotResponse> getAvailability(Long doctorId, LocalDate date, LocalTime workStart, LocalTime workEnd, int slotMinutes) {
        // doktor var mı?
        doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found: " + doctorId));

        if (slotMinutes <= 0) {
            throw new ConflictException("slotMinutes must be greater than 0");
        }
        if (!workStart.isBefore(workEnd)) {
            throw new ConflictException("workStart must be before workEnd");
        }

        LocalDateTime dayStart = LocalDateTime.of(date, workStart);
        LocalDateTime dayEnd = LocalDateTime.of(date, workEnd);

        // o gün doktorun randevuları
        List<Appointment> appointments = appointmentRepository.findDoctorAppointmentsInRange(doctorId, dayStart, dayEnd);

        // boş slot hesapla (slotMinutes’lik bloklar)
        List<TimeSlotResponse> available = new ArrayList<>();

        LocalDateTime cursor = dayStart;
        while (cursor.plusMinutes(slotMinutes).compareTo(dayEnd) <= 0) {
            LocalDateTime slotEnd = cursor.plusMinutes(slotMinutes);

            boolean overlaps = false;
            for (Appointment a : appointments) {
                if (a.getStartTime().isBefore(slotEnd) && a.getEndTime().isAfter(cursor)) {
                    overlaps = true;
                    break;
                }
            }

            if (!overlaps) {
                available.add(new TimeSlotResponse(cursor, slotEnd));
            }

            cursor = slotEnd;
        }

        return available;
    }

    private Sort parseSort(String sort) {
        if (sort == null || sort.isBlank()) {
            return Sort.by(Sort.Direction.ASC, "fullName");
        }
        String[] parts = sort.split(",");
        String field = parts[0].trim();
        Sort.Direction direction = Sort.Direction.ASC;
        if (parts.length > 1) {
            direction = "desc".equalsIgnoreCase(parts[1].trim()) ? Sort.Direction.DESC : Sort.Direction.ASC;
        }
        return Sort.by(direction, field);
    }

    private DoctorResponse toResponse(Doctor d) {
        return new DoctorResponse(d.getId(), d.getFullName(), d.getSpecialty(), d.getPhone(), d.getEmail());
    }
}
