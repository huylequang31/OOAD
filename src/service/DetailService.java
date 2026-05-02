package service;

import java.util.List;

import model.Users;
import dao.UserDAO;

public class DetailService {
    private final UserDAO userDAO = new UserDAO();
    private final ReminderService reminderService = new ReminderService();

    public List<Users> getParticipants(int appointmentId) {
        return userDAO.findByAppointmentId(appointmentId);
    }

    public List<Integer> getReminderIds(int appointmentId) {
        return reminderService.getReminderIdsByAppointment(appointmentId);
    }
}
