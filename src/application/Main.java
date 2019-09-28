package application;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Main extends Application {

    public static Parent root;
    public static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        try {
            root = FXMLLoader.load(getClass().getResource("/application/sample.fxml"));
            Main.primaryStage = primaryStage;
            primaryStage.setResizable(false);
            primaryStage.setTitle("Employee Manager");
            Scene scene = new Scene(root, 800, 450);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }


}