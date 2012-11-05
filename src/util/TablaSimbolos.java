package util;

import ast.NodoDeclaracion;
import ast.NodoIdentificador;
import ast.Tipo;
import java.util.HashMap;
import java.util.Iterator;

public class TablaSimbolos {

    private HashMap<String, RegistroSimbolo> tabla;
    private int direccion;  //Contador de las localidades de memoria asignadas a la tabla

    public TablaSimbolos() {
        tabla = new HashMap<String, RegistroSimbolo>();
        direccion = 0;
    }

    public void cargarTabla(NodoDeclaracion dec) {
        while (dec != null) {
            NodoIdentificador ide = dec.getVariable();
            while (ide != null) {
                insertarSimbolo(ide.getNombre(), dec.getTipo(), -1);
                ide = (NodoIdentificador) ide.getHermanoDerecha();
            }
            dec = (NodoDeclaracion) dec.getHermanoDerecha();
        }
    }

    //true es nuevo no existe se insertara, false ya existe NO se vuelve a insertar 
    public boolean insertarSimbolo(String identificador, Tipo.Variable tipo, int numLinea) {
        RegistroSimbolo simbolo;
        if (tabla.containsKey(identificador)) {
            return false;
        } else {
            simbolo = new RegistroSimbolo(identificador, tipo, numLinea, direccion++);
            tabla.put(identificador, simbolo);
            return true;
        }
    }

    public RegistroSimbolo buscarSimbolo(String identificador) {
        RegistroSimbolo simbolo = (RegistroSimbolo) tabla.get(identificador);
        return simbolo;
    }

    public void imprimirClaves() {
        System.out.println("*** Tabla de Simbolos ***");
        for (Iterator<String> it = tabla.keySet().iterator(); it.hasNext();) {
            String s = (String) it.next();
            System.out.println("Consegui Key: " + s + " con direccion: " + buscarSimbolo(s).getDireccionMemoria());
        }
        System.out.println("*** FIN de Simbolos ***");
    }

    public int getDireccion(String clave) {
        return buscarSimbolo(clave).getDireccionMemoria();
    }
}
