package com.hospital.automation.appointment.repository;

import com.hospital.automation.appointment.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("""
            select count(a) > 0
            from Appointment a
            where a.doctor.id = :doctorId
              and a.startTime < :endTime
              and a.endTime > :startTime
            """)
    boolean existsOverlappingForDoctor(
            @Param("doctorId") Long doctorId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    @Query("""
            select count(a) > 0
            from Appointment a
            where a.patient.id = :patientId
              and a.startTime < :endTime
              and a.endTime > :startTime
            """)
    boolean existsOverlappingForPatient(
            @Param("patientId") Long patientId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );
}
