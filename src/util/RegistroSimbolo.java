package util;

import ast.Tipo;
import ast.Tipo.Variable;

public class RegistroSimbolo {

    private Tipo.Variable tipo;
    private String identificador;
    private int NumLinea;
    private int DireccionMemoria;

    public RegistroSimbolo(String identificador,  Tipo.Variable tipo, int numLinea,
            int direccionMemoria) {
        this.identificador = identificador;
        this.tipo = tipo;
        NumLinea = numLinea;
        DireccionMemoria = direccionMemoria;
    }

    public String getIdentificador() {
        return identificador;
    }

    public Variable getTipo() {
        return tipo;
    }

    public int getNumLinea() {
        return NumLinea;
    }

    public int getDireccionMemoria() {
        return DireccionMemoria;
    }

    public void setDireccionMemoria(int direccionMemoria) {
        DireccionMemoria = direccionMemoria;
    }
}
