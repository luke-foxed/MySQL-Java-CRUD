package application;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class MainController {

    private boolean isUpdating = false;
    private DBController database = new DBController();
    private ArrayList<Employee> employees = new ArrayList<>();
    private Employee selectedEmployee = null;

    @FXML
    public TextField ssnTextField;

    @FXML
    public DatePicker DOBField;

    @FXML
    public TextField firstNameTextField;

    @FXML
    public TextField surnameTextField;

    @FXML
    public TextField salaryTextField;

    @FXML
    public TextField searchTextField;

    @FXML
    public ComboBox<String> genderComboBox = new ComboBox<>();
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
        if (event.getClickCount() == 1) selectedEmployee = employeeTableView.getSelectionModel().getSelectedItem();
    }

    @FXML
    public void updateEmployee() {
        if (selectedEmployee == null) {
            alertHelper("WARNING", "ERROR", "Please double click an employee from the table first!");
        } else {
            // convert date into suitable MySQL format
            LocalDate date = LocalDate.parse(selectedEmployee.getDOB());

            firstNameTextField.setText(selectedEmployee.getFirstName());
            surnameTextField.setText(selectedEmployee.getSurname());
            DOBField.setValue(date);
            genderComboBox.getSelectionModel().select(selectedEmployee.getGender());
            ssnTextField.setText(String.valueOf(selectedEmployee.getSSN()));
            salaryTextField.setText(String.valueOf(selectedEmployee.getSalary()));
            isUpdating = true;
            addButton.setText("CONFIRM");
        }
    }

    @FXML
    public void deleteEmployee() {
        if (selectedEmployee == null) {
            alertHelper("WARNING", "ERROR", "Please double click an employee from the table first!");
        } else {


            try {
                database.deleteEmployee(selectedEmployee.getID());
                employees = database.getEmployees();
                mapToTable(employees);
                selectedEmployee = null; // reset selected employee

            } catch (SQLException e) {
                alertHelper("WARNING", "DELETING ERROR", e.getMessage());
            }
        }
    }

    @FXML
    public void addEmployee() {

        if (checkValidation()) {
            alertHelper("WARNING", "MISSING FIELDS", "Please fill out all fields!");
        } else {
            int ID = employees.size() + 1; // assign Employee with new ID

            // Format date for mySQL
            String formattedDate = DOBField.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            try {
                Employee employee = new Employee(ID, firstNameTextField.getText(),
                        surnameTextField.getText(),
                        formattedDate,
                        genderComboBox.getValue(),
                        Integer.parseInt(ssnTextField.getText()),
                        Integer.parseInt(salaryTextField.getText()));

                if (isUpdating) {
                    employee.setID(selectedEmployee.getID()); // keep the ID of selected employee
                    database.updateEmployee(employee);
                    selectedEmployee = null; // reset selected employee
                    isUpdating = false; // reset updating flag
                } else {
                    database.addEmployee(employee);
                }

                try {
                    employees = database.getEmployees();
                    mapToTable(employees);
                    clearTextFields();
                    addButton.setText("ADD");

                } catch (SQLException e) {
                    alertHelper("ERROR", "ADDING EMPLOYEE ERROR", e.getMessage());
                }

            } catch (NumberFormatException | SQLException e) {
                alertHelper("ERROR", "ERROR", e.getMessage());
            }
        }
    }

    @FXML
    private void searchEmployee() {
        String surname = searchTextField.getText();

        try {
            ArrayList<Employee> matchedEmployees = database.findEmployee(surname);
            mapToTable(matchedEmployees);

        } catch (SQLException e) {
            alertHelper("ERROR", "SEARCH EMPLOYEE ERROR", e.getMessage());
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
            case "ERROR":
                alert = new Alert(Alert.AlertType.ERROR);
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

    private void clearTextFields() {
        firstNameTextField.clear();
        surnameTextField.clear();
        DOBField.setValue(null);
        ssnTextField.clear();
        salaryTextField.clear();
        genderComboBox.getSelectionModel().clearSelection();
    }

    private boolean checkValidation() {
        boolean isError;
        isError = Arrays.asList(
                firstNameTextField.getText(),
                surnameTextField.getText(),
                DOBField.getValue(),
                ssnTextField.getText(),
                salaryTextField.getText(),
                genderComboBox.getSelectionModel().getSelectedItem()
        ).contains("");
        return isError;
    }
}
