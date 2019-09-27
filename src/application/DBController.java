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
        connectionProps.put("user", userName);
        String password = "root";
        connectionProps.put("password", password);

        String serverName = "localhost";
        int portNumber = 3306;
        String dbName = "test";
        conn = DriverManager.getConnection("jdbc:mysql://"
                        + serverName + ":" + portNumber + "/" + dbName,
                connectionProps);

        return conn;
    }

    ArrayList<Employee> getEmployees() throws SQLException {
        ArrayList<Employee> employees = new ArrayList<>();
        String command = "SELECT * FROM " + this.tableName;
        ResultSet result = execute(conn, command);
        while (result.next()) {
            Employee employee = new Employee(
                    result.getInt("ID"),
                    result.getString("FIRST_NAME"),
                    result.getString("SURNAME"),
                    result.getString("DOB"),
                    result.getString("GENDER"),
                    result.getInt("SSN"),
                    result.getInt("SALARY"));
            System.out.println(employee.toString());
            employees.add(employee);
        }

        return employees;
    }

    void deleteEmployee(int employeeID) {
        String command = "DELETE FROM " + this.tableName + " WHERE ID = " + employeeID;
        System.out.println(command);
        execute(conn, command);
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

    private ResultSet execute(Connection conn, String command) {
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

        // Create a table
//        try {
//            String createString =
//                    "CREATE TABLE " + this.tableName + " ( " +
//                            "ID INTEGER NOT NULL, " +
//                            "NAME varchar(40) NOT NULL, " +
//                            "STREET varchar(40) NOT NULL, " +
//                            "CITY varchar(20) NOT NULL, " +
//                            "STATE char(2) NOT NULL, " +
//                            "ZIP char(5), " +
//                            "PRIMARY KEY (ID))";
//            this.executeUpdate(conn, createString);
//            System.out.println("Created a table");
//        } catch (SQLException e) {
//            System.out.println("ERROR: Could not create the table");
//            e.printStackTrace();
//        }
    }
}