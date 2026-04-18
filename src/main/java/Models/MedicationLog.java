package Models;

public class MedicationLog {

    //Attributes
    private int id;
    private int userId;
    private int reportId;
    private int medicationId;
    private String status; // 'Taken', 'Skipped', 'Missed'
    private String date;

    //Constructors
    public MedicationLog() {}

    public MedicationLog(int id, int userId, int reportId, int medicationId, String status, String date) {
        this.id = id;
        this.userId = userId;
        this.reportId = reportId;
        this.medicationId = medicationId;
        this.status = status;
        this.date = date;
    }

    //Getters & Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public int getMedicationId() {
        return medicationId;
    }

    public void setMedicationId(int medicationId) {
        this.medicationId = medicationId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
