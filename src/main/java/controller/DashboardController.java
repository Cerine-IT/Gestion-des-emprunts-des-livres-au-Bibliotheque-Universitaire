package controller;

import dao.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class DashboardController {
    // Properly declare all FXML elements with correct types
    @FXML private Label totalLivresLabel;
    @FXML private Label totalEtudiantsLabel;
    @FXML private Label livresEmpruntesLabel;
    @FXML private Label livresRetournesLabel;
    @FXML private PieChart empruntsPieChart;

    // Initialize method (optional but recommended)
    @FXML
    private void initialize() {
        // Initialization code here
    }

    @FXML
    private void gererLivres() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/javaoracle/views/LivreView.fxml"));
        Stage stage = (Stage) totalLivresLabel.getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    @FXML
    private void gererEtudiants() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/javaoracle/views/EtudiantView.fxml"));
        Stage stage = (Stage) totalEtudiantsLabel.getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    @FXML
    private void gererEmprunts() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/javaoracle/views/EmpruntView.fxml"));
        Stage stage = (Stage) livresEmpruntesLabel.getScene().getWindow();
        stage.setScene(new Scene(root));
    }
}