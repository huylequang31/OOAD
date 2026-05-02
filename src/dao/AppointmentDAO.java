package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Appointment;
import db.SQLServerConnection;

public class AppointmentDAO {
    private void ensureMinuteColumns(Connection connection) throws SQLException {
        try (Statement st = connection.createStatement()) {
            st.executeUpdate("IF COL_LENGTH('appointment', 'start_minute') IS NULL ALTER TABLE appointment ADD start_minute INT NOT NULL DEFAULT 0");
            st.executeUpdate("IF COL_LENGTH('appointment', 'end_minute') IS NULL ALTER TABLE appointment ADD end_minute INT NOT NULL DEFAULT 0");
        }
    }

    private Appointment readAppointment(ResultSet rs) throws SQLException {
        return new Appointment(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("location"),
            rs.getDate("meeting_date"),
            rs.getInt("start_hour"),
            rs.getInt("start_minute"),
            rs.getInt("end_hour"),
            rs.getInt("end_minute"),
            rs.getString("type_appointment")
        );
    }

    public Appointment findGroupAppointment(String name, Date date, int startHour, int startMinute, int endHour, int endMinute) {
        String sql = """
            SELECT *
            FROM appointment
            WHERE name = ?
              AND type_appointment = ?
              AND meeting_date = ?
              AND start_hour = ?
              AND start_minute = ?
              AND end_hour = ?
              AND end_minute = ?
        """;

        try (Connection connection = SQLServerConnection.getConnection()) {
            ensureMinuteColumns(connection);
            try (PreparedStatement st = connection.prepareStatement(sql)) {
                st.setString(1, name);
                st.setString(2, "Nhóm");
                st.setDate(3, new java.sql.Date(date.getTime()));
                st.setInt(4, startHour);
                st.setInt(5, startMinute);
                st.setInt(6, endHour);
                st.setInt(7, endMinute);

                ResultSet rs = st.executeQuery();
                if (rs.next()) {
                    return readAppointment(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Appointment findUserConflict(int userId, Date date, int startHour, int startMinute, int endHour, int endMinute) {
        String sql = """
            SELECT a.*
            FROM appointment a
            JOIN take t ON a.id = t.appointment_id
            WHERE t.user_id = ?
              AND a.meeting_date = ?
              AND NOT ((a.end_hour * 60 + a.end_minute) <= ? OR (a.start_hour * 60 + a.start_minute) >= ?)
        """;

        try (Connection conn = SQLServerConnection.getConnection()) {
            ensureMinuteColumns(conn);
            try (PreparedStatement st = conn.prepareStatement(sql)) {
                st.setInt(1, userId);
                st.setDate(2, new java.sql.Date(date.getTime()));
                st.setInt(3, startHour * 60 + startMinute);
                st.setInt(4, endHour * 60 + endMinute);

                ResultSet rs = st.executeQuery();
                if (rs.next()) {
                    return readAppointment(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Appointment findUserConflictExcludingAppointment(int userId, int excludedAppointmentId, Date date, int startHour, int startMinute, int endHour, int endMinute) {
        String sql = """
            SELECT a.*
            FROM appointment a
            JOIN take t ON a.id = t.appointment_id
            WHERE t.user_id = ?
              AND a.id <> ?
              AND a.meeting_date = ?
              AND NOT ((a.end_hour * 60 + a.end_minute) <= ? OR (a.start_hour * 60 + a.start_minute) >= ?)
        """;

        try (Connection conn = SQLServerConnection.getConnection()) {
            ensureMinuteColumns(conn);
            try (PreparedStatement st = conn.prepareStatement(sql)) {
                st.setInt(1, userId);
                st.setInt(2, excludedAppointmentId);
                st.setDate(3, new java.sql.Date(date.getTime()));
                st.setInt(4, startHour * 60 + startMinute);
                st.setInt(5, endHour * 60 + endMinute);

                ResultSet rs = st.executeQuery();
                if (rs.next()) {
                    return readAppointment(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int insert(int userId, String name, String location, Date date, int startHour, int startMinute, int endHour, int endMinute, String typeAppointment) {
        String sql = """
            INSERT INTO appointment(name, location, meeting_date, start_hour, start_minute, end_hour, end_minute, type_appointment)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection connection = SQLServerConnection.getConnection()) {
            ensureMinuteColumns(connection);
            try (PreparedStatement st = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                st.setString(1, name);
                st.setString(2, location);
                st.setDate(3, new java.sql.Date(date.getTime()));
                st.setInt(4, startHour);
                st.setInt(5, startMinute);
                st.setInt(6, endHour);
                st.setInt(7, endMinute);
                st.setString(8, typeAppointment);
                st.executeUpdate();

                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int appointmentId = rs.getInt(1);
                    insertTake(userId, appointmentId);
                    return appointmentId;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int updateById(int appId, String name, String location, Date date, int startHour, int startMinute, int endHour, int endMinute, String type) {
        String sql = "UPDATE appointment SET name = ?, location = ?, meeting_date = ?, start_hour = ?, start_minute = ?, end_hour = ?, end_minute = ?, type_appointment = ? WHERE id = ?";
        try (Connection connection = SQLServerConnection.getConnection()) {
            ensureMinuteColumns(connection);
            try (PreparedStatement st = connection.prepareStatement(sql)) {
                st.setString(1, name);
                st.setString(2, location);
                st.setDate(3, new java.sql.Date(date.getTime()));
                st.setInt(4, startHour);
                st.setInt(5, startMinute);
                st.setInt(6, endHour);
                st.setInt(7, endMinute);
                st.setString(8, type);
                st.setInt(9, appId);
                return st.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int updateNameLocationById(int appointmentId, String name, String location) {
        String sql = "UPDATE appointment SET name = ?, location = ? WHERE id = ?";
        try (Connection connection = SQLServerConnection.getConnection();
             PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, name);
            st.setString(2, location);
            st.setInt(3, appointmentId);
            return st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int insertTake(int userId, int appointmentId) {
        String sql = "INSERT INTO take(user_id, appointment_id) VALUES(?, ?)";
        try (Connection connection = SQLServerConnection.getConnection();
             PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, userId);
            st.setInt(2, appointmentId);
            return st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int insertParticipants(int appointmentId, List<Integer> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return 0;
        }
        String sql = """
            IF NOT EXISTS (
                SELECT 1
                FROM take
                WHERE user_id = ? AND appointment_id = ?
            )
            INSERT INTO take(user_id, appointment_id) VALUES(?, ?)
        """;
        int inserted = 0;
        try (Connection connection = SQLServerConnection.getConnection();
             PreparedStatement st = connection.prepareStatement(sql)) {
            for (Integer userId : userIds) {
                if (userId == null) {
                    continue;
                }
                st.setInt(1, userId);
                st.setInt(2, appointmentId);
                st.setInt(3, userId);
                st.setInt(4, appointmentId);
                inserted += st.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return inserted;
    }

    public List<Appointment> findByDate(int userId, Date date) {
        List<Appointment> list = new ArrayList<>();
        String sql = """
            SELECT a.*
            FROM appointment a
            JOIN take t ON a.id = t.appointment_id
            WHERE t.user_id = ? AND a.meeting_date = ?
            ORDER BY a.start_hour, a.start_minute, a.end_hour, a.end_minute
        """;

        try (Connection conn = SQLServerConnection.getConnection()) {
            ensureMinuteColumns(conn);
            try (PreparedStatement st = conn.prepareStatement(sql)) {
                st.setInt(1, userId);
                st.setDate(2, new java.sql.Date(date.getTime()));
                ResultSet rs = st.executeQuery();
                while (rs.next()) {
                    list.add(readAppointment(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Appointment> findByUser(int userId) {
        List<Appointment> list = new ArrayList<>();
        String sql = """
            SELECT a.*
            FROM appointment a
            JOIN take t ON a.id = t.appointment_id
            WHERE t.user_id = ?
            ORDER BY a.meeting_date, a.start_hour, a.start_minute
        """;

        try (Connection conn = SQLServerConnection.getConnection()) {
            ensureMinuteColumns(conn);
            try (PreparedStatement st = conn.prepareStatement(sql)) {
                st.setInt(1, userId);
                ResultSet rs = st.executeQuery();
                while (rs.next()) {
                    list.add(readAppointment(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int deleteById(int appointmentId) {
        try (Connection connection = SQLServerConnection.getConnection()) {
            try (PreparedStatement stDeleteReminders = connection.prepareStatement("DELETE FROM take_rmd WHERE appointment_id = ?")) {
                stDeleteReminders.setInt(1, appointmentId);
                stDeleteReminders.executeUpdate();
            }

            try (PreparedStatement stDeleteTake = connection.prepareStatement("DELETE FROM take WHERE appointment_id = ?")) {
                stDeleteTake.setInt(1, appointmentId);
                stDeleteTake.executeUpdate();
            }

            try (PreparedStatement stDeleteAppointment = connection.prepareStatement("DELETE FROM appointment WHERE id = ?")) {
                stDeleteAppointment.setInt(1, appointmentId);
                return stDeleteAppointment.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
