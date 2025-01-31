package Sistema;

public class LivroFisico extends Livro{
    private static final long serialVersionUID = 1L;
    private int estoque;
    private int qtdPaginas;

    public LivroFisico(String titulo, String autor, String editora, String categoria, String sinopse, int codigo,
                       int anoPublicacao, int estoque, int qtdPaginas) {
        super(titulo, autor, editora, categoria, sinopse, codigo, anoPublicacao);
        this.estoque = estoque;
        this.qtdPaginas = qtdPaginas;
    }

    public int getEstoque() {
        return estoque;
    }

    public void atualizarEstoque(int estoque){
        this.estoque = estoque;
    }

    public int getQtdPaginas() {
        return qtdPaginas;
    }

    public void setQtdPaginas(int qtdPaginas) {
        this.qtdPaginas = qtdPaginas;
    }
}
