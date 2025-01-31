package org.example.demo;

import Sistema.*;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LivrosFisicosController {

    @FXML
    private ListView<String> livrosFisicosListView;
    private Usuario user = new Usuario();

    private Biblioteca<LivroFisico> bibliotecaLivrosFisicos = new Biblioteca<>();
    private static final String LIVROS_FISICOS_FILE = "livros_fisicos.dat";

    private static final String LIVROS_FISICOS_TXT = "C:\\Users\\vitor\\Downloads\\Projeto Biblioteca\\demo\\LivrosFisicos.txt";

    public void setBibliotecaLivrosFisicos(Biblioteca<LivroFisico> bibliotecaLivrosFisicos) throws IOException {
        this.bibliotecaLivrosFisicos = bibliotecaLivrosFisicos;
        carregarLivrosFisicosDeArquivo();
        loadBooks();
    }

    private void loadBooks() {
        livrosFisicosListView.getItems().clear();
        for (LivroFisico livro : bibliotecaLivrosFisicos.obterTodos()) {
            livrosFisicosListView.getItems().add(livro.getTitulo());
        }

        livrosFisicosListView.setOnMouseClicked(event -> handleListClick(event));
    }

    private void handleListClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            String selectedItem = livrosFisicosListView.getSelectionModel().getSelectedItem();
            LivroFisico selectedBook = bibliotecaLivrosFisicos.obterTodos().stream()
                    .filter(livro -> livro.getTitulo().equals(selectedItem))
                    .findFirst().orElse(null);

            if (selectedBook != null) {
                showBookDetails(selectedBook);
            }
        }
    }

    private void showBookDetails(LivroFisico livro) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Detalhes do Livro");
        alert.setHeaderText(livro.getTitulo());
        alert.setContentText(
                "Autor: " + livro.getAutor() +
                        "\nAno: " + livro.getAnoPublicacao() +
                        "\nGênero: " + livro.getCategoria() +
                        "\nSinopse: " + livro.getSinopse()
        );

        ButtonType buttonAlugar = new ButtonType("Alugar");
        alert.getButtonTypes().setAll(buttonAlugar, ButtonType.CLOSE);

        alert.showAndWait().ifPresent(response -> {
            if (response == buttonAlugar) {
                alugarLivro(livro);
            }
        });
    }

    private void alugarLivro(LivroFisico livro) {
        if (livro.getEstoque() > 0) {
            livro.atualizarEstoque(livro.getEstoque() - 1);
            updateFile();
            Emprestimo aluguel = new Emprestimo();
            List<Emprestimo> emprestimos = new ArrayList<Emprestimo>();
            user.setListaEmprestimos(emprestimos);
            aluguel.setLivro(livro);
            user.adicionarEmpréstimo(aluguel);
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Livro Alugado");
            alert.setHeaderText(null);
            alert.setContentText("Você alugou o livro: " + livro.getTitulo());
            alert.showAndWait();
            loadBooks();
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Estoque Insuficiente");
            alert.setHeaderText(null);
            alert.setContentText("O livro " + livro.getTitulo() + " não está disponível no momento.");
            alert.showAndWait();
        }
    }

    private void carregarLivrosFisicosDeArquivo() throws IOException {
        File file = new File(LIVROS_FISICOS_TXT);
        if (!file.exists()) {
            System.out.println("Arquivo de livros físicos não encontrado.");
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
                    int estoque = Integer.parseInt(dados[7]);
                    int qtdPaginas = Integer.parseInt(dados[8]);

                    LivroFisico livroFisico = new LivroFisico(titulo, autor, editora, categoria, sinopse,
                            codigo, anoPublicacao, estoque, qtdPaginas);
                    bibliotecaLivrosFisicos.adicionar(livroFisico);
                }
            }
        }
    }

    private void updateFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(LIVROS_FISICOS_TXT))) {
            bw.write("Titulo;Autor;Editora;Categoria;Sinopse;Codigo;AnoPublicacao;Estoque;QtdPaginas\n");
            for (LivroFisico livro : bibliotecaLivrosFisicos.obterTodos()) {
                bw.write(String.format("%s;%s;%s;%s;%s;%d;%d;%d;%d\n",
                        livro.getTitulo(), livro.getAutor(), livro.getEditora(), livro.getCategoria(),
                        livro.getSinopse(), livro.getCodigo(), livro.getAnoPublicacao(), livro.getEstoque(),
                        livro.getQtdPaginas()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setUser(Usuario user) {
        this.user = user;
    }

    public Biblioteca<LivroFisico> getBibliotecaLivrosFisicos() {
        return bibliotecaLivrosFisicos;
    }
}
