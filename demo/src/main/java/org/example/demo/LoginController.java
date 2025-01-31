package org.example.demo;

import Sistema.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Hyperlink registerLink;

    @FXML
    private Hyperlink adminLoginLink;

    private Service service;
    private Biblioteca<LivroFisico> bibliotecaLivrosFisicos = new Biblioteca<>();
    private Biblioteca<LivroDigital> bibliotecaLivrosDigitais = new Biblioteca<>();

    @FXML
    private void initialize() {
        loginButton.setOnAction(this::handleLogin);
        registerLink.setOnAction(this::handleRegister);
        adminLoginLink.setOnAction(this::handleAdminLogin);
    }

    public void setService(Service service) {
        this.service = service;
    }

    public void setBibliotecaLivrosFisicos(Biblioteca<LivroFisico> bibliotecaLivrosFisicos) {
        this.bibliotecaLivrosFisicos = bibliotecaLivrosFisicos;
    }

    public void setBibliotecaLivrosDigitais(Biblioteca<LivroDigital> bibliotecaLivrosDigitais) {
        this.bibliotecaLivrosDigitais = bibliotecaLivrosDigitais;
    }

    private void handleLogin(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        Optional<Usuario> usuarioOpt = service.autenticarUsuario(email, password);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            showAlert(AlertType.INFORMATION, "Login realizado com sucesso!", "Bem Vindo, " + usuario.getNome() + "!");
            // Transição para a tela de menu do usuário
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("usuario_menu-view.fxml"));
                Parent menuUsuarioView = loader.load();
                UsuarioMenuController menuUsuarioController = loader.getController();
                menuUsuarioController.setUser(usuario);  // Passe o usuário autenticado para o controlador do menu do usuário
                Scene menuUsuarioScene = new Scene(menuUsuarioView);
                Stage window = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                window.setScene(menuUsuarioScene);
                window.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert(AlertType.ERROR, "Login Inválido!", "Usuário ou senha incorreta.");
        }
    }


    private void handleRegister(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("register-view.fxml"));
            Parent registerView = loader.load();
            RegisterController registerController = loader.getController();
            registerController.setService(service); // Passar a instância do Service
            Scene registerScene = new Scene(registerView);
            Stage window = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            window.setScene(registerScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleAdminLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin_login-view.fxml"));
            Parent adminLoginView = loader.load();
            AdminLoginController adminLoginController = loader.getController();
            adminLoginController.setService(service); // Passar a instância do Service
            Scene adminLoginScene = new Scene(adminLoginView);
            Stage window = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            window.setScene(adminLoginScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
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
