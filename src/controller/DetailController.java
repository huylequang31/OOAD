package controller;

import java.util.List;

import model.Users;
import service.DetailService;

public class DetailController {
    private final DetailService detailService = new DetailService();

    public List<Users> getParticipants(int appointmentId) {
        return detailService.getParticipants(appointmentId);
    }

    public List<Integer> getReminderIds(int appointmentId) {
        return detailService.getReminderIds(appointmentId);
    }
}
