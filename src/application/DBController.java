package application;

import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

class DBController {

    private final String tableName = "employees";
    private Connection conn = null;

    private Connection getConnection() throws SQLException {
        Connection conn = null;
        Properties connectionProps = new Properties();
        String userName = "root";
        String password = "root";
        connectionProps.put("user", userName);
        connectionProps.put("password", password);

        String serverName = "localhost";
        int portNumber = 3306;
        String dbName = "test";
        conn = DriverManager.getConnection(
                "jdbc:mysql://" + serverName + ":" +
                        portNumber + "/" + dbName, connectionProps);
        return conn;
    }

    ArrayList<Employee> getEmployees() throws SQLException {
        ArrayList<Employee> employees = new ArrayList<>();
        String command = "SELECT * FROM " + this.tableName;
        ResultSet result = executeSelectQuery(conn, command);
        return formatEmployees(employees, result);
    }

    void deleteEmployee(int employeeID) {
        String command = "DELETE FROM " + this.tableName + " WHERE ID = " + employeeID;
        System.out.println(command);
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(command);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void addEmployee(Employee employee) {
        String command =
                "INSERT INTO `employees`(`ID`, `FIRST_NAME`, `SURNAME`, `SSN`, `DOB`, `SALARY`, `GENDER`) VALUES (" +
                        "\'" + employee.getID() + "\'," +
                        "\'" + employee.getFirstName() + "\'," +
                        "\'" + employee.getSurname() + "\'," +
                        "\'" + employee.getSSN() + "\'," +
                        "\'" + employee.getDOB() + "\'," +
                        "\'" + employee.getSalary() + "\'," +
                        "\'" + employee.getGender() + "\'" + ")";
        try {
            Statement stmt = conn.createStatement();
            System.out.println(command);
            stmt.execute(command);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void updateEmployee(Employee employee) throws SQLException{
        System.out.println("Updating");
        String command = "UPDATE `employees` SET " +
                "`FIRST_NAME`= \'" + employee.getFirstName() + "\'," +
                "`SURNAME`= \'" + employee.getSurname() + "\'," +
                "`SSN`= \'" + employee.getSSN() + "\'," +
                "`DOB`= \'" + employee.getDOB() + "\'," +
                "`SALARY`= \'" + employee.getSalary() + "\'," +
                "`GENDER`= \'" + employee.getGender() + "\'" +
                " WHERE `ID` =" + employee.getID();

            Statement stmt = conn.createStatement();
            System.out.println(command);
            stmt.executeUpdate(command);
    }

    ArrayList<Employee> findEmployee(String surname) throws SQLException {
        ArrayList<Employee> employees = new ArrayList<>();
        String command = "SELECT * FROM `employees` WHERE `SURNAME` = \'" + surname + "\'";
        ResultSet result = executeSelectQuery(conn, command);
        return formatEmployees(employees, result);
    }

    private ArrayList<Employee> formatEmployees(ArrayList<Employee> employees, ResultSet result) throws SQLException {
        while (result.next()) {
            Employee employee = new Employee(
                    result.getInt("ID"),
                    result.getString("FIRST_NAME"),
                    result.getString("SURNAME"),
                    result.getString("DOB"),
                    result.getString("GENDER"),
                    result.getInt("SSN"),
                    result.getInt("SALARY"));
            employees.add(employee);
        }
        return employees;
    }

    private ResultSet executeSelectQuery(Connection conn, String command) {
        Statement stmt;
        ResultSet result = null;
        try {
            stmt = conn.createStatement();
            result = stmt.executeQuery(command);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    void run() {
        try {
            conn = this.getConnection();
            System.out.println("Connected to database");
        } catch (SQLException e) {
            System.out.println("ERROR: Could not connect to the database");
            e.printStackTrace();
        }
    }
}