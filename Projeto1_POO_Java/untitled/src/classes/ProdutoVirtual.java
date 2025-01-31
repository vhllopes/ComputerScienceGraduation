package classes;

public class ProdutoVirtual extends Produto{
    //Declarando atributos
    private double tamanhoArquivo;
    private String formato;
    private int disponibilidade;

    //Construtor da classe
    public ProdutoVirtual(int id, String nome, double preco, String descricao, String marca, Categoria categoria, double tamanhoArquivo, String formato) {
        super(id, nome, preco, descricao, marca, categoria);
        this.tamanhoArquivo = tamanhoArquivo;
        this.formato = formato;
    }

    //getters e setters
    public double getTamanhoArquivo() {
        return tamanhoArquivo;
    }

    public void setTamanhoArquivo(double tamanhoArquivo) {
        this.tamanhoArquivo = tamanhoArquivo;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public void realizarDownload(){
        System.out.println("Fazendo Download do produto: " + getNome() + "...");
    }

    @Override
    public void atualizarEstoque(int quantidade) {
        this.disponibilidade = quantidade;
    }
}
