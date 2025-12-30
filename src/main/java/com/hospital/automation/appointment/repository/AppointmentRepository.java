package com.hospital.automation.appointment.repository;

import com.hospital.automation.appointment.entity.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

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

    // UPDATE için: kendi id'sini hariç tut
    @Query("""
            select count(a) > 0
            from Appointment a
            where a.doctor.id = :doctorId
              and a.id <> :appointmentId
              and a.startTime < :endTime
              and a.endTime > :startTime
            """)
    boolean existsOverlappingForDoctorExcludingAppointment(
            @Param("doctorId") Long doctorId,
            @Param("appointmentId") Long appointmentId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    @Query("""
            select count(a) > 0
            from Appointment a
            where a.patient.id = :patientId
              and a.id <> :appointmentId
              and a.startTime < :endTime
              and a.endTime > :startTime
            """)
    boolean existsOverlappingForPatientExcludingAppointment(
            @Param("patientId") Long patientId,
            @Param("appointmentId") Long appointmentId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    @Query("""
            select a
            from Appointment a
            where a.doctor.id = coalesce(:doctorId, a.doctor.id)
              and a.patient.id = coalesce(:patientId, a.patient.id)
              and a.startTime >= coalesce(:from, a.startTime)
              and a.endTime <= coalesce(:to, a.endTime)
            """)
    Page<Appointment> search(
            @Param("doctorId") Long doctorId,
            @Param("patientId") Long patientId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            Pageable pageable
    );

    @Query("""
            select a
            from Appointment a
            where a.doctor.id = :doctorId
              and a.startTime < :dayEnd
              and a.endTime > :dayStart
            order by a.startTime asc
            """)
    List<Appointment> findDoctorAppointmentsInRange(
            @Param("doctorId") Long doctorId,
            @Param("dayStart") LocalDateTime dayStart,
            @Param("dayEnd") LocalDateTime dayEnd
    );
}
