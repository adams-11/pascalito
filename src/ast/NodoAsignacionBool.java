package ast;
/**
 * Nodo utilizado para asociar el nombre de la variable con su valor.
 * El valor puede ser, un numero, una expresion numerica o otra variable.
 * 
 */
public class NodoAsignacionBool extends NodoBase {

    /**
     * Nombre de la variable.
     */
    private String variable;
    /**
     * El valor asignado a la varible.
     */
    private NodoNumero valor;

    /**
     * Crea una instancia de NodoAsignacion sin valores.
     */
    public NodoAsignacionBool() {
    }

    /**
     * Crea una instancia de NodoAsignacion con el par nombre de la variable y
     * el valor que se le quiere asignar.
     * 
     * @param variable el nombre de la variable
     * @param valor el valor que se le asignara
     */
    public NodoAsignacionBool(String variable, NodoNumero valor) {
        this.variable = variable;
        this.valor = valor;
    }

    /**
     * Obtiene el nombre de la variable.
     * 
     * @return el nombre de la variable 
     */
    public String getIdentificador() {
        return variable;
    }

    /**
     * Obtiene el valor de la variable.
     * 
     * @return el valor de la variable 
     */
    public NodoNumero getValor() {
        return valor;
    }
}