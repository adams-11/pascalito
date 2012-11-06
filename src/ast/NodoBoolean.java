package ast;

public class NodoBoolean extends NodoBase {

    private boolean valor = false;

    public NodoBoolean() {
    }

    public NodoBoolean(boolean d) {
        valor = d;
    }

    public boolean getValor() {
        return valor;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }
}
