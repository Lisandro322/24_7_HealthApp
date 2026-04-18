package Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHelper {
    // The connection string (this will create 'health_tracker.db' in your project folder)
    private static final String URL = "jdbc:sqlite:health_tracker.db";
    private static Connection connection = null;

    // Get or Create Connection
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL);
        }
        return connection;
    }

    // The Setup Function
    public static void initializeDatabase() {
        // All your CREATE TABLE strings combined
        String[] tables = {
                "CREATE TABLE IF NOT EXISTS Users (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, first_name TEXT NOT NULL, last_name TEXT NOT NULL, " +
                        "age INTEGER CHECK (age >= 0), gender TEXT CHECK (gender IN ('Male', 'Female')), " +
                        "weight REAL CHECK (weight >= 0), height REAL CHECK (height >= 0));",

                "CREATE TABLE IF NOT EXISTS Daily_Reports (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER NOT NULL, date TEXT NOT NULL, " +
                        "mood_score INTEGER CHECK (mood_score BETWEEN 1 AND 10), bmi REAL, bmr REAL, " +
                        "caloric_intake REAL CHECK (caloric_intake >= 0), journal_log TEXT, " +
                        "UNIQUE (user_id, date), FOREIGN KEY (user_id) REFERENCES Users(id));",

                "CREATE TABLE IF NOT EXISTS Medication_Info (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER NOT NULL, med_name TEXT NOT NULL, " +
                        "daily_req INTEGER CHECK (daily_req >= 0), dosage TEXT, note TEXT, " +
                        "FOREIGN KEY (user_id) REFERENCES Users(id));",

                "CREATE TABLE IF NOT EXISTS Medication_Log (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER NOT NULL, report_id INTEGER NOT NULL, " +
                        "medication_id INTEGER NOT NULL, status TEXT CHECK (status IN ('Taken', 'Skipped', 'Missed')), " +
                        "date TEXT NOT NULL, FOREIGN KEY (user_id) REFERENCES Users(id), " +
                        "FOREIGN KEY (report_id) REFERENCES Daily_Reports(id), FOREIGN KEY (medication_id) REFERENCES Medication_Info(id));",

                "CREATE TABLE IF NOT EXISTS Medication_Schedule (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER NOT NULL, medication_id INTEGER NOT NULL, " +
                        "time TEXT NOT NULL, status TEXT CHECK (status IN ('Active', 'Stopped')), " +
                        "FOREIGN KEY (user_id) REFERENCES Users(id), FOREIGN KEY (medication_id) REFERENCES Medication_Info(id));"
        };

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            // Enable Foreign Key support in SQLite (Disabled by default!)
            stmt.execute("PRAGMA foreign_keys = ON;");

            for (String sql : tables) {
                stmt.execute(sql);
            }
            System.out.println("Database tables initialized successfully.");
        } catch (SQLException e) {
            System.err.println("Database setup error: " + e.getMessage());
        }
    }
}