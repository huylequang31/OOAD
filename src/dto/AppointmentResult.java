package dto;

public class AppointmentResult {
    private final AppointmentResultStatus status;
    private final int appointmentId;
    private final String message;

    private AppointmentResult(AppointmentResultStatus status, int appointmentId, String message) {
        this.status = status;
        this.appointmentId = appointmentId;
        this.message = message;
    }

    public static AppointmentResult success(int appointmentId, String message) {
        return new AppointmentResult(AppointmentResultStatus.SUCCESS, appointmentId, message);
    }

    public static AppointmentResult conflict(String message) {
        return new AppointmentResult(AppointmentResultStatus.CONFLICT, 0, message);
    }

    public static AppointmentResult joinGroup(String message) {
        return new AppointmentResult(AppointmentResultStatus.JOIN_GROUP, 0, message);
    }

    public static AppointmentResult invalid(String message) {
        return new AppointmentResult(AppointmentResultStatus.INVALID, 0, message);
    }

    public AppointmentResultStatus getStatus() {
        return status;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public String getMessage() {
        return message;
    }
}
