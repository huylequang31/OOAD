package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import db.SQLServerConnection;

public class TakeReminderDAO {
    public int insert(int appointmentId, int reminderId) {
        String sql = "INSERT INTO take_rmd(appointment_id, reminder_id) VALUES (?, ?)";
        try (Connection connection = SQLServerConnection.getConnection();
             PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, appointmentId);
            st.setInt(2, reminderId);
            return st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
