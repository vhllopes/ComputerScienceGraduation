package classes;

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DisplayLoja {
    //Atributos
    private String categorias;
    private String produtoFisico;
    private String produtoVirtual;
    private Produto produtoCarrinho;
    Loja loja = new Loja("Commerce TechGear", "4527636458", "rua Almeida Sanches, 98");

    //Construtor
    public DisplayLoja(String categorias, String produtoFisico, String produtoVirtual) {
        this.loja = loja;
        leituraCategorias(categorias);
        leituraProdutosFisicos(produtoFisico);
        leituraProdutosVirtuais(produtoVirtual);
        exibirInterface();

    }

    //Realiza a leitura das categorias
    public void leituraCategorias(String caminho){
        File arquivo = new File(caminho);
        Scanner scanner = null;
        try{
            scanner = new Scanner(arquivo);
        } catch (FileNotFoundException ex){
            Logger.getLogger(DisplayLoja.class.getName()).log(Level.SEVERE, null, ex);
        }

        while(scanner.hasNextLine()){
            String linha = scanner.nextLine();
            String[] campos = linha.split("#");
            int codigo = Integer.parseInt(campos[0]);
            String nome = campos[1];
            String descricao = campos[2];
            Categoria categoria = new Categoria(codigo, nome, descricao);
            loja.adicionarCategoria(categoria);
        }
        scanner.close();
    }

    //Realiza a leitura dos produtos fisicos
    public void leituraProdutosFisicos(String caminho){
        File arquivo = new File(caminho);
        Scanner scanner = null;
        try{
            scanner = new Scanner(arquivo);
        } catch (FileNotFoundException ex){
            Logger.getLogger(DisplayLoja.class.getName()).log(Level.SEVERE, null, ex);
        }

        while(scanner.hasNextLine()){
            String linha = scanner.nextLine();
            String[] campos = linha.split("#");
            int codigo = Integer.parseInt(campos[0]);
            String nome = campos[1];
            double preco = Double.parseDouble(campos[2]);
            String descricao = campos[3];
            String marca = campos[4];
            int codCategoria = Integer.parseInt(campos[5]);
            double peso = Double.parseDouble(campos[6]);
            String dimensao = campos[7];
            Categoria categoria = loja.buscarCategoriaPorId(codCategoria);
            ProdutoFisico produto = new ProdutoFisico(codigo, nome, preco, descricao, marca, categoria, peso, dimensao);
            loja.adicionarProduto(categoria, produto);
        }
        scanner.close();
    }

    //Realiza a leitura dos produtos virtuais
    public void leituraProdutosVirtuais(String caminho){
        File arquivo = new File(caminho);
        Scanner scanner = null;
        try{
            scanner = new Scanner(arquivo);
        } catch (FileNotFoundException ex){
            Logger.getLogger(DisplayLoja.class.getName()).log(Level.SEVERE, null, ex);
        }

        while(scanner.hasNextLine()){
            String linha = scanner.nextLine();
            String[] campos = linha.split("#");
            int codigo = Integer.parseInt(campos[0]);
            String nome = campos[1];
            double preco = Double.parseDouble(campos[2]);
            String descricao = campos[3];
            String marca = campos[4];
            int codCategoria = Integer.parseInt(campos[5]);
            String tamanhoArquivo = campos[6];
            String formato = campos[7];
            Categoria categoria = loja.buscarCategoriaPorId(codCategoria);

            // Dividir o campo do tamanho do arquivo em valor e unidade
            String[] tamanhoArquivoSplit = tamanhoArquivo.split("\\s+");
            double valorNumerico = Double.parseDouble(tamanhoArquivoSplit[0]);
            String unidadeArquivo = tamanhoArquivoSplit[1];

            ProdutoVirtual produto = new ProdutoVirtual(codigo, nome, preco, descricao, marca, categoria, valorNumerico, formato);
            loja.adicionarProduto(categoria, produto);
        }
        scanner.close();
    }

    //Método responsável por exibir a interface de interação com usuário
    public void exibirInterface() {
        Scanner scanner = new Scanner(System.in);

        int opcao;
        do {
            System.out.println("=== Menu ===");
            System.out.println("1. Buscar Produto");
            System.out.println("2. Adicionar ao Carrinho");
            System.out.println("3. Realizar Compra");
            System.out.println("4. Gerenciar Categoria");
            System.out.println("5. Gerenciar Produto");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    buscarProduto();
                    break;
                case 2:
                    adicionarAoCarrinho(produtoCarrinho);
                    break;
                case 3:
                    realizarCompra();
                    break;
                case 4:
                    gerenciarCategoria();
                    break;
                case 5:
                    gerenciarProduto();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        } while (opcao != 0);

    }

    //Realiza as interações quando o usuário deseja buscar um produto específico
    public void buscarProduto() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite o NOME ou ID do produto que deseja buscar: ");
        String identificador = scanner.nextLine();
        Produto produto;
        try {
            int id = Integer.parseInt(identificador);
            produto = loja.buscarProdutoPorId(id);
        } catch (NumberFormatException e) {
            produto = loja.buscarProdutoPorNome(identificador);
        }
        if (produto != null) {
            produtoCarrinho = produto;
            System.out.println("Produto encontrado:");
            System.out.println("ID: " + produto.getId());
            System.out.println("Nome: " + produto.getNome());
            System.out.println("Preço: " + produto.getPreco());
            System.out.println("Descrição: " + produto.getDescricao());
            System.out.println("Marca: " + produto.getMarca());
            System.out.println("Categoria: " + produto.getCategoria().getNome());
            if(produto instanceof ProdutoFisico){
                System.out.println("Peso: " + ((ProdutoFisico) produto).getPeso());
                System.out.println("Dimensões: " + ((ProdutoFisico) produto).getDimensoes());
            }
            else{
                System.out.println("Tamanho do arquivo: " + ((ProdutoVirtual) produto).getTamanhoArquivo());
                System.out.println("Formato: " + ((ProdutoVirtual) produto).getFormato());
            }
        } else {
            System.out.println("Produto não encontrado.");
        }
    }

    //Adiciona o produto previamente buscado ao carrinho
    public void adicionarAoCarrinho(Produto produto){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Deseja adicionar " + produto.getNome() + " ao carrinho? (1-SIM / 2-NÃO): ");
        int op = scanner.nextInt();
        if(op == 1) {
            System.out.println("Produto '" + produto.getNome() + "' adicionado ao carrinho com sucesso!");
        }
        else if(op==2){
            System.out.println("Produto não adicionado...");
        }
        else{
            System.out.println("Valor de operação incorreto! Processo interrompido...");
        }
    }

    //Realiza a compra do produto que se encontra no carrinho
    public void  realizarCompra(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Deseja realizar a compra do produto " + produtoCarrinho.getNome() + " no valor de R$" + produtoCarrinho.getPreco() + "? (1-SIM/2-NÃO): ");
        int op = scanner.nextInt();
        if(op == 1) {
            loja.removerProduto(produtoCarrinho.getCategoria(), produtoCarrinho);
            System.out.println("Compra realizada com sucesso!");
        }
        else if(op==2){
            System.out.println("Compra cancelada...");
        }
        else{
            System.out.println("Valor de operação incorreto! Processo interrompido...");
        }
    }

    //Gerencia uma categoria fornecida pelo usuário
    public void gerenciarCategoria(){
        Scanner scanner = new Scanner(System.in);
        int escolha;
        do {
            System.out.println("=== Gerenciar Categorias ===");
            System.out.println("1. Adicionar Categoria");
            System.out.println("2. Remover Categoria");
            System.out.println("3. Editar Categoria");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");
            escolha = scanner.nextInt();
            scanner.nextLine();

            switch (escolha) {
                case 1:
                    System.out.println("Código da nova Categoria: ");
                    int novoCod = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Nome da nova Categoria: ");
                    String novoNome = scanner.nextLine();
                    System.out.println("Descrição da nova Categoria: ");
                    String novaDescricao = scanner.nextLine();
                    Categoria categoria = new Categoria(novoCod, novoNome, novaDescricao);
                    loja.adicionarCategoria(categoria);
                    System.out.println("Categoria adicionadda com sucesso!");
                    break;
                case 2:
                    System.out.println("Insira o código da categoria a ser removida: ");
                    int codRemove = scanner.nextInt();
                    scanner.nextLine();
                    Categoria categoriaRemove = loja.buscarCategoriaPorId(codRemove);
                    if(categoriaRemove != null){
                        loja.removerCategoria(categoriaRemove);
                        System.out.println("Categoria removida com sucesso!");
                    }
                    else{
                        System.out.println("Categoria não encontrada...");
                    }

                    break;
                case 3:
                    System.out.print("Digite o código da categoria que deseja editar: ");
                    int codigo = scanner.nextInt();
                    scanner.nextLine();
                    Categoria categoria1 = loja.buscarCategoriaPorId(codigo);
                    if (categoria1 == null) {
                        System.out.println("Categoria não encontrada.");
                        return;
                    }

                    int operacaoCat;
                    do {
                        System.out.println("=== Editar Categoria ===");
                        System.out.println("1. Mudar Código");
                        System.out.println("2. Mudar Nome");
                        System.out.println("3. Mudar Descrição");
                        System.out.println("0. Voltar");
                        System.out.print("Escolha uma opção: ");
                        operacaoCat = scanner.nextInt();
                        scanner.nextLine();

                        switch (operacaoCat) {
                            case 1:
                                System.out.print("Novo código: ");
                                int novoCodigo = scanner.nextInt();
                                scanner.nextLine();
                                if (loja.buscarCategoriaPorId(novoCodigo) != null) {
                                    System.out.println("Já existe uma categoria com esse código.");
                                } else {
                                    categoria1.setCodigo(novoCodigo);
                                    System.out.println("Código atualizado com sucesso.");
                                }
                                break;
                            case 2:
                                System.out.print("Novo nome: ");
                                String novoNomeCategoria1 = scanner.nextLine();
                                categoria1.setNome(novoNomeCategoria1);
                                System.out.println("Nome atualizado com sucesso.");
                                break;
                            case 3:
                                System.out.print("Nova descrição: ");
                                String novaDescricaoCategoria1 = scanner.nextLine();
                                categoria1.setDescricao(novaDescricaoCategoria1);
                                System.out.println("Descrição atualizada com sucesso.");
                                break;
                            case 0:
                                System.out.println("Voltando ao menu anterior...");
                                break;
                            default:
                                System.out.println("Opção inválida! Tente novamente.");
                        }
                    } while (operacaoCat != 0);
                    break;
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        } while (escolha != 0);
    }

    //Gerencia um produto fornecido pelo usuário
    public void gerenciarProduto() {
        Scanner scanner = new Scanner(System.in);
        int escolha;
        do {
            System.out.println("=== Gerenciar Produtos ===");
            System.out.println("1. Adicionar Produto Físico");
            System.out.println("2. Adicionar Produto Virtual");
            System.out.println("3. Remover Produto");
            System.out.println("4. Editar Produto");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");
            escolha = scanner.nextInt();
            scanner.nextLine();

            switch (escolha) {
                case 1:
                    System.out.println("Código do novo Produto Físico: ");
                    int novoCod = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Nome do novo Produto Físico: ");
                    String novoNome = scanner.nextLine();
                    System.out.println("Preço do novo Produto Físico: ");
                    double novoPreco = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.println("Descrição do novo Produto Físico: ");
                    String novaDescricao = scanner.nextLine();
                    System.out.println("Marca do novo Produto Físico: ");
                    String novaMarca = scanner.nextLine();
                    System.out.println("Código da categoria do novo Produto Físico: ");
                    int codCategoria = scanner.nextInt();
                    scanner.nextLine();
                    Categoria categoria = loja.buscarCategoriaPorId(codCategoria);
                    if (categoria == null) {
                        System.out.println("Categoria não encontrada.");
                        return;
                    }
                    System.out.println("Peso do novo Produto Físico: ");
                    double novoPeso = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.println("Dimensões do novo Produto Físico: ");
                    String novasDimensoes = scanner.nextLine();

                    ProdutoFisico produtoFisico = new ProdutoFisico(novoCod, novoNome, novoPreco, novaDescricao, novaMarca, categoria, novoPeso, novasDimensoes);
                    loja.adicionarProduto(categoria, produtoFisico);
                    System.out.println("Produto Físico adicionado com sucesso!");
                    break;
                case 2:
                    System.out.println("Código do novo Produto Virtual: ");
                    int novoCod1 = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Nome do novo Produto Virtual: ");
                    String novoNome1 = scanner.nextLine();
                    System.out.println("Preço do novo Produto Virtual: ");
                    double novoPreco1 = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.println("Descrição do novo Produto Virtual: ");
                    String novaDescricao1 = scanner.nextLine();
                    System.out.println("Marca do novo Produto Virtual: ");
                    String novaMarca1 = scanner.nextLine();
                    System.out.println("Código da categoria do novo Produto Virtual: ");
                    int codCategoria1 = scanner.nextInt();
                    scanner.nextLine();
                    Categoria categoria1 = loja.buscarCategoriaPorId(codCategoria1);
                    if (categoria1 == null) {
                        System.out.println("Categoria não encontrada.");
                        return;
                    }
                    System.out.println("Tamanho do arquivo do novo Produto Virtual: ");
                    double novoTamanho = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.println("Formato do novo Produto Virtual: ");
                    String novoFormato = scanner.nextLine();

                    ProdutoVirtual produtoVirtual = new ProdutoVirtual(novoCod1, novoNome1, novoPreco1, novaDescricao1, novaMarca1, categoria1, novoTamanho, novoFormato);
                    loja.adicionarProduto(categoria1, produtoVirtual);
                    System.out.println("Produto Virtual adicionado com sucesso!");
                    break;
                case 3:
                    System.out.println("Insira o código do produto a ser removido: ");
                    int codProduto = scanner.nextInt();
                    scanner.nextLine();
                    Produto produto = loja.buscarProdutoPorId(codProduto);
                    if (produto == null) {
                        System.out.println("Produto não encontrado.");
                        return;
                    }

                    Categoria categoria2 = produto.getCategoria();
                    loja.removerProduto(categoria2, produto);
                    System.out.println("Produto removido com sucesso!");
                    break;
                case 4:
                    System.out.print("Digite o código do produto que deseja editar: ");
                    int codigo = scanner.nextInt();
                    scanner.nextLine();
                    Produto produtoEditar = loja.buscarProdutoPorId(codigo);
                    if (produtoEditar == null) {
                        System.out.println("Produto não encontrada.");
                        return;
                    }

                    int operacaoProd;
                    do {
                        System.out.println("=== Editar Produto ===");
                        System.out.println("1. Mudar Código");
                        System.out.println("2. Mudar Nome");
                        System.out.println("3. Mudar Descrição");
                        System.out.println("4. Mudar Preço");
                        System.out.println("5. Mudar Marca");
                        System.out.println("0. Voltar");
                        System.out.print("Escolha uma opção: ");
                        operacaoProd = scanner.nextInt();
                        scanner.nextLine();

                        switch (operacaoProd) {
                            case 1:
                                System.out.print("Novo código: ");
                                int novoCodigo = scanner.nextInt();
                                scanner.nextLine();
                                if (loja.buscarCategoriaPorId(novoCodigo) != null) {
                                    System.out.println("Já existe um produto com esse código.");
                                } else {
                                    produtoEditar.setId(novoCodigo);
                                    System.out.println("Código atualizado com sucesso.");
                                }
                                break;
                            case 2:
                                System.out.print("Novo nome: ");
                                String nomeProdutoEditar = scanner.nextLine();
                                produtoEditar.setNome(nomeProdutoEditar);
                                System.out.println("Nome atualizado com sucesso.");
                                System.out.println();
                            case 3:
                                System.out.print("Nova descrição: ");
                                String descricaoProdutoEditar = scanner.nextLine();
                                produtoEditar.setDescricao(descricaoProdutoEditar);
                                System.out.println("Descrição atualizada com sucesso.");
                                break;
                            case 4:
                                System.out.println("Novo preço: ");
                                double precoProdutoEditar = scanner.nextDouble();
                                scanner.nextLine();
                                produtoEditar.atualizarPreco(precoProdutoEditar);
                                System.out.println("Preco atualizado com sucesso.");
                                break;
                            case 5:
                                System.out.println("Nova marca: ");
                                String marcaProdutoEditar = scanner.nextLine();
                                produtoEditar.setMarca(marcaProdutoEditar);
                                System.out.println("Marca atualizada com sucesso.");
                                break;
                            case 0:
                                System.out.println("Voltando ao menu anterior...");
                                break;
                            default:
                                System.out.println("Opção inválida! Tente novamente.");
                        }
                    } while (operacaoProd != 0);
                    break;
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        } while (escolha != 0);
    }


}