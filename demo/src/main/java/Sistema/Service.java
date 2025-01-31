package Sistema;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Service {
    private List<Usuario> usuarios;
    private List<Bibliotecario> bibliotecarios;

    public Service() {
        usuarios = new ArrayList<>();
        bibliotecarios = new ArrayList<>();
    }

    public void criarUsuario(String nome, String email, String senha) {
        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenha(senha);
        usuario.setId(usuarios.size() + 1);
        usuarios.add(usuario);
    }

    public Optional<Usuario> autenticarUsuario(String email, String senha) {
        return usuarios.stream()
                .filter(u -> u.getEmail().equals(email) && u.getSenha().equals(senha))
                .findFirst();
    }

    public Optional<Bibliotecario> autenticarBibliotecario(String cpf, String senha) {
        return bibliotecarios.stream()
                .filter(b -> b.getCpf().equals(cpf) && b.getSenha().equals(senha))
                .findFirst();
    }

}
