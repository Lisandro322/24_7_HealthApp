package DAO;

import DB.DatabaseHelper;
import Models.MedicationInfo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicationInfoDAO {

    public void insertMedication(MedicationInfo med) {
        String sql = "INSERT INTO Medication_Info(user_id, med_name, daily_req, dosage, note) VALUES(?,?,?,?,?)";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, med.getUserId());
            pstmt.setString(2, med.getMedName());
            pstmt.setInt(3, med.getDailyReq());
            pstmt.setString(4, med.getDosage());
            pstmt.setString(5, med.getNote());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<MedicationInfo> getMedsByUserId(int userId) {
        List<MedicationInfo> list = new ArrayList<>();
        String sql = "SELECT * FROM Medication_Info WHERE user_id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToMedInfo(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void deleteMedication(int medId) {
        String deleteSchedules = "DELETE FROM Medication_Schedule WHERE medication_id = ?";
        String deleteMed = "DELETE FROM Medication_Info WHERE id = ?";

        try (Connection conn = DatabaseHelper.getConnection()) {
            conn.setAutoCommit(false); // Start Transaction

            try (PreparedStatement psSched = conn.prepareStatement(deleteSchedules);
                 PreparedStatement psMed = conn.prepareStatement(deleteMed)) {

                // 1. Clear schedules first
                psSched.setInt(1, medId);
                psSched.executeUpdate();

                // 2. Clear medication info
                psMed.setInt(1, medId);
                psMed.executeUpdate();

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                System.err.println("Transaction rolled back: " + e.getMessage());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean updateMedication(MedicationInfo med) {
        String sql = "UPDATE Medication_Info SET med_name = ?, daily_req = ?, dosage = ?, note = ? WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, med.getMedName());
            pstmt.setInt(2, med.getDailyReq());
            pstmt.setString(3, med.getDosage());
            pstmt.setString(4, med.getNote());
            pstmt.setInt(5, med.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating medication: " + e.getMessage());
            return false;
        }
    }

    private MedicationInfo mapResultSetToMedInfo(ResultSet rs) throws SQLException {
        return new MedicationInfo(
                rs.getInt("id"),
                rs.getInt("user_id"),
                rs.getString("med_name"),
                rs.getInt("daily_req"),
                rs.getString("dosage"),
                rs.getString("note")
        );
    }
    public MedicationInfo getMedById(int medId) {
        String sql = "SELECT * FROM Medication_Info WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, medId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new MedicationInfo(
                        rs.getInt("id"), rs.getInt("user_id"), rs.getString("med_name"),
                        rs.getInt("daily_req"), rs.getString("dosage"), rs.getString("note")
                );
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
}