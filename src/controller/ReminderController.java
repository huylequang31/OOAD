package controller;

import java.util.List;

import model.Reminder;
import service.ReminderService;

public class ReminderController {
    private final ReminderService reminderService = new ReminderService();

    public List<Reminder> getAllReminder() {
        return reminderService.getAllReminder();
    }

    public int InsertTakeReminder(int appointmentId, String reminderTitle) {
        return reminderService.insertTakeReminder(appointmentId, reminderTitle);
    }
}
