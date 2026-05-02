package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Users;
import db.SQLServerConnection;

public class UserDAO {
    public List<Users> findAllExceptUserId(int excludedUserId) {
        List<Users> users = new ArrayList<>();
        String sql = "SELECT id, name, phone_number FROM users WHERE id <> ? ORDER BY name";
        try (Connection connection = SQLServerConnection.getConnection();
             PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, excludedUserId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                users.add(new Users(rs.getInt("id"), rs.getString("name"), rs.getString("phone_number")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public List<Users> findByAppointmentId(int appointmentId) {
        List<Users> users = new ArrayList<>();
        String sql = """
            SELECT users.*
            FROM users
            JOIN take ON users.id = take.user_id
            WHERE appointment_id = ?
        """;
        try (Connection connection = SQLServerConnection.getConnection();
             PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, appointmentId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                users.add(new Users(rs.getInt("id"), rs.getString("name"), rs.getString("phone_number")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
}
