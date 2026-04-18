package DAO;

import DB.DatabaseHelper;
import Models.MedicationSchedule;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicationScheduleDAO {

    public void insertSchedule(MedicationSchedule medicationSchedule) {
        String sql = "INSERT INTO Medication_Schedule(user_id, medication_id, time, status) VALUES(?,?,?,?)";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, medicationSchedule.getUserId());
            pstmt.setInt(2, medicationSchedule.getMedicationId());
            pstmt.setString(3, medicationSchedule.getTime());
            pstmt.setString(4, medicationSchedule.getStatus());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public List<MedicationSchedule> getSchedulesByUserId(int userId) {
        List<MedicationSchedule> list = new ArrayList<>();
        // Join with Medication_Info to potentially get the name in the future,
        // but for now, we'll just get the schedule rows for this user.
        String sql = "SELECT * FROM Medication_Schedule WHERE user_id = ? ORDER BY time ASC";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new MedicationSchedule(
                        rs.getInt("id"), rs.getInt("user_id"), rs.getInt("medication_id"),
                        rs.getString("time"), rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<MedicationSchedule> getSchedulesByMedId(int medId) {
        List<MedicationSchedule> list = new ArrayList<>();
        String sql = "SELECT * FROM Medication_Schedule WHERE medication_id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, medId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new MedicationSchedule(
                        rs.getInt("id"), rs.getInt("user_id"), rs.getInt("medication_id"),
                        rs.getString("time"), rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void updateScheduleStatus(int scheduleId, String newStatus) {
        String sql = "UPDATE Medication_Schedule SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newStatus);
            pstmt.setInt(2, scheduleId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}