package org.example.demo;

import Sistema.*;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class EbooksController {

    @FXML
    private ListView<String> ebooksListView;

    private static final String LIVROS_DIGITAIS_TXT = "C:\\Users\\vitor\\Downloads\\Projeto Biblioteca\\demo\\LivrosDigitais.txt";

    private Biblioteca<LivroDigital> bibliotecaLivrosDigitais = new Biblioteca<>();

    public void setBibliotecaLivrosDigitais(Biblioteca<LivroDigital> bibliotecaLivrosDigitais) throws IOException {
        this.bibliotecaLivrosDigitais = bibliotecaLivrosDigitais;
        carregarLivrosDigitaisDeArquivo();
        loadBooks();
    }

    private void loadBooks() throws IOException {
        carregarLivrosDigitaisDeArquivo();
        ebooksListView.getItems().clear();
        for (LivroDigital livro : bibliotecaLivrosDigitais.obterTodos()) {
            ebooksListView.getItems().add(livro.getTitulo());
        }

        ebooksListView.setOnMouseClicked(event -> handleListClick(event));
    }

    private void handleListClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            String selectedItem = ebooksListView.getSelectionModel().getSelectedItem();
            LivroDigital selectedBook = bibliotecaLivrosDigitais.obterTodos().stream()
                    .filter(livro -> livro.getTitulo().equals(selectedItem))
                    .findFirst().orElse(null);

            if (selectedBook != null) {
                showBookDetails(selectedBook);
            }
        }
    }

    private void showBookDetails(LivroDigital livro) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Detalhes do Ebook");
        alert.setHeaderText(livro.getTitulo());
        alert.setContentText("Autor: " + livro.getAutor() + "\nAno: " + livro.getAnoPublicacao() + "\nGênero: " + livro.getCategoria() +
                "\nSinopse: " + livro.getSinopse());

        ButtonType buttonDownload = new ButtonType("Download");
        alert.getButtonTypes().setAll(buttonDownload, ButtonType.CLOSE);

        alert.showAndWait().ifPresent(response -> {
            if (response == buttonDownload) {
                // Lógica para download do ebook
                System.out.println("Ebook baixado: " + livro.getTitulo());
            }
        });
    }
    private void carregarLivrosDigitaisDeArquivo() throws IOException {
        File file = new File(LIVROS_DIGITAIS_TXT);
        if (!file.exists()) {
            System.out.println("Arquivo de livros digitais não encontrado.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linha;
            boolean isHeader = true;
            while ((linha = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue; // Pular a primeira linha (cabeçalho)
                }
                String[] dados = linha.split(";");
                if (dados.length == 9) {
                    String titulo = dados[0];
                    String autor = dados[1];
                    String editora = dados[2];
                    String categoria = dados[3];
                    String sinopse = dados[4];
                    int codigo = Integer.parseInt(dados[5]);
                    int anoPublicacao = Integer.parseInt(dados[6]);
                    String formato = dados[7];
                    String tamanhoArquivo = dados[8];

                    LivroDigital livroDigital = new LivroDigital(titulo, autor, editora, categoria, sinopse, codigo,
                            anoPublicacao, formato, tamanhoArquivo);
                    bibliotecaLivrosDigitais.adicionar(livroDigital);
                }
            }
        }
    }

}
