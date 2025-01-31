package Sistema;

public class LivroDigital extends Livro{
    private static final long serialVersionUID = 1L;
    private String formato;
    private String tamanhoArquivo;

    public LivroDigital(String titulo, String autor, String editora, String categoria, String sinopse, int codigo,
                        int anoPublicacao, String formato, String tamanhoArquivo) {
        super(titulo, autor, editora, categoria, sinopse, codigo, anoPublicacao);
        this.formato = formato;
        this.tamanhoArquivo = tamanhoArquivo;
    }

    public String getFormato() {
        return formato;
    }


    public void setFormato(String formato) {
        this.formato = formato;
    }

    public String getTamanhoArquivo() {
        return tamanhoArquivo;
    }

    public void setTamanhoArquivo(String tamanhoArquivo) {
        this.tamanhoArquivo = tamanhoArquivo;
    }

    public void fazerDownload(){
        System.out.println("Download realizado com sucesso!");
    }
}
