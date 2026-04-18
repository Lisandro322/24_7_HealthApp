package DAO;

import DB.DatabaseHelper;
import Models.MedicationLog;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicationLogDAO {

    public void insertLog(MedicationLog log) {
        String sql = "INSERT INTO Medication_Log(user_id, report_id, medication_id, status, date) VALUES(?,?,?,?,?)";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, log.getUserId());
            pstmt.setInt(2, log.getReportId());
            pstmt.setInt(3, log.getMedicationId());
            pstmt.setString(4, log.getStatus());
            pstmt.setString(5, log.getDate());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<MedicationLog> getLogsByReportId(int reportId) {
        List<MedicationLog> logs = new ArrayList<>();
        String sql = "SELECT * FROM Medication_Log WHERE report_id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, reportId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                logs.add(new MedicationLog(
                        rs.getInt("id"), rs.getInt("user_id"), rs.getInt("report_id"),
                        rs.getInt("medication_id"), rs.getString("status"), rs.getString("date")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return logs;
    }
}