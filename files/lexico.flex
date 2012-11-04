import java_cup.runtime.SymbolFactory;

%%
%cup
%class Scanner
%ignorecase

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
comentario		= "{"[^}]*"}"

%%
"PROGRAM" {
	if(debug)
		System.out.println("token PROGRAM");
	return sf.newSymbol("PROGRAM",Symbols.PROGRAM);
}
"BEGIN" {
	if(debug)
		System.out.println("token BEGIN");
	return sf.newSymbol("BEGIN",Symbols.BEGIN);
}
"END." {
	if(debug)
		System.out.println("token FINPROGRAM");
	return sf.newSymbol("FINPROGRAM",Symbols.FINPROGRAM);
}
"WRITE" {
	if(debug)
		System.out.println("token WRITE");
	return sf.newSymbol("WRITE",Symbols.WRITE);
}
"WRITE" {
	if(debug)
		System.out.println("token WRITE");
	return sf.newSymbol("WRITE",Symbols.WRITE);
}
"WRITE" {
	if(debug)
		System.out.println("token WRITE");
	return sf.newSymbol("WRITE",Symbols.WRITE);
}
"WRITELN" {
	if(debug)
		System.out.println("token WRITELN");
	return sf.newSymbol("WRITELN",Symbols.WRITELN);
}
"=" {
	if(debug)
		System.out.println("token IGUAL");
	return sf.newSymbol("IGUAL",Symbols.IGUAL);
}
"+" {
	if(debug)
		System.out.println("token SUMA");
	return sf.newSymbol("SUMA",Symbols.SUMA);
}
"-" {
    if(debug)
		System.out.println("token REST");
	return sf.newSymbol("REST",Symbols.REST);
}
"*" {
	if(debug)
		System.out.println("token MULT");
	return sf.newSymbol("MULT",Symbols.MULT);
}
"/" {
	if(debug)
		System.out.println("token DIVI");
	return sf.newSymbol("DIVI",Symbols.DIVI);
}
"MOD" {
	if(debug)
		System.out.println("token MOD");
	return sf.newSymbol("MOD",Symbols.MOD);
}
"(" {
	if(debug)
		System.out.println("token LPARENT");
	return sf.newSymbol("LPARENT",Symbols.LPARENT);
}
")" {
	if(debug)
		System.out.println("token RPARENT");
	return sf.newSymbol("RPARENT",Symbols.RPARENT);
}
"[" {
	if(debug)
		System.out.println("token LCORCHE");
	return sf.newSymbol("LCORCHE",Symbols.LCORCHE);
}
"]" {
	if(debug)
		System.out.println("token RCORCHE");
	return sf.newSymbol("RCORCHE",Symbols.RCORCHE);
}			
";" {
	if(debug)
		System.out.println("token PTCO");
	return sf.newSymbol("PTCO",Symbols.PTCO);
}
"AND" {
	if(debug)
		System.out.println("token AND");
	return sf.newSymbol("AND",Symbols.AND);
}
"OR" {
	if(debug)
		System.out.println("token OR");
	return sf.newSymbol("OR",Symbols.OR);
}
"NOT" {
	if(debug)
		System.out.println("token NOT");
	return sf.newSymbol("NOT",Symbols.NOT);
}
"," {
	if(debug)
		System.out.println("token COMA");
	return sf.newSymbol("COMA",Symbols.COMA);
}
{identificador} {
	if(debug)
		System.out.println("token IDENTIFICADOR");
	return sf.newSymbol("IDENTIFICADOR",Symbols.IDENTIFICADOR,new String(yytext()));
}
{cadena} {
	if(debug)
        System.out.println("token CADENA");
	return sf.newSymbol("CADENA",Symbols.CADENA,new String(yytext()));
}
{numero} {
	if(debug)
		System.out.println("token NUMERO");
	return sf.newSymbol("NUMERO",Symbols.NUMERO,new String(yytext()));
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
{comentario} {
	if(debug)
        System.out.println("token COMENTARIO");
}
. {
	System.err.println("Caracter Ilegal encontrado en analisis lexico: " + yytext() + "\n");
}