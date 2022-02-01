package com.maiia.pro.service;

import com.maiia.pro.entity.Appointment;
import com.maiia.pro.entity.Availability;
import com.maiia.pro.entity.TimeSlot;
import com.maiia.pro.repository.AppointmentRepository;
import com.maiia.pro.repository.AvailabilityRepository;
import com.maiia.pro.repository.TimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;


@Service
public class ProAvailabilityService {

    @Autowired
    private AvailabilityRepository availabilityRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private TimeSlotRepository timeSlotRepository;

   

    public List<Availability> findByPractitionerId(Integer practitionerId) {
          return availabilityRepository.findByPractitionerId(practitionerId);
    }

    public List<Availability> generateAvailabilities(Integer practitionerId) {
        List<Availability>result= new ArrayList<>();
        List<TimeSlot> timeSlotsLists= timeSlotRepository.findByPractitionerId(practitionerId);
        List<Appointment> appointments= appointmentRepository.findByPractitionerId(practitionerId);
        long availablityDurationInMilis=15*60*1000;
      
        // remove reserved time slots and get available ones
        for (Appointment appointment : appointments) {
            LocalDateTime appointmentStartDate=appointment.getStartDate();
            LocalDateTime appointmentEndDate=appointment.getEndDate();
           for (int i = 0; i <timeSlotsLists.size() ; i++) {
               TimeSlot timeSlot= timeSlotsLists.get(i);
            LocalDateTime timeSlotStartDate=timeSlot.getStartDate();
            LocalDateTime timeSlotEndDate=timeSlot.getEndDate();
               if(appointmentStartDate.equals(timeSlotStartDate)){
                timeSlotsLists.add(new TimeSlot(timeSlot.getId(), practitionerId, 
                appointmentEndDate, timeSlotEndDate));
               }else if
               (appointmentStartDate.isAfter(timeSlotStartDate) 
               &&  appointmentStartDate.isBefore(timeSlotEndDate)) {
                if(appointmentEndDate.equals(timeSlotEndDate)){
                    timeSlotsLists.add(new TimeSlot(timeSlot.getId(), practitionerId, 
                    timeSlotStartDate,appointmentStartDate));
                }else{
                    timeSlotsLists.add(new TimeSlot(timeSlot.getId(), practitionerId, 
                    timeSlotStartDate,appointmentStartDate));
                    timeSlotsLists.add(new TimeSlot(timeSlot.getId(), practitionerId, 
                    appointmentEndDate, timeSlotEndDate));
                }
               }else{
                timeSlotsLists.add(new TimeSlot(timeSlot.getId(), practitionerId, 
                timeSlotStartDate, timeSlotEndDate));
               }
               timeSlotsLists.remove(i);
              
           }
           
        }
      
      
      
      
        for (TimeSlot timeSlot : timeSlotsLists) {
       
                long numberOfAvailiabilites=ChronoUnit.MILLIS.between(timeSlot.getStartDate(), timeSlot.getEndDate())/availablityDurationInMilis;
                LocalDateTime startDate=timeSlot.getStartDate();
                LocalDateTime endDate=startDate.plusMinutes(15);
                for (int i = 0; i < numberOfAvailiabilites; i++) {
                 
                    result.add(new Availability(i, practitionerId, startDate,endDate));
                   
                     startDate=endDate;
                     endDate=startDate.plusMinutes(15);
                }
          if(startDate.isBefore(timeSlot.getEndDate()) && ChronoUnit.MILLIS.between(startDate, timeSlot.getEndDate())>=10*60*1000 ){
            result.add(new Availability((int)numberOfAvailiabilites, practitionerId, startDate,timeSlot.getEndDate()));
          }
            
           
        }
      

        return result;
    }
}
