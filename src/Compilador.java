
import util.TablaSimbolos;
import ast.*;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import util.UtGen;

/**
 *
 * @author ADAMS
 */
public class Compilador {

    private PrintWriter out;
    private java.util.Scanner in = new java.util.Scanner(System.in);
    private NodoBase root;
    //private NodoProcedimientoDeclaracion rootProcedimientos;
    private TablaSimbolos tablaSimbolos = null;
    /*
     * desplazamientoTmp es una variable inicializada en 0 y empleada como el
     * desplazamiento de la siguiente localidad temporal disponible desde la
     * parte superior o tope de la memoria (la que apunta el registro MP).
     *
     * - Se decrementa (desplazamientoTmp--) despues de cada almacenamiento y
     *
     * - Se incrementa (desplazamientoTmp++) despues de cada eliminacion/carga
     * en otra variable de un valor de la pila.
     *
     * Pudiendose ver como el apuntador hacia el tope de la pila temporal y las
     * llamadas a la funcion emitirRM corresponden a una inserccion y extraccion
     * de esta pila
     */
    private static int desplazamientoTmp = 0;

    public Compilador(NodoBase root, NodoBase rootProcedimientos, TablaSimbolos tablaSimbolos, PrintWriter out) {
        this.root = root;
        //this.rootProcedimientos = (NodoProcedimientoDeclaracion) rootProcedimientos;
        this.tablaSimbolos = tablaSimbolos;
        this.out = out;
    }

    public void start() {
//        NodoProcedimientoDeclaracion nodoActual = rootProcedimientos;
//        while (nodoActual != null) {
//            procedimientos.put(nodoActual.getNombre(), nodoActual);
//            nodoActual = (NodoProcedimientoDeclaracion) nodoActual.getHermanoDerecha();
//        }

        System.out.println();
        System.out.println();
        System.out.println("------ CODIGO OBJETO DEL LENGUAJE TINY GENERADO PARA LA TM ------");
        System.out.println();
        System.out.println();
        generarPreludioEstandar();
        interpretarNodo(root);
        /*
         * Genero el codigo de finalizacion de ejecucion del codigo
         */
        UtGen.emitirComentario("Fin de la ejecucion.");
        UtGen.emitirRO("HALT", 0, 0, 0, "", out);
        System.out.println();
        System.out.println();
        System.out.println("------ FIN DEL CODIGO OBJETO DEL LENGUAJE TINY GENERADO PARA LA TM ------");
    }

    private void interpretarNodo(NodoBase nodo) {
        NodoBase nodoActual = nodo;
        while (nodoActual != null) {
            if (nodoActual instanceof NodoEscribir) {
                nodoEscribir((NodoEscribir) nodoActual);
            } else if (nodoActual instanceof NodoNumero) {
                nodoNumero((NodoNumero) nodoActual);
            } else if (nodoActual instanceof NodoAsignacion) {
                nodoAsignacion((NodoAsignacion) nodoActual);
            } else if (nodoActual instanceof NodoIdentificador) {
                nodoIdentificador((NodoIdentificador) nodoActual);
            } else if (nodoActual instanceof NodoOperacionMat) {
                nodoOperacionMat((NodoOperacionMat) nodoActual);
            } else if (nodoActual instanceof NodoOperacionMatUnaria) {
                nodoOperacionMatUnaria((NodoOperacionMatUnaria) nodoActual);
            }
            nodoActual = nodoActual.getHermanoDerecha();
        }
    }

    private void nodoEscribir(NodoEscribir nodoEscribir) {

        if (UtGen.debug) {
            UtGen.emitirComentario("-> escribir");
        }

        NodoBase valor = nodoEscribir.getValor();

        //Para todos los valores del print
        do {
            /*
             * Genero el codigo de la expresion que va a ser escrita en pantalla
             */
            interpretarNodo(valor);
            /*
             * Ahora genero la salida
             */
            UtGen.emitirRO("OUT", UtGen.AC, 0, 0, "escribir: genero la salida de la expresion", out);

            //si es una sentencia print con varios valores separados por ","
            valor = valor.getHermanoDerecha();
            /*
             * if (valor != null) { System.out.print(" "); }
             */
        } while (valor != null);

        if (UtGen.debug) {
            UtGen.emitirComentario("<- escribir");
        }
    }

    private void nodoIdentificador(NodoIdentificador nodo) {
        int direccion;
        if (UtGen.debug) {
            UtGen.emitirComentario("-> identificador");
        }
        direccion = tablaSimbolos.getDireccion(nodo.getNombre());
        UtGen.emitirRM("LD", UtGen.AC, direccion, UtGen.GP, "cargar valor de identificador: " + nodo.getNombre(), out);
        if (UtGen.debug) {
            UtGen.emitirComentario("-> identificador");
        }
    }

