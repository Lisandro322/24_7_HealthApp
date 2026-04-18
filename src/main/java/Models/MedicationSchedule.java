package Models;

public class MedicationSchedule {
    // Attributes
    private int id;
    private int userId;
    private int medicationId;
    private String time;
    private String status; // 'Active', 'Stopped'

    // Constructors
    public MedicationSchedule() {}

    public MedicationSchedule(int id, int userId, int medicationId, String time, String status) {
        this.id = id;
        this.userId = userId;
        this.medicationId = medicationId;
        this.time = time;
        this.status = status;
    }

    // Getters & Setters
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

    public int getMedicationId() {
        return medicationId;
    }

    public void setMedicationId(int medicationId) {
        this.medicationId = medicationId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
