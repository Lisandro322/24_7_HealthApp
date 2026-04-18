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

}
