
import ast.*;
import java.io.PrintWriter;
import util.TablaSimbolos;
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
            } else if (nodoActual instanceof NodoAsignacionBool) {
                nodoAsignacionBool((NodoAsignacionBool) nodoActual);
            } else if (nodoActual instanceof NodoIdentificador) {
                nodoIdentificador((NodoIdentificador) nodoActual);
            } else if (nodoActual instanceof NodoOperacionMat) {
                nodoOperacionMat((NodoOperacionMat) nodoActual);
            } else if (nodoActual instanceof NodoOperacionMatUnaria) {
                nodoOperacionMatUnaria((NodoOperacionMatUnaria) nodoActual);
            } else if (nodoActual instanceof NodoBoolean) {
                nodoBoolean((NodoBoolean) nodoActual);
            } else if (nodoActual instanceof NodoIf) {
                nodoIf((NodoIf) nodoActual);
            } else if (nodoActual instanceof NodoOperacionBool) {
                nodoOperacionBool((NodoOperacionBool) nodoActual);
            } else if (nodoActual instanceof NodoOperacionBoolLogica) {
                nodoOperacionBoolLogica((NodoOperacionBoolLogica) nodoActual);
            }else if(nodoActual instanceof NodoOperacionBoolUnaria){
                nodoOperacionBoolUnaria((NodoOperacionBoolUnaria) nodoActual);
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

    private void nodoAsignacionBool(NodoAsignacionBool nodo) {
        int direccion;

        if (UtGen.debug) {
            UtGen.emitirComentario("-> asignacionBool");
        }

        if (tablaSimbolos.getTipo(nodo.getIdentificador()) == Tipo.Variable.BOOLEAN) {

            interpretarNodo(nodo.getValor());
            /*
             * Ahora almaceno el valor resultante
             */
            direccion = tablaSimbolos.getDireccion(nodo.getIdentificador());

            UtGen.emitirRM("ST", UtGen.AC, direccion, UtGen.GP, "asignacion: almaceno el valor para el id " + nodo.getIdentificador(), out);

        } else {
            System.err.println("No coincide tipo, identificador: " + nodo.getIdentificador());
            System.exit(-1);
        }

        if (UtGen.debug) {
            UtGen.emitirComentario("<- asignacionBool");
        }
    }

    private void nodoAsignacion(NodoAsignacion nodo) {
        int direccion;

        if (UtGen.debug) {
            UtGen.emitirComentario("-> asignacion");
        }
        if (tablaSimbolos.getTipo(nodo.getIdentificador()) == Tipo.Variable.INTEGER) {
            /*
             * Genero el codigo para la expresion a la derecha de la asignacion
             */

            interpretarNodo(nodo.getValor());
            /*
             * Ahora almaceno el valor resultante
             */
            direccion = tablaSimbolos.getDireccion(nodo.getIdentificador());

            UtGen.emitirRM("ST", UtGen.AC, direccion, UtGen.GP, "asignacion: almaceno el valor para el id " + nodo.getIdentificador(), out);
        } else {
            System.err.println("No coincide tipo, identificador: " + nodo.getIdentificador());
            System.exit(-1);
        }
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

    private void nodoIf(NodoIf nodo) {
        int localidadSaltoElse, localidadSaltoEnd, localidadActual;
        if (UtGen.debug) {
            UtGen.emitirComentario("-> if");
        }

        /*
         * Genero el codigo para la parte de prueba del IF
         */
        interpretarNodo(nodo.getCondicion());

        localidadSaltoElse = UtGen.emitirSalto(1);
        UtGen.emitirComentario("If: el salto hacia el else debe estar aqui");
        /*
         * Genero la parte THEN
         */
        interpretarNodo(nodo.getParteThen());

        localidadSaltoEnd = UtGen.emitirSalto(1);

        UtGen.emitirComentario("If: el salto hacia el final debe estar aqui");

        localidadActual = UtGen.emitirSalto(0);

        UtGen.cargarRespaldo(localidadSaltoElse);

        UtGen.emitirRM_Abs("JEQ", UtGen.AC, localidadActual, "if: jmp hacia else", out);

        UtGen.restaurarRespaldo();
        /*
         * Genero la parte ELSE
         */
        if (nodo.getParteElse() != null) {
            interpretarNodo(nodo.getParteElse());
            localidadActual = UtGen.emitirSalto(0);
            UtGen.cargarRespaldo(localidadSaltoEnd);
            UtGen.emitirRM_Abs("LDA", UtGen.PC, localidadActual, "if: jmp hacia el final", out);
            UtGen.restaurarRespaldo();
        }

        if (UtGen.debug) {
            UtGen.emitirComentario("<- if");
        }
    }
    
    private void nodoOperacionBoolUnaria(NodoOperacionBoolUnaria nodo) {
        if (UtGen.debug) {
            UtGen.emitirComentario("-> NodoOperacionBoolUnaria");
        }

        /*
         * Genero la expresion izquierda de la operacion
         */
        interpretarNodo(nodo.getValor());
        
                UtGen.emitirRO("SUB", UtGen.AC, UtGen.AC1, UtGen.AC, "op: and", out);
                UtGen.emitirRM("JEQ", UtGen.AC, 2, UtGen.PC, "voy dos instrucciones mas alla if verdadero (AC==0)", out);
                UtGen.emitirRM("LDC", UtGen.AC, 0, UtGen.AC, "caso de falso (AC=0)", out);
                UtGen.emitirRM("LDA", UtGen.PC, 1, UtGen.PC, "Salto incodicional a direccion: PC+1 (es falso evito colocarlo verdadero)", out);
                UtGen.emitirRM("LDC", UtGen.AC, 1, UtGen.AC, "caso de verdadero (AC=1)", out);

        

        if (UtGen.debug) {
            UtGen.emitirComentario("<- NodoOperacionBoolUnaria");
        }
    }

    private void nodoOperacionBoolLogica(NodoOperacionBoolLogica nodo) {
        if (UtGen.debug) {
            UtGen.emitirComentario("-> nodoOperacionBoolLogica");
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
            case AND:
                UtGen.emitirRO("SUB", UtGen.AC, UtGen.AC1, UtGen.AC, "op: and", out);
                UtGen.emitirRM("JEQ", UtGen.AC, 2, UtGen.PC, "voy dos instrucciones mas alla if verdadero (AC==0)", out);
                UtGen.emitirRM("LDC", UtGen.AC, 0, UtGen.AC, "caso de falso (AC=0)", out);
                UtGen.emitirRM("LDA", UtGen.PC, 1, UtGen.PC, "Salto incodicional a direccion: PC+1 (es falso evito colocarlo verdadero)", out);
                UtGen.emitirRM("LDC", UtGen.AC, 1, UtGen.AC, "caso de verdadero (AC=1)", out);
                break;
            case OR:
                UtGen.emitirRO("ADD", UtGen.AC, UtGen.AC1, UtGen.AC, "op: or", out);
                UtGen.emitirRM("JNE", UtGen.AC, 2, UtGen.PC, "voy dos instrucciones mas alla if verdadero (AC!=0)", out);
                UtGen.emitirRM("LDC", UtGen.AC, 0, UtGen.AC, "caso de falso (AC=0)", out);
                UtGen.emitirRM("LDA", UtGen.PC, 1, UtGen.PC, "Salto incodicional a direccion: PC+1 (es falso evito colocarlo verdadero)", out);
                UtGen.emitirRM("LDC", UtGen.AC, 1, UtGen.AC, "caso de verdadero (AC=1)", out);
                break;
        }

        if (UtGen.debug) {
            UtGen.emitirComentario("<- nodoOperacionBoolLogica");
        }
    }

    private void nodoOperacionBool(NodoOperacionBool nodo) {
        if (UtGen.debug) {
            UtGen.emitirComentario("-> nodoOperacionBool");
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
            case IGUAL:
                UtGen.emitirRO("SUB", UtGen.AC, UtGen.AC1, UtGen.AC, "op: =", out);
                UtGen.emitirRM("JEQ", UtGen.AC, 2, UtGen.PC, "voy dos instrucciones mas alla if verdadero (AC==0)", out);
                UtGen.emitirRM("LDC", UtGen.AC, 0, UtGen.AC, "caso de falso (AC=0)", out);
                UtGen.emitirRM("LDA", UtGen.PC, 1, UtGen.PC, "Salto incodicional a direccion: PC+1 (es falso evito colocarlo verdadero)", out);
                UtGen.emitirRM("LDC", UtGen.AC, 1, UtGen.AC, "caso de verdadero (AC=1)", out);
                break;
            case MENOR:
                UtGen.emitirRO("SUB", UtGen.AC, UtGen.AC1, UtGen.AC, "op: <", out);
                UtGen.emitirRM("JLT", UtGen.AC, 2, UtGen.PC, "voy dos instrucciones mas alla if verdadero (AC<0)", out);
                UtGen.emitirRM("LDC", UtGen.AC, 0, UtGen.AC, "caso de falso (AC=0)", out);
                UtGen.emitirRM("LDA", UtGen.PC, 1, UtGen.PC, "Salto incodicional a direccion: PC+1 (es falso evito colocarlo verdadero)", out);
                UtGen.emitirRM("LDC", UtGen.AC, 1, UtGen.AC, "caso de verdadero (AC=1)", out);
                break;
            case MENORIGUAL:
                UtGen.emitirRO("SUB", UtGen.AC, UtGen.AC1, UtGen.AC, "op: <", out);
                UtGen.emitirRM("JLE", UtGen.AC, 2, UtGen.PC, "voy dos instrucciones mas alla if verdadero (AC<0)", out);
                UtGen.emitirRM("LDC", UtGen.AC, 0, UtGen.AC, "caso de falso (AC=0)", out);
                UtGen.emitirRM("LDA", UtGen.PC, 1, UtGen.PC, "Salto incodicional a direccion: PC+1 (es falso evito colocarlo verdadero)", out);
                UtGen.emitirRM("LDC", UtGen.AC, 1, UtGen.AC, "caso de verdadero (AC=1)", out);
                break;
            case MAYOR:
                UtGen.emitirRO("SUB", UtGen.AC, UtGen.AC, UtGen.AC1, "op: >", out);
                UtGen.emitirRM("JGT", UtGen.AC, 2, UtGen.PC, "voy dos instrucciones mas alla if verdadero (AC>0)", out);
                UtGen.emitirRM("LDC", UtGen.AC, 0, UtGen.AC, "caso de falso (AC=0)", out);
                UtGen.emitirRM("LDA", UtGen.PC, 1, UtGen.PC, "Salto incodicional a direccion: PC+1 (es falso evito colocarlo verdadero)", out);
                UtGen.emitirRM("LDC", UtGen.AC, 1, UtGen.AC, "caso de verdadero (AC=1)", out);
                break;
            case MAYORIGUAL:
                UtGen.emitirRO("SUB", UtGen.AC, UtGen.AC, UtGen.AC1, "op: >", out);
                UtGen.emitirRM("JGE", UtGen.AC, 2, UtGen.PC, "voy dos instrucciones mas alla if verdadero (AC>0)", out);
                UtGen.emitirRM("LDC", UtGen.AC, 0, UtGen.AC, "caso de falso (AC=0)", out);
                UtGen.emitirRM("LDA", UtGen.PC, 1, UtGen.PC, "Salto incodicional a direccion: PC+1 (es falso evito colocarlo verdadero)", out);
                UtGen.emitirRM("LDC", UtGen.AC, 1, UtGen.AC, "caso de verdadero (AC=1)", out);
                break;
            case DIFERENTE:
                UtGen.emitirRO("SUB", UtGen.AC, UtGen.AC, UtGen.AC1, "op: >", out);
                UtGen.emitirRM("JNE", UtGen.AC, 2, UtGen.PC, "voy dos instrucciones mas alla if verdadero (AC>0)", out);
                UtGen.emitirRM("LDC", UtGen.AC, 0, UtGen.AC, "caso de falso (AC=0)", out);
                UtGen.emitirRM("LDA", UtGen.PC, 1, UtGen.PC, "Salto incodicional a direccion: PC+1 (es falso evito colocarlo verdadero)", out);
                UtGen.emitirRM("LDC", UtGen.AC, 1, UtGen.AC, "caso de verdadero (AC=1)", out);
                break;
            default:
                UtGen.emitirComentario("BUG: tipo de operacion desconocida");
        }

        if (UtGen.debug) {
            UtGen.emitirComentario("<- nodoOperacionBool ");
        }
    }

    private void nodoBoolean(NodoBoolean nodo) {
        if (UtGen.debug) {
            UtGen.emitirComentario("-> constante boolean");
        }

        //carga 0 para false, 1 para true
        UtGen.emitirRM("LDC", UtGen.AC, nodo.getValor() ? 1 : 0, 0, "cargar constante booleana: " + nodo.getValor(), out);

        if (UtGen.debug) {
            UtGen.emitirComentario("<- constante boolean");
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
