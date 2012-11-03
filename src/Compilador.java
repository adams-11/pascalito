
import ast.*;


/**
 *
 * @author ADAMS
 */
public class Compilador {

    private java.util.Scanner in = new java.util.Scanner(System.in);
    private NodoBase root;
    //private NodoProcedimientoDeclaracion rootProcedimientos;

    public Compilador(NodoBase root, NodoBase rootProcedimientos) {
        this.root = root;
        //this.rootProcedimientos = (NodoProcedimientoDeclaracion) rootProcedimientos;
    }
    
    public void start() {
//        NodoProcedimientoDeclaracion nodoActual = rootProcedimientos;
//        while (nodoActual != null) {
//            procedimientos.put(nodoActual.getNombre(), nodoActual);
//            nodoActual = (NodoProcedimientoDeclaracion) nodoActual.getHermanoDerecha();
//        }

        System.out.println("");
        System.out.println("-----------------------------------");
        System.out.println("");
        
        interpretarNodo(root);

        System.out.println("");
        System.out.println("-----------------------------------");
    }
    
    private void interpretarNodo(NodoBase nodo) {
        NodoBase nodoActual = nodo;
        while (nodoActual != null) {
            if (nodoActual instanceof NodoEscribir) {
                nodoEscribir((NodoEscribir) nodoActual);
            }
            nodoActual = nodoActual.getHermanoDerecha();
        }
    }
    
     private void nodoEscribir(NodoEscribir nodoEscribir) {
        NodoBase valor = nodoEscribir.getValor();

        //Para todos los valores del print
        do {
             if (valor instanceof NodoCadena) {
                NodoCadena nodoCadena = (NodoCadena) valor;
                String cadena = nodoCadena.getCadena();
                //elimina las comillas
                cadena = cadena.substring(1, cadena.length() - 1);
                System.out.print(cadena);
            }
            //si es una sentencia print con varios valores separados por ","
            valor = valor.getHermanoDerecha();
            if (valor != null) {
                System.out.print(" ");
            }
        } while (valor != null);

        if (nodoEscribir.isSaltoDeLinea()) {    //Termina con nueva linea
            System.out.println("");
        } else {                                //Si termina con ;
            System.out.print(" ");
        }
    }
}
