package Sistema;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Loader {
    private Biblioteca<LivroFisico> bibliotecaLivrosFisicos = new Biblioteca<>();
    private Biblioteca<LivroDigital> bibliotecaLivrosDigitais = new Biblioteca<>();
    private List<Usuario> usuarios = new ArrayList<>();
    private List<Bibliotecario> bibliotecarios = new ArrayList<>();
    private static final String LIVROS_FISICOS_FILE = "livros_fisicos.dat";
    private static final String LIVROS_DIGITAIS_FILE = "livros_digitais.dat";
    private static final String USUARIOS_FILE = "usuarios.dat";
    private static final String BIBLIOTECARIOS_FILE = "bibliotecarios.dat";
    private static final String LIVROS_FISICOS_TXT = "C:\\Users\\vitor\\Downloads\\Projeto Biblioteca\\demo\\LivrosFisicos.txt";
    private static final String LIVROS_DIGITAIS_TXT = "C:\\Users\\vitor\\Downloads\\Projeto Biblioteca\\demo\\LivrosDigitais.txt";

    public Loader() {
        try {
            criarArquivos(); // Criar arquivos se não existirem
            carregarLivrosDeArquivosTexto();
            carregarDados();

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erro ao carregar dados: " + e.getMessage());
            e.printStackTrace(); // Adicionando stack trace para facilitar a depuração
        }
    }


    private void criarArquivos() throws IOException {
        File livrosFisicosFile = new File(LIVROS_FISICOS_FILE);
        File livrosDigitaisFile = new File(LIVROS_DIGITAIS_FILE);
        File usuariosFile = new File(USUARIOS_FILE);
        File bibliotecariosFile = new File(BIBLIOTECARIOS_FILE);

        livrosFisicosFile.createNewFile();
        livrosDigitaisFile.createNewFile();
        usuariosFile.createNewFile();
        bibliotecariosFile.createNewFile();
    }

    private void carregarLivrosDeArquivosTexto() throws IOException {
        carregarLivrosFisicosDeArquivo();
        carregarLivrosDigitaisDeArquivo();
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

                    LivroFisico livroFisico = new LivroFisico(titulo, autor, editora, categoria, sinopse, codigo, anoPublicacao, estoque, qtdPaginas);
                    bibliotecaLivrosFisicos.adicionar(livroFisico);
                }
            }
        }
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

                    LivroDigital livroDigital = new LivroDigital(titulo, autor, editora, categoria, sinopse, codigo, anoPublicacao, formato, tamanhoArquivo);
                    bibliotecaLivrosDigitais.adicionar(livroDigital);
                }
            }
        }
    }

    private void carregarDados() throws IOException, ClassNotFoundException {
        File usuariosFile = new File(USUARIOS_FILE);
        File bibliotecariosFile = new File(BIBLIOTECARIOS_FILE);

        if (usuariosFile.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(usuariosFile))) {
                usuarios = (List<Usuario>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Erro ao carregar usuários: " + e.getMessage());
                e.printStackTrace();
            }
        }

        if (bibliotecariosFile.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(bibliotecariosFile))) {
                bibliotecarios = (List<Bibliotecario>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Erro ao carregar bibliotecários: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public Biblioteca<LivroFisico> getBibliotecaLivrosFisicos() {
        return bibliotecaLivrosFisicos;
    }

    public Biblioteca<LivroDigital> getBibliotecaLivrosDigitais() {
        return bibliotecaLivrosDigitais;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public List<Bibliotecario> getBibliotecarios() {
        return bibliotecarios;
    }

    public List<LivroFisico> getLivrosFisicos() {
        return bibliotecaLivrosFisicos.obterTodos();
    }

    public List<LivroDigital> getLivrosDigitais() {
        return bibliotecaLivrosDigitais.obterTodos();
    }

}
