package org.example.demo;

import Sistema.Loader;
import Sistema.Service;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainPage extends Application {

    private Service service;
    private Loader loader;

    @Override
    public void start(Stage stage) throws IOException {
        // Inicialize os serviços
        service = new Service();
        loader = new Loader(); // Inicialize o Loader

        // Configura o FXMLLoader para o login-view
        FXMLLoader fxmlLoader = new FXMLLoader(MainPage.class.getResource("login-view.fxml"));

        // Carrega o login-view
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);

        // Pega o controlador e passa as dependências
        LoginController loginController = fxmlLoader.getController();
        loginController.setService(service);
        loginController.setBibliotecaLivrosDigitais(loader.getBibliotecaLivrosDigitais());
        loginController.setBibliotecaLivrosFisicos(loader.getBibliotecaLivrosFisicos());

        // Configure a janela
        stage.setTitle("ShelfShare!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
