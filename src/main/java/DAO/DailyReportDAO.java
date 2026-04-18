package DAO;

import DB.DatabaseHelper;
import Models.DailyReport;
import java.sql.*;

public class DailyReportDAO {

    /**
     * Saves or Updates a report.
     * SQLite 'REPLACE' handles the logic if a record with the same unique
     * user_id and date already exists.
     */
    public void upsertReport(DailyReport report) {
        String sql = "INSERT OR REPLACE INTO Daily_Reports(user_id, date, mood_score, bmi, bmr, caloric_intake, journal_log) VALUES(?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, report.getUserId());
            pstmt.setString(2, report.getDate());
            pstmt.setInt(3, report.getMoodScore());
            pstmt.setDouble(4, report.getBmi());
            pstmt.setDouble(5, report.getBmr());
            pstmt.setDouble(6, report.getCaloricIntake());
            pstmt.setString(7, report.getJournalLog());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error upserting daily report: " + e.getMessage());
        }
    }

    /**
     * Fetches a specific report for a user on a specific date.
     * Essential for loading existing data into the ReportView.
     */
    public DailyReport getReportByDate(int userId, String date) {
        String sql = "SELECT * FROM Daily_Reports WHERE user_id = ? AND date = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, date);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToReport(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching report by date: " + e.getMessage());
        }
        return null; // Return null if no report exists for today yet
    }

    /**
     * Helper method to map SQLite rows to the DailyReport Model.
     */
    private DailyReport mapResultSetToReport(ResultSet rs) throws SQLException {
        return new DailyReport(
                rs.getInt("id"),
                rs.getInt("user_id"),
                rs.getString("date"),
                rs.getInt("mood_score"),
                rs.getDouble("bmi"),
                rs.getDouble("bmr"),
                rs.getDouble("caloric_intake"),
                rs.getString("journal_log")
        );
    }
}