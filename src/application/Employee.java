package application;

public class Employee {

    private int ID;
    private String firstName;
    private String surname;
    private String DOB;
    private String gender;
    private int SSN;
    private int salary;

    public Employee(int ID, String firstName, String surname, String DOB, String gender, int SSN, int salary) {
        this.ID = ID;
        this.firstName = firstName;
        this.surname = surname;
        this.DOB = DOB;
        this.gender = gender;
        this.SSN = SSN;
        this.salary = salary;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getSSN() {
        return SSN;
    }

    public void setSSN(int SSN) {
        this.SSN = SSN;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "firstName='" + firstName + '\'' +
                ", surname='" + surname + '\'' +
                ", DOB='" + DOB + '\'' +
                ", gender='" + gender + '\'' +
                ", SSN=" + SSN +
                ", salary=" + salary +
                '}';
    }
}
