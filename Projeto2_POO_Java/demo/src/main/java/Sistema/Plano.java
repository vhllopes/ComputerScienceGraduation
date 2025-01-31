package Sistema;

import java.io.Serializable;

public abstract class Plano implements Serializable {
    private static final long serialVersionUID = 1L;
    private String tipo;
    private double precoMensal;
    private int numMaxEmprest;
    private boolean ebooks;

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getPrecoMensal() {
        return precoMensal;
    }

    public void setPrecoMensal(double precoMensal) {
        this.precoMensal = precoMensal;
    }

    public int getNumMaxEmprest() {
        return numMaxEmprest;
    }

    public void setNumMaxEmprest(int numMaxEmprest) {
        this.numMaxEmprest = numMaxEmprest;
    }

    public boolean isEbooks() {
        return ebooks;
    }

    public void setEbooks(boolean ebooks) {
        this.ebooks = ebooks;
    }
}
