package controller;

import java.util.Date;
import java.util.List;

import model.Appointment;
import dto.AppointmentRequest;
import dto.AppointmentResult;
import service.AppointmentService;

public class AppointmentController {
    private final AppointmentService appointmentService = new AppointmentService();

    public AppointmentResult addAppointment(AppointmentRequest request) {
        return appointmentService.handleAddAppointment(request);
    }

    public int updateInfo(int userId, int appId, String name, String location, Date date, int startHour, int startMinute, int endHour, int endMinute, String type) {
        return appointmentService.updateInfo(userId, appId, name, location, date, startHour, startMinute, endHour, endMinute, type);
    }

    public List<Appointment> getAppointmentsByDate(int userId, Date date) {
        return appointmentService.getAppointmentsByDate(userId, date);
    }

    public List<Appointment> getAppointmentsByUser(int userId) {
        return appointmentService.getAppointmentsByUser(userId);
    }

    public boolean deleteAppointment(int appointmentId) {
        return appointmentService.deleteAppointment(appointmentId);
    }
}
