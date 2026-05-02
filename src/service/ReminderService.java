package service;

import java.util.List;

import model.Reminder;
import dao.ReminderDAO;
import dao.TakeReminderDAO;

public class ReminderService {
    private final ReminderDAO reminderDAO = new ReminderDAO();
    private final TakeReminderDAO takeReminderDAO = new TakeReminderDAO();

    public List<Reminder> getAllReminder() {
        return reminderDAO.findAll();
    }

    public int insertTakeReminder(int appointmentId, String reminderTitle) {
        int reminderId = reminderDAO.findIdByTitle(reminderTitle);
        if (reminderId == 0) {
            return 0;
        }
        return takeReminderDAO.insert(appointmentId, reminderId);
    }

    public List<Integer> getReminderIdsByAppointment(int appointmentId) {
        return reminderDAO.findReminderIdsByAppointment(appointmentId);
    }
}
