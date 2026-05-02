package service;

import java.util.List;

import model.Appointment;
import dao.AppointmentDAO;
import dto.AppointmentRequest;
import dto.AppointmentResult;

public class AppointmentService {
    private final AppointmentDAO appointmentDAO = new AppointmentDAO();

    public AppointmentResult handleAddAppointment(AppointmentRequest request) {
        String validationMessage = validateAppointmentRequest(request);
        if (validationMessage != null) {
            return AppointmentResult.invalid(validationMessage);
        }

        Appointment groupAppointment = appointmentDAO.findGroupAppointment(
            request.getName(),
            request.getDate(),
            request.getStartHour(),
            request.getStartMinute(),
            request.getEndHour(),
            request.getEndMinute()
        );

        if (groupAppointment != null) {
            if (!request.isJoinGroupIfExists()) {
                return AppointmentResult.joinGroup("Lịch hẹn này trùng với 1 group meeting, bạn có muốn tham gia không?");
            }

            int rows = appointmentDAO.insertTake(request.getUserId(), groupAppointment.getId());
            if (rows > 0) {
                addGroupParticipants(groupAppointment.getId(), request);
                return AppointmentResult.success(groupAppointment.getId(), "Đã thêm lịch hẹn thành công!");
            }
            return AppointmentResult.invalid("Không thể tham gia group meeting.");
        }

        Appointment conflictAppointment = appointmentDAO.findUserConflict(
            request.getUserId(),
            request.getDate(),
            request.getStartHour(),
            request.getStartMinute(),
            request.getEndHour(),
            request.getEndMinute()
        );

        if (conflictAppointment != null) {
            if (!request.isReplaceConflictIfExists()) {
                return AppointmentResult.conflict("Lịch hẹn đã trùng với lịch cũ của bạn, bạn có muốn thay thế không?");
            }

            int rows = appointmentDAO.updateNameLocationById(
                conflictAppointment.getId(),
                request.getName(),
                request.getLocation()
            );
            if (rows > 0) {
                return AppointmentResult.success(conflictAppointment.getId(), "Đã thay thế lịch hẹn thành công!");
            }
            return AppointmentResult.invalid("Không thể thay thế lịch hẹn.");
        }

        int appointmentId = appointmentDAO.insert(
            request.getUserId(),
            request.getName(),
            request.getLocation(),
            request.getDate(),
            request.getStartHour(),
            request.getStartMinute(),
            request.getEndHour(),
            request.getEndMinute(),
            request.getTypeAppointment()
        );

        if (appointmentId > 0) {
            addGroupParticipants(appointmentId, request);
            return AppointmentResult.success(appointmentId, "Thêm lịch hẹn thành công!");
        }
        return AppointmentResult.invalid("Không thể thêm lịch hẹn.");
    }

    private String validateAppointmentRequest(AppointmentRequest request) {
        if (request == null) {
            return "Dữ liệu lịch hẹn không hợp lệ.";
        }
        if (isBlank(request.getName()) || isBlank(request.getLocation())) {
            return "Vui lòng điền đủ thông tin!";
        }
        if (request.getDate() == null) {
            return "Ngày hẹn không hợp lệ.";
        }
        if (!isValidHour(request.getStartHour()) || !isValidHour(request.getEndHour())
                || !isValidMinute(request.getStartMinute()) || !isValidMinute(request.getEndMinute())) {
            return "Thời gian không hợp lệ.";
        }
        int start = request.getStartHour() * 60 + request.getStartMinute();
        int end = request.getEndHour() * 60 + request.getEndMinute();
        if (start >= end) {
            return "Giờ bắt đầu phải bé hơn giờ kết thúc!";
        }
        if (isBlank(request.getTypeAppointment())) {
            return "Kiểu cuộc họp không hợp lệ.";
        }
        if (isGroupMeeting(request) && (request.getParticipantUserIds() == null || request.getParticipantUserIds().isEmpty())) {
            return "Vui lòng chọn ít nhất 1 thành viên cho cuộc họp nhóm.";
        }
        return null;
    }

    private boolean isGroupMeeting(AppointmentRequest request) {
        return "Nhóm".equalsIgnoreCase(request.getTypeAppointment());
    }

    private void addGroupParticipants(int appointmentId, AppointmentRequest request) {
        if (!isGroupMeeting(request)) {
            return;
        }
        appointmentDAO.insertParticipants(appointmentId, request.getParticipantUserIds());
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private boolean isValidHour(int hour) {
        return hour >= 0 && hour <= 23;
    }

    private boolean isValidMinute(int minute) {
        return minute >= 0 && minute <= 59;
    }

    public int updateInfo(int userId, int appId, String name, String location, java.util.Date date, int startHour, int startMinute, int endHour, int endMinute, String type) {
        Appointment conflictAppointment = appointmentDAO.findUserConflictExcludingAppointment(
            userId,
            appId,
            date,
            startHour,
            startMinute,
            endHour,
            endMinute
        );
        if (conflictAppointment != null) {
            return -2;
        }
        return appointmentDAO.updateById(appId, name, location, date, startHour, startMinute, endHour, endMinute, type);
    }

    public List<Appointment> getAppointmentsByDate(int userId, java.util.Date date) {
        return appointmentDAO.findByDate(userId, date);
    }

    public List<Appointment> getAppointmentsByUser(int userId) {
        return appointmentDAO.findByUser(userId);
    }

    public boolean deleteAppointment(int appointmentId) {
        return appointmentDAO.deleteById(appointmentId) > 0;
    }
}
