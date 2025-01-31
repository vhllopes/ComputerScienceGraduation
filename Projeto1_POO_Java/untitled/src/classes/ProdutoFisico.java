package classes;

public class ProdutoFisico extends Produto{
    //Declarando atributos
    private double peso;
    private String dimensoes;
    private int estoque;

    //Construtor da classe
    public ProdutoFisico(int id, String nome, double preco, String descricao, String marca, Categoria categoria, double peso, String dimensoes) {
        super(id, nome, preco, descricao, marca, categoria);
        this.peso = peso;
        this.dimensoes = dimensoes;
    }

    //getters e setters
    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public String getDimensoes() {
        return dimensoes;
    }

    public void setDimensoes(String dimensoes) {
        this.dimensoes = dimensoes;
    }

    //método para cálcular frete
    public double calcularFrete(){
        double taxaFrete = 2.5;    //valor ilustrativo...
        double valorFrete = getPeso() * taxaFrete;
        return valorFrete;
    }

    @Override
    public void atualizarEstoque(int quantidade) {
        this.estoque = quantidade;
    }
}
