package com.maiia.pro.service;

import com.maiia.pro.entity.Appointment;
import com.maiia.pro.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProAppointmentService {
    @Autowired
    private AppointmentRepository appointmentRepository;

    public Appointment find(String appointmentId) {
        return appointmentRepository.findById(appointmentId).orElseThrow();
    }

    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }

    public List<Appointment> findByPractitionerId(int practitionerId) {
        return appointmentRepository.findByPractitionerId(practitionerId);
    }

    public Appointment createAppointment(int practitionerId,int patientId,LocalDateTime startDate,LocalDateTime endDate) {
        Appointment appointment = new Appointment();
        appointment.setPatientId(patientId);
        appointment.setPractitionerId(practitionerId);
        appointment.setStartDate(startDate);
        appointment.setEndDate(endDate);
        return appointmentRepository.save(appointment);
    }

}
