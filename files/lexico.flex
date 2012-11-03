import java_cup.runtime.SymbolFactory;

%%
%cup
%class Scanner

%{
	private SymbolFactory sf;
	private int lineaNum;
	private boolean debug;
	public Scanner(java.io.InputStream r, SymbolFactory sf){
		this(r);
		this.sf=sf;
		lineaNum=0;
		debug=true;
	}	
%}

%eofval{
	return sf.newSymbol("EOF",Symbols.EOF);
%eofval}

digito			= [0-9]
numero			= {digito}+
letra			= [a-zA-Z]
identificador	= {letra}([a-zA-Z0-9.%$_])*
nuevalinea		= \n | \n\r | \r\n
espacio			= [ \t]+
cadena			= "'"[^']*"'"

%%

"PRINT" {
	if(debug)
		System.out.println("token PRINT");
	return sf.newSymbol("WRITE",Symbols.PRINT);
}				
";" {
	if(debug)
		System.out.println("token PTCO");
	return sf.newSymbol("PTCO",Symbols.PTCO);
}
{identificador} {
	if(debug)
		System.out.println("token IDENTIFICADOR");
	return sf.newSymbol("IDENTIFICADOR",Symbols.IDENTIFICADOR,new String(yytext()));
}
{numero} {
	if(debug)
		System.out.println("token NUMERO");
	return sf.newSymbol("NUMERO",Symbols.NUMERO,new String(yytext()));
}
{cadena} {
	if(debug)
        System.out.println("token CADENA");
	return sf.newSymbol("CADENA",Symbols.CADENA,new String(yytext()));
}					
{nuevalinea} {
	lineaNum++;
	if(debug)
		System.out.println("token LINEANUEVA");
	return sf.newSymbol("LINEANUEVA",Symbols.LINEANUEVA);
}
{espacio} {
	/*Salta espacios en blanco*/
}
"'"[^\n]* {
	/*Salto comentarios*/
	if(debug)
		System.out.println("token COMENTARIO");
}
"REM"{espacio}[^\n]* {
	/* salto comentarios */
	if(debug)
		System.out.println("token COMENTARIO");
}
. {
	System.err.println("Caracter Ilegal encontrado en analisis lexico: " + yytext() + "\n");
}