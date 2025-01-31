package org.example.demo;

import Sistema.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class EmprestimosController {

    @FXML
    private ListView<String> emprestimosListView;
    private Usuario user;

    public void setUser(Usuario user) {
        this.user = user;
        loadEmprestimos();
    }

    private void loadEmprestimos() {
        if (user != null && user.getListaEmprestimos() != null && !user.getListaEmprestimos().isEmpty()) {
            for (int i = 0; i < user.getListaEmprestimos().size(); i++) {
                emprestimosListView.getItems().add(user.getListaEmprestimos().get(i).getLivro().getTitulo());
            }
        } else {
            emprestimosListView.getItems().add("Sem empréstimos no momento");
        }
    }
}
