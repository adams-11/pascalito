
import ast.NodoBase;
import ast.NodoDeclaracion;
import ast.NodoIdentificador;
import java.io.FileInputStream;
import java_cup.runtime.DefaultSymbolFactory;
import java_cup.runtime.SymbolFactory;

/**
 *
 */
public class Principal {

    public static void main(String[] args) throws Exception {
        Parser parser;
        SymbolFactory sf = new DefaultSymbolFactory();
        if (args.length == 0) {
            parser = new Parser(
                    new Scanner(System.in, sf), sf);
        } else {
            parser = new Parser(
                    new Scanner(new FileInputStream(args[0]), sf), sf);
        }
        parser.parse();
        NodoBase root = parser.action_obj.getRoot();
        NodoBase funciones = parser.action_obj.getFunciones();


        NodoBase variables = parser.action_obj.getVars();

        if (variables instanceof NodoDeclaracion) {
            NodoDeclaracion dec = (NodoDeclaracion) variables;
            while (dec != null) {
                System.out.println(dec.getTipo());
                NodoIdentificador ide = dec.getVariable();
                while (ide != null) {
                    System.out.println(ide.getNombre());
                    ide = (NodoIdentificador) ide.getHermanoDerecha();
                }
                dec = (NodoDeclaracion) dec.getHermanoDerecha();
            }
        }


        new Compilador(root, funciones).start();
    }
}