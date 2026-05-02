package dto;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class AppointmentRequest {
    private int userId;
    private String name;
    private String location;
    private Date date;
    private int startHour;
    private int startMinute;
    private int endHour;
    private int endMinute;
    private String typeAppointment;
    private boolean joinGroupIfExists;
    private boolean replaceConflictIfExists;
    private List<Integer> participantUserIds;

    public AppointmentRequest(int userId, String name, String location, Date date,
            int startHour, int startMinute, int endHour, int endMinute,
            String typeAppointment) {
        this.userId = userId;
        this.name = name;
        this.location = location;
        this.date = date;
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endHour = endHour;
        this.endMinute = endMinute;
        this.typeAppointment = typeAppointment;
        this.participantUserIds = new ArrayList<>();
    }

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public Date getDate() {
        return date;
    }

    public int getStartHour() {
        return startHour;
    }

    public int getStartMinute() {
        return startMinute;
    }

    public int getEndHour() {
        return endHour;
    }

    public int getEndMinute() {
        return endMinute;
    }

    public String getTypeAppointment() {
        return typeAppointment;
    }

    public boolean isJoinGroupIfExists() {
        return joinGroupIfExists;
    }

    public void setJoinGroupIfExists(boolean joinGroupIfExists) {
        this.joinGroupIfExists = joinGroupIfExists;
    }

    public boolean isReplaceConflictIfExists() {
        return replaceConflictIfExists;
    }

    public void setReplaceConflictIfExists(boolean replaceConflictIfExists) {
        this.replaceConflictIfExists = replaceConflictIfExists;
    }

    public List<Integer> getParticipantUserIds() {
        return participantUserIds;
    }

    public void setParticipantUserIds(List<Integer> participantUserIds) {
        this.participantUserIds = participantUserIds != null ? participantUserIds : new ArrayList<>();
    }
}
