package ast;

public class NodoDeclaracion extends NodoBase {

    private NodoIdentificador variable;    
    private Tipo.Variable tipo;

    public NodoDeclaracion() {
    }

    public NodoDeclaracion(NodoBase nombre, Tipo.Variable  tipo) {
        this.variable = (NodoIdentificador)nombre;
        this.tipo = tipo;
    }

    public NodoIdentificador getVariable() {
        return variable;
    }

    public Tipo.Variable  getTipo() {
        return tipo;
    }
}
