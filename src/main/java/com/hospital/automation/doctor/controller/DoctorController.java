package com.hospital.automation.doctor.controller;

import com.hospital.automation.doctor.dto.DoctorCreateRequest;
import com.hospital.automation.doctor.dto.DoctorResponse;
import com.hospital.automation.doctor.dto.DoctorUpdateRequest;
import com.hospital.automation.doctor.dto.TimeSlotResponse;
import com.hospital.automation.doctor.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DoctorResponse create(@Valid @RequestBody DoctorCreateRequest request) {
        return doctorService.create(request);
    }

    @GetMapping("/{id}")
    public DoctorResponse getById(@PathVariable Long id) {
        return doctorService.getById(id);
    }

    @GetMapping
    public List<DoctorResponse> getAll() {
        return doctorService.getAll();
    }

    @GetMapping("/page")
    public Page<DoctorResponse> getPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "fullName,asc") String sort
    ) {
        return doctorService.getPage(page, size, sort);
    }

    @GetMapping("/{doctorId}/availability")
    public List<TimeSlotResponse> availability(
            @PathVariable Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(defaultValue = "09:00") @DateTimeFormat(pattern = "HH:mm") LocalTime workStart,
            @RequestParam(defaultValue = "17:00") @DateTimeFormat(pattern = "HH:mm") LocalTime workEnd,
            @RequestParam(defaultValue = "30") int slotMinutes
    ) {
        return doctorService.getAvailability(doctorId, date, workStart, workEnd, slotMinutes);
    }

    @PutMapping("/{id}")
    public DoctorResponse update(@PathVariable Long id, @Valid @RequestBody DoctorUpdateRequest request) {
        return doctorService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        doctorService.delete(id);
    }
}
