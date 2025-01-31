package classes;
import java.util.ArrayList;
import java.util.List;

public class Categoria {
    //Declarando atributos
    private int codigo;
    private String nome;
    private String descricao;
    private List<Produto> listaProdutos;

    //Construtor da classe
    public Categoria(int codigo, String nome, String descricao) {
        this.codigo = codigo;
        this.nome = nome;
        this.descricao = descricao;
        this.listaProdutos = new ArrayList<>();
    }

    //getters e setters
    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    //Adiciona um produto a lista de produtos presente na classe categoria
    public void adicionarProduto(Produto produto){
        listaProdutos.add(produto);
    }

    //Remove um produto da lista de produtos...
    public void removerProduto(int id){
        Produto produto = buscarProduto(id); //Encontrando o produto a ser removido
        if(produto != null){                //Caso encontrado
            listaProdutos.remove(produto);
            System.out.println("Produto " + produto.getNome() + " removido com sucesso!");
        }
        else{                             //Caso não encontrado...
            System.out.println("Produto não encontrado...");
        }
    }

    //Método de busca de um produto
    public Produto buscarProduto(int id){
        Produto produto;
        for (int i = 0; i < listaProdutos.size(); i++) {   //Percorre a lista de produtos até encontrar o produto em questão
            produto = listaProdutos.get(i);
            if (produto.getId() == id) {
                return produto; // Retorna o produto se o ID for encontrado
            }
        }
        return null; // Retorna null se o produto com o ID fornecido não for encontrado
    }
    public List<Produto> listarProdutos() {
        return listaProdutos;
    }

}
