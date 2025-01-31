package classes;

import java.util.ArrayList;
import java.util.List;

public class Loja {
    //Declarando atributos
    private String nome;
    private String cnpj;
    private String endereco;
    private List<Categoria> listaCategorias;

    //Construtor da classe...
    public Loja(String nome, String cnpj, String endereco) {
        this.nome = nome;
        this.cnpj = cnpj;
        this.endereco = endereco;
        this.listaCategorias = new ArrayList<>();
    }

    //Método para adicionar uma categoria a lista de categorias
    public void adicionarCategoria(Categoria categoria){
        listaCategorias.add(categoria);
    }

    //Método que adiciona um produto a lista de produtos de uma categoria específica
    public void adicionarProduto(Categoria categoria, Produto produto){
        categoria.adicionarProduto(produto);
    }

    //Busca categoria por nome
    public Categoria buscarCategoriaPorNome(String nome){
        Categoria categoria;
        for(int i=0; i<listaCategorias.size(); i++){   //Percorre a lista até encontrar um nome igual ao passado como parâmetro...
            categoria = listaCategorias.get(i);
            if(categoria.getNome().equals(nome)){
                return categoria;   //Retorna a categoria caso encontre
            }
        }
        return null; //Retorna nulo caso não encontre
    }
    public Categoria buscarCategoriaPorId(int id){
        Categoria categoria;
        for(int i=0; i<listaCategorias.size(); i++){
            categoria = listaCategorias.get(i);
            if(categoria.getCodigo() == id){
                return categoria;
            }
        }
        return null;
    }
    public Produto buscarProdutoPorId(int idProduto) {
        Produto produto;
        for (int i=0; i < listaCategorias.size(); i++) {
            produto = listaCategorias.get(i).buscarProduto(idProduto);
            if (produto != null) {
                return produto; // Retorna o produto se encontrado em alguma categoria
            }
        }
        return null; // Retorna null se o produto com o ID fornecido não for encontrado em nenhuma categoria
    }

    //Busca de um produto por seu nome
    public Produto buscarProdutoPorNome(String nomeProduto) {
        for (Categoria categoria : listaCategorias) {
            List<Produto> produtos = categoria.listarProdutos();
            for (Produto produto : produtos) {
                if (produto.getNome().equalsIgnoreCase(nomeProduto)) {
                    return produto; // Retorna o produto se encontrado
                }
            }
        }
        return null; // Retorna null se o produto com o nome fornecido não for encontrado
    }

    //Remove uma categoria da lista
    public void removerCategoria(Categoria categoria){
        listaCategorias.remove(categoria);
    }

    //Remove um produto de uma categoria
    public void removerProduto(Categoria categoria, Produto produto){
        categoria.removerProduto(produto.getId());
    }
}
