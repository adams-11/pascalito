package ast;

/**
 * 
 */
public class NodoBase {

    private NodoBase hermanoDerecha;

    /**
     */
    public NodoBase() {
    }

    /**
     * @param hermanoDerecha el nodo hermano
     */
    public NodoBase(NodoBase hermanoDerecha) {
        this.hermanoDerecha = hermanoDerecha;
    }

    /**
     * @return el nodo hermano
     */
    public NodoBase getHermanoDerecha() {
        return hermanoDerecha;
    }

    /**
     * @param hermanoDerecha el nodo hermano.
     */
    public void setHermanoDerecha(NodoBase hermanoDerecha) {
        this.hermanoDerecha = hermanoDerecha;
    }

    /**
     * @return true si tiene hermanos, false de lo contrario
     */
    public boolean tieneHermano() {
        return (hermanoDerecha != null);
    }
}
