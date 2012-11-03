import ast.NodoBase;
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
        new Compilador(root,funciones).start();
    }
}