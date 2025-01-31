package Sistema;
import java.io.Serializable;

public class Emprestimo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String dataEmprestimo;
    private String dataDevolucaoPrevista;
    private String status;
    private LivroFisico livro;


    public String getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(String dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public String getDataDevolucao() {
        return dataDevolucaoPrevista;
    }

    public void setDataDevolucao(String dataDevolucao) {
        this.dataDevolucaoPrevista = dataDevolucao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LivroFisico getLivro() {
        return livro;
    }

    public void setLivro(LivroFisico livro) {
        this.livro = livro;
    }
}
