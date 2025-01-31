package org.example.demo;

import Sistema.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField senhaField;

    @FXML
    private Button registerButton;

    private Service service;

    @FXML
    private void initialize() {
        registerButton.setOnAction(this::handleRegistro);
    }

    public void setService(Service service) {
        this.service = service;
    }

    private void handleRegistro(ActionEvent event) {
        String nome = nameField.getText();
        String email = emailField.getText();
        String senha = senhaField.getText();

        service.criarUsuario(nome, email, senha);
        showAlert(AlertType.INFORMATION, "Registro concluído!", "Novo usuário registrado com sucesso.");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
            Parent loginView = loader.load();
            LoginController loginController = loader.getController();
            loginController.setService(service); // Passar a instância do Service
            Scene loginScene = new Scene(loginView);
            Stage window = (Stage) registerButton.getScene().getWindow();
            window.setScene(loginScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erro ao carregar a tela de login", "Não foi possível carregar a tela de login.");
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
