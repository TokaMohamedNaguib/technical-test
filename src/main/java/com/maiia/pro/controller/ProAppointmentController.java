package com.maiia.pro.controller;

import com.maiia.pro.entity.Appointment;
import com.maiia.pro.service.ProAppointmentService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/appointments", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProAppointmentController {
    @Autowired
    private ProAppointmentService proAppointmentService;

    @ApiOperation(value = "Get appointments by practitionerId")
    @GetMapping("/{practitionerId}")
    public List<Appointment> getAppointmentsByPractitioner(@PathVariable final int practitionerId) {
        return proAppointmentService.findByPractitionerId(practitionerId);
    }

    @ApiOperation(value = "Get all appointments")
    @GetMapping
    public List<Appointment> getAppointments() {
        return proAppointmentService.findAll();
    }

    @ApiOperation(value = "Create new appointment")
    @PostMapping
    public Appointment createAppointment(@RequestBody HashMap<String, Object> body) {
       try {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        int practitionerId=Integer.parseInt( body.get("practitionerId").toString());
        int patientId=Integer.parseInt( body.get("patientId").toString());
        LocalDateTime startDate= LocalDateTime.parse(body.get("startDate").toString(), formatter);
        LocalDateTime endDate= LocalDateTime.parse(body.get("endDate").toString(), formatter);
        return proAppointmentService.createAppointment(practitionerId, patientId, startDate, endDate);
        } catch (Exception e) {
            throw e;
        }
      
    }
}
