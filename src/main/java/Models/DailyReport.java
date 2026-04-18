package Models;

public class DailyReport {

    //Attributes
    private int id;
    private int userId;
    private String date; // Stored as TEXT in SQLite
    private int moodScore;
    private double bmi;
    private double bmr;
    private double caloricIntake;
    private String journalLog;

    //Constructors
    public DailyReport() {}

    public DailyReport(int id, int userId, String date, int moodScore, double bmi, double bmr, double caloricIntake, String journalLog) {
        this.id = id;
        this.userId = userId;
        this.date = date;
        this.moodScore = moodScore;
        this.bmi = bmi;
        this.bmr = bmr;
        this.caloricIntake = caloricIntake;
        this.journalLog = journalLog;
    }

    //Getter & setters

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getMoodScore() {
        return moodScore;
    }

    public void setMoodScore(int moodScore) {
        this.moodScore = moodScore;
    }

    public double getBmi() {
        return bmi;
    }

    public void setBmi(double bmi) {
        this.bmi = bmi;
    }

    public double getBmr() {
        return bmr;
    }

    public void setBmr(double bmr) {
        this.bmr = bmr;
    }

    public double getCaloricIntake() {
        return caloricIntake;
    }

    public void setCaloricIntake(double caloricIntake) {
        this.caloricIntake = caloricIntake;
    }

    public String getJournalLog() {
        return journalLog;
    }

    public void setJournalLog(String journalLog) {
        this.journalLog = journalLog;
    }
}