    private void nodoAsignacion(NodoAsignacion nodo) {
        int direccion;

        if (UtGen.debug) {
            UtGen.emitirComentario("-> asignacion");
        }
        /*
         * Genero el codigo para la expresion a la derecha de la asignacion
         */

        interpretarNodo(nodo.getValor());
        /*
         * Ahora almaceno el valor resultante
         */
        direccion = tablaSimbolos.getDireccion(nodo.getIdentificador());

        UtGen.emitirRM("ST", UtGen.AC, direccion, UtGen.GP, "asignacion: almaceno el valor para el id " + nodo.getIdentificador(), out);

        if (UtGen.debug) {
            UtGen.emitirComentario("<- asignacion");
        }
    }

    private void nodoOperacionMatUnaria(NodoOperacionMatUnaria nodo) {
        if (UtGen.debug) {
            UtGen.emitirComentario("-> nodoOperacionMatUnaria");
        }
        
        interpretarNodo(nodo.getValor());
        /*
         * Almaceno en la pseudo pila de valor temporales el valor de la
         * operacion izquierda
         */
        UtGen.emitirRM("ST", UtGen.AC, desplazamientoTmp--, UtGen.MP, "op: push en la pila tmp el resultado expresion izquierda", out);
        /*
         * Genero la expresion derecha de la operacion
         */
        interpretarNodo(new NodoNumero(-1));
        /*
         * Ahora cargo/saco de la pila el valor izquierdo
         */
        UtGen.emitirRM("LD", UtGen.AC1, ++desplazamientoTmp, UtGen.MP, "op: pop o cargo de la pila el valor izquierdo en AC1", out);

        UtGen.emitirRO("MUL", UtGen.AC, UtGen.AC1, UtGen.AC, "op: *", out);
        
        if (UtGen.debug) {
            UtGen.emitirComentario("<- nodoOperacionMatUnaria");
        }
    }

    private void nodoOperacionMat(NodoOperacionMat nodo) {
        if (UtGen.debug) {
            UtGen.emitirComentario("-> Operacion: " + nodo.getTipo());
        }

        /*
         * Genero la expresion izquierda de la operacion
         */
        interpretarNodo(nodo.getOpIzquierdo());
        /*
         * Almaceno en la pseudo pila de valor temporales el valor de la
         * operacion izquierda
         */
        UtGen.emitirRM("ST", UtGen.AC, desplazamientoTmp--, UtGen.MP, "op: push en la pila tmp el resultado expresion izquierda", out);
        /*
         * Genero la expresion derecha de la operacion
         */
        interpretarNodo(nodo.getOpDerecho());
        /*
         * Ahora cargo/saco de la pila el valor izquierdo
         */
        UtGen.emitirRM("LD", UtGen.AC1, ++desplazamientoTmp, UtGen.MP, "op: pop o cargo de la pila el valor izquierdo en AC1", out);

        switch (nodo.getTipo()) {
            case SUMA:
                UtGen.emitirRO("ADD", UtGen.AC, UtGen.AC1, UtGen.AC, "op: +", out);
                break;
            case REST:
                UtGen.emitirRO("SUB", UtGen.AC, UtGen.AC1, UtGen.AC, "op: -", out);
                break;
            case MULT:
                UtGen.emitirRO("MUL", UtGen.AC, UtGen.AC1, UtGen.AC, "op: *", out);
                break;
            case DIVI:
                UtGen.emitirRO("DIV", UtGen.AC, UtGen.AC1, UtGen.AC, "op: /", out);
                break;
            default:
                UtGen.emitirComentario("BUG: tipo de operacion desconocida");
        }

        if (UtGen.debug) {
            UtGen.emitirComentario("<- Operacion: " + nodo.getTipo());
        }
    }

    private void nodoNumero(NodoNumero numero) {
        if (UtGen.debug) {
            UtGen.emitirComentario("-> constante");
        }
        UtGen.emitirRM("LDC", UtGen.AC, numero.getValor(), 0, "cargar constante: " + numero.getValor(), out);
        if (UtGen.debug) {
            UtGen.emitirComentario("<- constante");
        }
    }

    private void generarPreludioEstandar() {
        UtGen.emitirComentario("Compilacion TINY para el codigo objeto TM");
        UtGen.emitirComentario("Archivo: " + "NOMBRE_ARREGLAR");
        /*
         * Genero inicializaciones del preludio estandar
         */
        /*
         * Todos los registros en tiny comienzan en cero
         */
        UtGen.emitirComentario("Preludio estandar:");
        UtGen.emitirRM("LD", UtGen.MP, 0, UtGen.AC, "cargar la maxima direccion desde la localidad 0", out);
        UtGen.emitirRM("ST", UtGen.AC, 0, UtGen.AC, "limpio el registro de la localidad 0", out);
    }
}
