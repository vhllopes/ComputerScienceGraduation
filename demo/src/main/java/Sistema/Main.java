package Sistema;

import java.io.*;
import java.util.*;

class Main {
    private static Biblioteca<LivroFisico> bibliotecaLivrosFisicos = new Biblioteca<>();
    private static Biblioteca<LivroDigital> bibliotecaLivrosDigitais = new Biblioteca<>();
    private static List<Usuario> usuarios = new ArrayList<>();
    private static List<Bibliotecario> bibliotecarios = new ArrayList<>();
    private static final String LIVROS_FISICOS_FILE = "livros_fisicos.dat";
    private static final String LIVROS_DIGITAIS_FILE = "livros_digitais.dat";
    private static final String USUARIOS_FILE = "usuarios.dat";
    private static final String BIBLIOTECARIOS_FILE = "bibliotecarios.dat";
    private static final String LIVROS_FISICOS_TXT = "C:\\Users\\vitor\\Downloads\\Projeto Biblioteca\\demo\\LivrosFisicos.txt";
    private static final String LIVROS_DIGITAIS_TXT = "C:\\Users\\vitor\\Downloads\\Projeto Biblioteca\\demo\\LivrosDigitais.txt";


    public static void main(String[] args) {

        try {
            carregarLivrosDeArquivosTexto();
            carregarDados();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erro ao carregar dados: " + e.getMessage());
        }
        //carregando arquivos de texto
        try {
            carregarLivrosDigitaisDeArquivo();
        } catch (IOException e) {
            System.out.println("Erro ao carregar dados: " + e.getMessage());
        }
        //carregando arquivos de texto
        try {
            carregarLivrosFisicosDeArquivo();
        } catch (IOException e) {
            System.out.println("Erro ao carregar dados: " + e.getMessage());
        }

        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            exibirMenuPrincipal();
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine(); // Limpar o buffer do scanner

            switch (opcao) {
                case 1:
                    criarNovoUsuario(scanner);
                    break;
                case 2:
                    criarNovoBibliotecario(scanner);
                    break;
                case 3:
                    adicionarLivro(scanner);
                    break;
                case 4:
                    exibirDados(scanner);
                    break;
                case 0:
                    System.out.println("Saindo do sistema...");
                    break;
                default:
                    System.out.println("Opção inválida! Por favor, escolha novamente.");
                    break;
            }
        } while (opcao != 0);

        scanner.close();

