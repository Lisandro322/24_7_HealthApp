package Models;

public class UserInfo {
    //Attribute
    private int id;
    private String firstName;
    private String lastName;
    private int age;
    private String gender;
    private double weight;
    private double height;

    // Constructor
    public UserInfo() {}

    public UserInfo(int id, String firstName, String lastName, int age, String gender, double weight, double height) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
        this.weight = weight;
        this.height = height;
    }

    //ToString
    @Override
    public String toString() {
        return firstName + " " + lastName + " " + age + " " + gender + " " + weight + " " + height;
    }

    // Getters and setters
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public String getFirstName() {
        return firstName;
    }
    public void setfirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {return lastName;}
    public void setlastName(String lastName) {this.lastName = lastName;}

    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    public double getWeight() {
        return weight;
    }
    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }
    public void setHeight(double height) {
        this.height = height;
    }
}
