package Sistema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Biblioteca<T extends Livro> implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<T> listaObjetos = new ArrayList<>();

    public Biblioteca() {
        this.listaObjetos = new ArrayList<>();
    }

    public void adicionar(T objeto) {
        listaObjetos.add(objeto);
    }

    public void remover(T objeto) {
        listaObjetos.remove(objeto);
    }

    public List<T> obterTodos() {
        return listaObjetos;
    }
}
