package Sistema;

import java.io.Serializable;
import java.util.List;

public class Bibliotecario implements Autenticavel, Serializable {
    private static final long serialVersionUID = 1L;
    private String cpf;
    private String senha;
    private int ID;
    private Biblioteca<Livro> bibliotecaLivros;
    private List<Usuario> listaUsuarios;


    // Getters e setters
    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Biblioteca<Livro> getBibliotecaLivros() {
        return bibliotecaLivros;
    }

    public List<Usuario> getBibliotecaUsuários() {
        return listaUsuarios;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public boolean autenticar(String nome, String senha) {
        if(this.cpf.equals(nome) && this.senha.equals(senha)){
            return true;
        };
        return false;
    }

}
