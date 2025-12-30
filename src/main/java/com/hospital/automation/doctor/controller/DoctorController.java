package com.hospital.automation.doctor.controller;

import com.hospital.automation.doctor.dto.DoctorCreateRequest;
import com.hospital.automation.doctor.dto.DoctorResponse;
import com.hospital.automation.doctor.dto.DoctorUpdateRequest;
import com.hospital.automation.doctor.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
