package model;

import java.util.Date;

public class Appointment {
	private int id;
	private String name;
	private String location;
	private Date meetingDate;
	private int startHour;
	private int startMinute;
	private int endHour;
	private int endMinute;
	private String typeAppointment;
	
	public Appointment() {}
	
	public Appointment(int id, String name, String location, Date meetingDate, int startHour, int endHour, String typeAppointment) {
		this(id, name, location, meetingDate, startHour, 0, endHour, 0, typeAppointment);
	}
	
	public Appointment(int id, String name, String location, Date meetingDate, int startHour, int startMinute, int endHour, int endMinute, String typeAppointment) {
		this.id = id;
		this.name = name;
		this.location = location;
		this.meetingDate = meetingDate;
		this.startHour = startHour;
		this.startMinute = startMinute;
		this.endHour = endHour;
		this.endMinute = endMinute;
		this.typeAppointment = typeAppointment;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Date getMeetingDate() {
		return meetingDate;
	}

	public void setMeetingDate(Date meetingDate) {
		this.meetingDate = meetingDate;
	}

	public int getStartHour() {
		return startHour;
	}

	public void setStartHour(int startHour) {
		this.startHour = startHour;
	}

	public int getStartMinute() {
		return startMinute;
	}

	public void setStartMinute(int startMinute) {
		this.startMinute = startMinute;
	}

	public int getEndHour() {
		return endHour;
	}

	public void setEndHour(int endHour) {
		this.endHour = endHour;
	}

	public int getEndMinute() {
		return endMinute;
	}

	public void setEndMinute(int endMinute) {
		this.endMinute = endMinute;
	}

	public String getTypeAppointment() {
		return typeAppointment;
	}

	public void setTypeAppointment(String typeAppointment) {
		this.typeAppointment = typeAppointment;
	}
	
}
