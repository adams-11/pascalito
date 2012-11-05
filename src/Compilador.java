
import util.TablaSimbolos;
import ast.*;
import util.UtGen;

/**
 *
 * @author ADAMS
 */
public class Compilador {

    private java.util.Scanner in = new java.util.Scanner(System.in);
    private NodoBase root;
    //private NodoProcedimientoDeclaracion rootProcedimientos;
    private TablaSimbolos tablaSimbolos = null;

    public Compilador(NodoBase root, NodoBase rootProcedimientos, TablaSimbolos tablaSimbolos) {
        this.root = root;
        //this.rootProcedimientos = (NodoProcedimientoDeclaracion) rootProcedimientos;
        this.tablaSimbolos=tablaSimbolos;
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
        UtGen.emitirRO("HALT", 0, 0, 0, "");
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
            }else if(nodoActual instanceof NodoOperacionMat){
                //nodoOperacionMat((NodoOperacionMat) nodoActual);
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
            UtGen.emitirRO("OUT", UtGen.AC, 0, 0, "escribir: genero la salida de la expresion");

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
        UtGen.emitirRM("LD", UtGen.AC, direccion, UtGen.GP, "cargar valor de identificador: "+nodo.getNombre());
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

        UtGen.emitirRM("ST", UtGen.AC, direccion, UtGen.GP, "asignacion: almaceno el valor para el id "+nodo.getIdentificador());

        if (UtGen.debug) {
            UtGen.emitirComentario("<- asignacion");
        }
    }

    private void nodoNumero(NodoNumero numero) {
        if (UtGen.debug) {
            UtGen.emitirComentario("-> constante");
        }
        UtGen.emitirRM("LDC", UtGen.AC, numero.getValor(), 0, "cargar constante: " + numero.getValor());
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
        UtGen.emitirRM("LD", UtGen.MP, 0, UtGen.AC, "cargar la maxima direccion desde la localidad 0");
        UtGen.emitirRM("ST", UtGen.AC, 0, UtGen.AC, "limpio el registro de la localidad 0");
    }
}
