
import ast.NodoBase;
import ast.NodoDeclaracion;
import ast.NodoIdentificador;
import java.io.FileInputStream;
import java_cup.runtime.DefaultSymbolFactory;
import java_cup.runtime.SymbolFactory;
import util.TablaSimbolos;

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
        NodoBase declaraciones = parser.action_obj.getVars();
        
        TablaSimbolos tablaSimbolos=new TablaSimbolos();        
        tablaSimbolos.cargarTabla((NodoDeclaracion)declaraciones);
        
        tablaSimbolos.imprimirClaves();
        
        new Compilador(root, funciones,tablaSimbolos).start();
    }
}