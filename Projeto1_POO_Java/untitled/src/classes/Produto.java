package classes;

public abstract class Produto {
    //Declarando os atributos de "Produto"
    private int id;
    private String nome;
    private double preco;
    private String descricao;
    private String marca;
    Categoria categoria;

    //Construtor de "Produto"
    public Produto(int id, String nome, double preco, String descricao, String marca, Categoria categoria){
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.descricao = descricao;
        this.marca = marca;
        this.categoria = categoria;
    }


    //getters e setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public abstract void atualizarEstoque(int quantidade);

    public void atualizarPreco(double novoPreco){
        this.preco = novoPreco;
    }

}
