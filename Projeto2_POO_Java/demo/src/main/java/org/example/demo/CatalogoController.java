package org.example.demo;

import Sistema.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import java.io.IOException;

public class CatalogoController {

    @FXML
    private Button livroFisicoButton;

    @FXML
    private Button ebooksButton;

    private Biblioteca<LivroFisico> bibliotecaLivrosFisicos = new Biblioteca<>();
    private Biblioteca<LivroDigital> bibliotecaLivrosDigitais = new Biblioteca<>();
    private Usuario user;

    public void setBibliotecaLivrosFisicos(Biblioteca<LivroFisico> bibliotecaLivrosFisicos) {
        this.bibliotecaLivrosFisicos = bibliotecaLivrosFisicos;
    }

    public void setBibliotecaLivrosDigitais(Biblioteca<LivroDigital> bibliotecaLivrosDigitais) {
        this.bibliotecaLivrosDigitais = bibliotecaLivrosDigitais;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }

    @FXML
    public void initialize() {
        livroFisicoButton.setOnAction(event -> loadLivrosFisicosView());
        ebooksButton.setOnAction(event -> loadEbooksView());
    }

    private void loadLivrosFisicosView() {
        Plano planoAtual = user.getPlanoAtual();
        if(planoAtual != null){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/livros_fisicos-view.fxml"));
                Parent root = loader.load();

                LivrosFisicosController controller = loader.getController();
                controller.setBibliotecaLivrosFisicos(bibliotecaLivrosFisicos);
                controller.setUser(this.user);

                Stage stage = new Stage();
                stage.setTitle("Livros Físicos");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            showAlert(AlertType.ERROR, "Acesso Negado", "Catálogo indisponível para seu plano.");
        }
    }

    private void loadEbooksView() {
        if (user != null) {
            Plano planoAtual = user.getPlanoAtual();
            if (planoAtual != null && "Premium".equalsIgnoreCase(planoAtual.getTipo())) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/ebooks-view.fxml"));
                    Parent root = loader.load();

                    EbooksController controller = loader.getController();
                    controller.setBibliotecaLivrosDigitais(bibliotecaLivrosDigitais);

                    Stage stage = new Stage();
                    stage.setTitle("Ebooks");
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                showAlert(AlertType.ERROR, "Acesso Negado", "Catálogo indisponível para seu plano.");
            }
        } else {
            showAlert(AlertType.ERROR, "Acesso Negado", "Catálogo indisponível para seu plano.");
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
