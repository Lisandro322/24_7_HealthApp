package Models;

public class MedicationInfo {

    //Attributes
    private int id;
    private int userId;
    private String medName;
    private int dailyReq;
    private String dosage;
    private String note;

    //Constructors
    public MedicationInfo() {
    }

    public MedicationInfo(int id, int userId, String medName, int dailyReq, String dosage, String note) {
        this.id = id;
        this.userId = userId;
        this.medName = medName;
        this.dailyReq = dailyReq;
        this.dosage = dosage;
        this.note = note;
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

    public String getMedName() {
        return medName;
    }

    public void setMedName(String medName) {
        this.medName = medName;
    }

    public int getDailyReq() {
        return dailyReq;
    }

    public void setDailyReq(int dailyReq) {
        this.dailyReq = dailyReq;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}