package ast;

/**
 *
 * @author Personal
 */
public class NodoOperacionBoolUnaria extends NodoBase {

    private NodoBase valor;
    private boolean negarValor;

    public NodoOperacionBoolUnaria() {
    }

    public NodoOperacionBoolUnaria(NodoBase valor, boolean negarValor) {
        this.valor = valor;
        this.negarValor = negarValor;
    }

    public NodoBase getValor() {
        return valor;
    }

    public boolean isNegarValor() {
        return negarValor;
    }
}
