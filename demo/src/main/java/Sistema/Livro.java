package Sistema;

import java.io.Serializable;
public abstract class Livro implements Serializable {
    private static final long serialVersionUID = 1L;
    private String titulo;
    private String autor;
    private String editora;
    private String categoria;
    private String sinopse;
    private int codigo;
    private int anoPublicacao;

    public Livro(String titulo, String autor, String editora, String categoria, String sinopse, int codigo, int anoPublicacao) {
        this.titulo = titulo;
        this.autor = autor;
        this.editora = editora;
        this.categoria = categoria;
        this.sinopse = sinopse;
        this.codigo = codigo;
        this.anoPublicacao = anoPublicacao;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getEditora() {
        return editora;
    }

    public void setEditora(String editora) {
        this.editora = editora;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getSinopse() {
        return sinopse;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public int getAnoPublicacao() {
        return anoPublicacao;
    }

    public void setAnoPublicacao(int anoPublicacao) {
        this.anoPublicacao = anoPublicacao;
    }
}
