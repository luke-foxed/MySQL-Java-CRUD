package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MainController {

    private DBController database = new DBController();
    ArrayList<Employee> employees = new ArrayList<>();
    Employee selectedEmployee = null;

    @FXML
    public TextField ssnTextField;

    @FXML
    public TextField dobTextField;

    @FXML
    public TextField firstNameTextField;

    @FXML
    public TextField surnameTextField;

    @FXML
    public TextField salaryTextField;

    @FXML
    public TextField genderTextField;

    @FXML
    public Button addButton;

    @FXML
    public Button updateButton;

    @FXML
    public Button deleteButton;

    @FXML
    public TableView<Employee> employeeTableView = new TableView<>();

    @FXML
    public TableColumn<Employee, Integer> IDColumn;

    @FXML
    public TableColumn<Employee, String> firstNameColumn;

    @FXML
    public TableColumn<Employee, String> surnameColumn;

    @FXML
    public TableColumn<Employee, String> DOBColumn;

    @FXML
    public TableColumn<Employee, String> genderColumn;

    @FXML
    public TableColumn<Employee, Integer> ssnColumn;

    @FXML
    public TableColumn<Employee, Integer> salaryColumn;

    @FXML
    protected void initialize() {
        CompletableFuture.runAsync(() -> {
            try {
                database.run();
                employees = database.getEmployees();
                mapToTable(employees);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    public void clickItem(MouseEvent event) {
        if (event.getClickCount() == 2) selectedEmployee = employeeTableView.getSelectionModel().getSelectedItem();
    }

    @FXML
    public void updateEmployee() {
        if (selectedEmployee == null) {
            alertHelper("WARNING", "ERROR", "Please double click an employee from the table first!");
        } else {
            firstNameTextField.setText(selectedEmployee.getFirstName());
            surnameTextField.setText(selectedEmployee.getSurname());
            dobTextField.setText(selectedEmployee.getDOB());
            genderTextField.setText(selectedEmployee.getGender());
            ssnTextField.setText(String.valueOf(selectedEmployee.getSSN()));
            salaryTextField.setText(String.valueOf(selectedEmployee.getSalary()));
        }
    }

    @FXML
    public void deleteEmployee() throws SQLException {
        if (selectedEmployee == null) {
            alertHelper("WARNING", "ERROR", "Please double click an employee from the table first!");
        } else {
            database.deleteEmployee(selectedEmployee.getID());
            employees = database.getEmployees();
            mapToTable(employees);
        }
    }

    @FXML
    public void addEmployee() throws SQLException {

        // using streams for quicker field validation
        if (Stream.of(
                firstNameTextField.getText(),
                surnameTextField.getText(),
                dobTextField.getText(),
                ssnTextField.getText(),
                salaryTextField.getText(),
                genderTextField.getText()
        ).anyMatch(i -> i.equals(""))) {
            alertHelper("WARNING", "MISSING FIELDS", "Please fill out all fields!");
        } else {
            int ID = employees.size() + 1;
            Employee employee = new Employee(ID, firstNameTextField.getText(),
                    surnameTextField.getText(),
                    dobTextField.getText(),
                    genderTextField.getText(),
                    Integer.parseInt(ssnTextField.getText()),
                    Integer.parseInt(salaryTextField.getText()));
            database.addEmployee(employee);
            employees = database.getEmployees();
            mapToTable(employees);
        }
    }

    private void mapToTable(ArrayList<Employee> employees) {
        IDColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        DOBColumn.setCellValueFactory(new PropertyValueFactory<>("DOB"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        ssnColumn.setCellValueFactory(new PropertyValueFactory<>("SSN"));
        salaryColumn.setCellValueFactory(new PropertyValueFactory<>("salary"));
        employeeTableView.setItems(FXCollections.observableArrayList(employees));
    }

    private void alertHelper(String type, String title, String message) {
        Alert alert;
        switch (type.toUpperCase()) {
            case "WARNING":
                alert = new Alert(Alert.AlertType.WARNING);
                break;
            case "INFORMATION":
                alert = new Alert(Alert.AlertType.INFORMATION);
                break;
            case "CONFIRMATION":
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                break;
            default:
                alert = new Alert(Alert.AlertType.NONE);
        }
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
