package org.example.demo;

import Sistema.Service;
import Sistema.Bibliotecario;
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
import java.util.Optional;

public class AdminLoginController {
    @FXML
    private Button loginButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField cpfField;

    private Service service;

    @FXML
    private void initialize() {
        loginButton.setOnAction(this::handleLogin);
    }

    public void setService(Service service) {
        this.service = service;
    }

    private void handleLogin(ActionEvent event) {
        String cpf = cpfField.getText();
        String password = passwordField.getText();

        Optional<Bibliotecario> bibliotecarioOpt = service.autenticarBibliotecario(cpf, password);
        if (bibliotecarioOpt.isPresent()) {
            showAlert(AlertType.INFORMATION, "Login realizado com sucesso!", "Bem Vindo, " + bibliotecarioOpt.get().getCpf() + "!");
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/bibliotecario-main.fxml"));
                Parent mainView = loader.load();
                Scene mainScene = new Scene(mainView);
                Stage window = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                window.setScene(mainScene);
                window.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert(AlertType.ERROR, "Login Inválido!", "CPF ou senha incorretos.");
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
