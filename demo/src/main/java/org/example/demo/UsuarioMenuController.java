package org.example.demo;

import Sistema.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioMenuController {

    @FXML
    private BorderPane bp;

    @FXML
    private Button homeButton;

    @FXML
    private Button catalogoButton;

    @FXML
    private Button emprestimosButton;
    @FXML
    private Button exitButton;

    @FXML
    private Button planosButton;

    @FXML
    private Label nameUser;

    @FXML
    private Label emailUser;

    @FXML
    private Label planoUser;
    private Usuario user;

    private static final String USUARIOS_FILE = "usuarios.dat";



    @FXML
    public void initialize() {
        homeButton.setOnAction(event -> loadView("usuario_menu-view.fxml"));
        catalogoButton.setOnAction(event -> loadCatalogoView("catalogo-view.fxml"));
        emprestimosButton.setOnAction(event -> loadEmprestimosView("emprestimos-view.fxml"));
        planosButton.setOnAction(event -> loadPlanoView("planos-view.fxml"));
        exitButton.setOnAction(event -> handleExit());
    }

    private void loadView(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Node node = loader.load();
            bp.setCenter(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void exibirLivrosFisicos(Biblioteca<LivroFisico> bibliotecaLivrosFisicos) {
        System.out.println("\n### LISTA DE LIVROS FÍSICOS ###");
        for (LivroFisico livro : bibliotecaLivrosFisicos.obterTodos()) {
            System.out.println("Título: " + livro.getTitulo());
        }
    }

    private void loadCatalogoView(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Node node = loader.load();

            CatalogoController controller = loader.getController();
            controller.setUser(this.user);

            bp.setCenter(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadPlanoView(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Node node = loader.load();

            PlanosController controller = loader.getController();
            controller.setUser(this.user);

            bp.setCenter(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void loadEmprestimosView(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Node node = loader.load();

            EmprestimosController controller = loader.getController();
            controller.setUser(this.user); // Define o usuário antes de carregar os empréstimos

            bp.setCenter(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setUser(Usuario user) {
        this.user = user;
        nameUser.setText(user.getNome());
        emailUser.setText(user.getEmail());
        planoUser.setText("Não disponível");
    }

    private void handleExit() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Saída");
        alert.setHeaderText("Você quer salvar seus dados antes de sair?");
        alert.setContentText("Selecione uma opção.");

        ButtonType buttonTypeSave = new ButtonType("Salvar");
        ButtonType buttonTypeExitWithoutSaving = new ButtonType("Sair sem salvar");

        alert.getButtonTypes().setAll(buttonTypeSave, buttonTypeExitWithoutSaving);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == buttonTypeSave) {
            salvarDados();
        } else if (result.isPresent() && result.get() == buttonTypeExitWithoutSaving) {
            Platform.exit();
        }
    }

    private void salvarDados() {
        salvarUsuario();
        Platform.exit();
    }

    private void salvarUsuario() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USUARIOS_FILE, true))) {
            oos.writeObject(user);
        } catch (IOException i) {
            i.printStackTrace();
        }
    }
}