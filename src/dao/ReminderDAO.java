package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Reminder;
import db.SQLServerConnection;

public class ReminderDAO {
    public List<Reminder> findAll() {
        List<Reminder> list = new ArrayList<>();
        String sql = "SELECT * FROM reminder";
        try (Connection connection = SQLServerConnection.getConnection();
             PreparedStatement st = connection.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                list.add(new Reminder(rs.getInt("id"), rs.getString("title")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int findIdByTitle(String title) {
        String sql = "SELECT id FROM reminder WHERE title = ?";
        try (Connection connection = SQLServerConnection.getConnection();
             PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, title);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Integer> findReminderIdsByAppointment(int appointmentId) {
        List<Integer> list = new ArrayList<>();
        String sql = """
            SELECT take_rmd.reminder_id
            FROM appointment
            JOIN take_rmd ON appointment.id = take_rmd.appointment_id
            WHERE appointment.id = ?
        """;
        try (Connection connection = SQLServerConnection.getConnection();
             PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, appointmentId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                list.add(rs.getInt("reminder_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
