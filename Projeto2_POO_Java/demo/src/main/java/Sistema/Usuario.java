package Sistema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Usuario implements Autenticavel, Serializable{
    private static final long serialVersionUID = 1L;
    private String nome;
    private String email;
    private String senha;
    private int id;
    private List<Emprestimo> listaEmprestimos;
    private Plano planoAtual;

    public Usuario(){
        List<Emprestimo> emprestimos = new ArrayList<>();
    }


    public void selecionarPlano(Plano plano){
        this.planoAtual = plano;
    }

    @Override
    public boolean autenticar(String email, String senha) {
        if(this.email.equals(email) && this.senha.equals(senha)){
            return true;
        };
        return false;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Plano getPlanoAtual() {
        return planoAtual;
    }

    public void setPlanoAtual(Plano planoAtual) {
        this.planoAtual = planoAtual;
    }

    public void setListaEmprestimos(List<Emprestimo> listaEmprestimos) {
        this.listaEmprestimos = listaEmprestimos;
    }

    public void adicionarEmpréstimo(Emprestimo emprestimo) {
        listaEmprestimos.add(emprestimo);
    }

    public void removerEmpréstimo(Emprestimo emprestimo) {
        listaEmprestimos.remove(emprestimo);
    }

    public List<Emprestimo> getListaEmprestimos() {
        return listaEmprestimos;
    }

}
