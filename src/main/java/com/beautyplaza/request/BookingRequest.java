// request/BookingRequest.java
package com.beautyplaza.request;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class BookingRequest {
    private Long beautyServiceId;
    private Long technicianId;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String serviceType;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String notes;
}