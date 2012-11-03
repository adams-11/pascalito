package ast;

public class NodoNumero extends NodoBase {

    private Integer valor = 0;

    public NodoNumero() {
    }

    public NodoNumero(int d) {
        valor = d;
    }

    public NodoNumero(String s) {
        this.valor = Integer.parseInt(s);
    }

    public Integer getValor() {
        return valor;
    }

    public void setValor(Integer valor) {
        this.valor = valor;
    }
}