        try {
            salvarDados();
        } catch (IOException e) {
            System.out.println("Erro ao salvar dados: " + e.getMessage());
        }
    }

    private static void exibirMenuPrincipal() {
        System.out.println("\n### BIBLIOTECA VIRTUAL ###");
        System.out.println("1. Criar Novo Usuário");
        System.out.println("2. Criar Novo Bibliotecário");
        System.out.println("3. Adicionar Livro");
        System.out.println("4. Exibir Dados Cadastrados");
        System.out.println("0. Sair do Sistema");
    }

    private static void criarNovoUsuario(Scanner scanner) {
        System.out.print("Digite o nome do usuário: ");
        String nome = scanner.nextLine();
        System.out.print("Digite o email do usuário: ");
        String email = scanner.nextLine();
        System.out.print("Digite a senha do usuário: ");
        String senha = scanner.nextLine();
        System.out.print("Qual o plano? (1-premium / 2-standard): ");
        int idPlano = scanner.nextInt();
        scanner.nextLine(); // Limpar o buffer do scanner

        int id = usuarios.size() + 1;

        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenha(senha);
        usuario.setId(id);
        if (idPlano == 1) {
            usuario.setPlanoAtual(new PlanoPremium());
        } else {
            usuario.setPlanoAtual(new PlanoStandard());
        }
        usuario.setListaEmprestimos(new ArrayList<>());

        usuarios.add(usuario);
        System.out.println("Usuário criado com sucesso!");
    }

    private static void criarNovoBibliotecario(Scanner scanner) {
        System.out.print("Digite o nome do bibliotecário: ");
        String cpf = scanner.nextLine();
        System.out.print("Digite a senha: ");
        String senha = scanner.nextLine();
        System.out.print("Digite o ID do bibliotecário: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Bibliotecario bibliotecario = new Bibliotecario();
        bibliotecario.setCpf(cpf);
        bibliotecario.setSenha(senha);
        bibliotecario.setID(id);

        bibliotecarios.add(bibliotecario);
        System.out.println("Bibliotecário criado com sucesso!");
    }

    private static void adicionarLivro(Scanner scanner) {
        System.out.println("1. Adicionar Livro Físico");
        System.out.println("2. Adicionar Livro Digital");
        System.out.print("Escolha uma opção: ");
        int opcao = scanner.nextInt();
        scanner.nextLine(); // Limpar o buffer do scanner

        System.out.print("Digite o título do livro: ");
        String titulo = scanner.nextLine();
        System.out.print("Digite o autor do livro: ");
        String autor = scanner.nextLine();
        System.out.print("Digite a editora do livro: ");
        String editora = scanner.nextLine();
        System.out.print("Digite a categoria do livro: ");
        String categoria = scanner.nextLine();
        System.out.print("Digite a sinopse do livro: ");
        String sinopse = scanner.nextLine();
        System.out.print("Digite o código do livro: ");
        int codigo = scanner.nextInt();
        System.out.print("Digite o ano de publicação do livro: ");
        int anoPublicacao = scanner.nextInt();
        scanner.nextLine(); // Limpar o buffer do scanner

        if (opcao == 1) {
            System.out.print("Digite o estoque do livro físico: ");
            int estoque = scanner.nextInt();
            System.out.print("Digite a quantidade de páginas do livro físico: ");
            int qtdPaginas = scanner.nextInt();
            scanner.nextLine(); // Limpar o buffer do scanner

            LivroFisico livroFisico = new LivroFisico(titulo, autor, editora, categoria, sinopse, codigo, anoPublicacao, estoque, qtdPaginas);
            bibliotecaLivrosFisicos.adicionar(livroFisico);
            System.out.println("Livro físico adicionado com sucesso!");
        } else if (opcao == 2) {
            System.out.print("Digite o formato do livro digital: ");
            String formato = scanner.nextLine();
            System.out.print("Digite o tamanho do arquivo do livro digital: ");
            String tamanhoArquivo = scanner.nextLine();

            LivroDigital livroDigital = new LivroDigital(titulo, autor, editora, categoria, sinopse, codigo, anoPublicacao, formato, tamanhoArquivo);
            bibliotecaLivrosDigitais.adicionar(livroDigital);
            System.out.println("Livro digital adicionado com sucesso!");
        } else {
            System.out.println("Opção inválida! Voltando ao menu principal.");
        }
    }

    private static void exibirDados(Scanner scanner) {
        System.out.println("1. Exibir Usuários");
        System.out.println("2. Exibir Bibliotecários");
        System.out.println("3. Exibir Livros Físicos");
        System.out.println("4. Exibir Livros Digitais");
        System.out.print("Escolha uma opção: ");
        int opcao = scanner.nextInt();
        scanner.nextLine(); // Limpar o buffer do scanner

        switch (opcao) {
            case 1:
                exibirUsuarios();
                break;
            case 2:
                exibirBibliotecarios();
                break;
            case 3:
                exibirLivrosFisicos();
                break;
            case 4:
                exibirLivrosDigitais();
                break;
            default:
                System.out.println("Opção inválida! Voltando ao menu principal.");
                break;
        }
    }

    private static void exibirUsuarios() {
        System.out.println("\n### LISTA DE USUÁRIOS ###");
        for (Usuario usuario : usuarios) {
            System.out.println("Nome: " + usuario.getNome());
        }
    }

    private static void exibirBibliotecarios() {
        System.out.println("\n### LISTA DE BIBLIOTECÁRIOS ###");
        for (Bibliotecario bibliotecario : bibliotecarios) {
            System.out.println("Nome: " + bibliotecario.getCpf());
        }
    }

    private static void exibirLivrosFisicos() {
        System.out.println("\n### LISTA DE LIVROS FÍSICOS ###");
        for (LivroFisico livro : bibliotecaLivrosFisicos.obterTodos()) {
            System.out.println("Título: " + livro.getTitulo());
        }
    }

    private static void exibirLivrosDigitais() {
        System.out.println("\n### LISTA DE LIVROS DIGITAIS ###");
        for (LivroDigital livro : bibliotecaLivrosDigitais.obterTodos()) {
            System.out.println("Título: " + livro.getTitulo());
        }
    }

    private static void carregarLivrosDeArquivosTexto() throws IOException {
        carregarLivrosFisicosDeArquivo();
        carregarLivrosDigitaisDeArquivo();
    }

    private static void carregarLivrosFisicosDeArquivo() throws IOException {
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

    private static void carregarLivrosDigitaisDeArquivo() throws IOException {
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

    private static void carregarDados() throws IOException, ClassNotFoundException {
        File livrosDigitais = new File(LIVROS_DIGITAIS_FILE);
        File livrosFisicos = new File(LIVROS_FISICOS_FILE);
        File usuariosFile = new File(USUARIOS_FILE);
        File bibliotecariosFile = new File(BIBLIOTECARIOS_FILE);

        if (livrosDigitais.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(livrosDigitais))) {
                bibliotecaLivrosDigitais = (Biblioteca<LivroDigital>) ois.readObject();
            }
        }

        if (livrosFisicos.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(livrosFisicos))) {
                bibliotecaLivrosFisicos = (Biblioteca<LivroFisico>) ois.readObject();
            }
        }

        if (usuariosFile.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(usuariosFile))) {
                usuarios = (List<Usuario>) ois.readObject();
            }
        }

        if (bibliotecariosFile.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(bibliotecariosFile))) {
                bibliotecarios = (List<Bibliotecario>) ois.readObject();
            }
        }
    }

    private static void salvarDados() throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(LIVROS_FISICOS_FILE))) {
            oos.writeObject(bibliotecaLivrosFisicos);
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(LIVROS_DIGITAIS_FILE))) {
            oos.writeObject(bibliotecaLivrosDigitais);
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USUARIOS_FILE))) {
            oos.writeObject(usuarios);
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BIBLIOTECARIOS_FILE))) {
            oos.writeObject(bibliotecarios);
        }
    }
}
