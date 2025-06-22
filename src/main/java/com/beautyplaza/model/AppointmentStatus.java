package com.beautyplaza.model;

/**
 * Enum representing the various statuses an appointment can have.
 */
public enum AppointmentStatus {
    SCHEDULED, // Appointment has been booked.
    CONFIRMED, // Appointment has been confirmed.
    COMPLETED, // Appointment service has been rendered.
    CANCELLED, // Appointment has been cancelled.
    PAID       // Appointment has been paid for.
}
