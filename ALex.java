package PdL;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;

// Analizador Lexico. Cada vez que es llamado imprime un nuevo token en tokens.txt y lo devuelve

public class ALex {
	
	private static Character c;
	static FileReader f;
	static BufferedReader br;
	static LineNumberReader r;
	static PrintWriter w;
	static PrintWriter w2;
	static PrintWriter err;
	static int linea;
	private static int estado;
	static Token fin = new Token("",""); //Token a devolver
	static boolean ultimoC=false;
	
	
	public ALex(String ruta) {
		//Inicializa los ficheros donde se van a escribir los resultados
		linea =1;
		estado=0;
		try {
			f = new FileReader(ruta);
			br = new BufferedReader(f);
			r = new LineNumberReader(br);
			w = new PrintWriter("tokens.txt", "UTF-8");
			w2 = new PrintWriter("TS.txt", "UTF-8");
			err = new PrintWriter("errores.txt", "UTF-8");
			c = (char)br.read();
		} catch (FileNotFoundException e) {
			System.out.println("Error: Fichero no encontrado");
			MainPdL.errFich=true;
		}
		catch (IOException e) {
			System.out.println("Error: Error al leer el fichero");
			MainPdL.errFich=true;
		}
	}
	
	//Conjunto de las palabras reservadas
	static String [] PR = {"boolean", "for", "function", "if", "input", "int", "let", "print", "return", "string", "true", "false"};
	
	public static Token Leer() throws IOException {
		
		//Caso EOF
		if((MainPdL.tk.getCodigo()=="puntocoma" || MainPdL.tk.getCodigo()=="cierrallave") && !br.ready()) {
			setToken("EOF","");
			estado=8;
		}
		
		estado=0;		
		
		if(estado==0) {
			if(Character.isAlphabetic(c)){
				estado=1; 
			}// letras

			if(Character.isDigit(c)) {
				estado=2;
			} // digitos
			
			if(c == '\n') {
				linea++;
				c = (char)br.read();
				Leer();
				estado=8;
			} // salto de linea
			
			else if(Character.isWhitespace(c)) {
				c = (char)br.read();
				Leer();
				estado=8;
			} // espacio
			
			if (estado==0) {
				switch (c) { 
				case ('&'): 
					estado=3;
				break;
				case('\''):
					estado=4;
				break;
				case('/'):
					estado=5;
				break;
				case('='):
					estado=7;
					break;
				case('+'):
					w.println("<mas,>");
					c = (char)br.read();
					setToken("mas","");
					estado=8;
					break;
				case('('): 
					w.println("<abreparentesis,>");
					c = (char)br.read();				
					setToken("abreparentesis","");
					estado=8;
					break;
				case (')'): 
					w.println("<cierraparentesis,>");
					c = (char)br.read();
					setToken("cierraparentesis","");
					estado=8;
					break;
				case(';'):
					w.println("<puntocoma,>");
					c = (char)br.read();
					setToken("puntocoma","");
					estado=8;
					break;
				case(','):
					w.println("<coma,>");
					c = (char)br.read();
					setToken("coma","");
					estado=8;
					break;
				case('{'):
					w.println("<abrellave,>");
					c = (char)br.read();
					setToken("abrellave","");
					estado=8;
					break;
				case('}'):
					w.println("<cierrallave,>");
					c = (char)br.read();
					setToken("cierrallave","");
					estado=8;
					break;
				}
			}// fin del switch
		}// fin del estado 0


		if (estado == 1){
			String concatenar = ""; 
			int resul=0;
			concatenar = concatenar + c; 
			c=(char)br.read();
			while (Character.isAlphabetic(c) || Character.isDigit(c) || c == '_') {
				concatenar = concatenar + c; 
				c = (char)br.read();				
			}
			System.out.println(concatenar);
			//Comprueba si es PR
			if(esPR(concatenar, resul)<11) {
				int i=esPR(concatenar, resul);
				w.println("<PR,"+ i +">");
				setToken("PR",Integer.toString(i));
			}
			//Comprueba si estamos en zona de declaración
			else if (MainPdL.ZD) {
				MainPdL.TSAct.insertarNodo(concatenar);
				int ps = MainPdL.TSAct.getNodo(concatenar);
				w.println("<id," + ps + ">");
				setToken("id",Integer.toString(ps));
			}
			//Comprueba si es un identificador existente en la TS
			else if(MainPdL.TSAct.estaEnTS(concatenar)) {
				int ps = MainPdL.TSAct.getNodo(concatenar);
				w.println("<id," + ps + ">");	
				setToken("id",Integer.toString(ps));
			}
			//Comprueba si es un identificador existente en la TSGlobal (solo si estamos dentro del cuerpo de una funcion)
			else if(MainPdL.TSG.estaEnTS(concatenar)&&ASt.cuerpoFuncion) {
				int ps = MainPdL.TSG.getNodo(concatenar);
				w.println("<id," + ps + ">");	
				setToken("id",Integer.toString(ps));
			}
			//Si no se cumple ningún caso se considera variable global entera
			else {
				MainPdL.TSG.insertarNodo(concatenar);
				int ps = MainPdL.TSG.getNodo(concatenar);
				MainPdL.TSG.getNodo(ps).setTipo("entero");
				MainPdL.TSG.getNodo(ps).setAncho(2);
				w.println("<id," + ps + ">");
				setToken("id",Integer.toString(ps));
			}
			estado=8;
		} // Identificadores y Palabras Reservadas
		
		if(estado==2) {
			int num=0;
			while(Character.isDigit(c)){
				num=num*10+(Character.getNumericValue(c));	
				System.out.println(num);
				c=(char)br.read();
			}
			System.out.println(num);
			//Comprueba que el entero se encuentre dentro de los límites
			if(num>=0 && num<=32767) {
				w.println("<entero, " + num  +">");
				setToken("entero",Integer.toString(num));
				}
			else {
				err.println("Error en línea " + linea + ". Error léxico: Los enteros no pueden ocupar más de 16 bits (no pueden ser mayores que 32767)");
				ASt.error=true;
			}
		} // Enteros
		
		if (estado == 3){ 
			c = (char)br.read(); 
			// &= -> Asignación con y lógico
			if(c == '='){
				w.println("<asignacionylogico,>");
				c = (char)br.read(); 
				setToken("asignacionylogico","");
			}
			//&& -> Operador y lógico
			else if (c == '&'){
				//MainPdL.tk = new Token ("ylogico","");
				w.println("<ylogico,>");
				c = (char)br.read();
				setToken("ylogico","");
			}
			//Caracter & invalido
			else {
				err.println("Error en la linea " + linea + ". Error sintáctico: El caracter & es invalido, prueba con &= o con &&");
				ASt.error=true;
				c = (char)br.read();
				estado=8;
			}
		} // Elementos &
		
		if(estado==4) {
			String cadena= "";
			int cont=0;
			c = (char)br.read();
			while(c!='\'') {
				cadena = cadena + c;
				cont++;
				c = (char)br.read();
			}
			c = (char)br.read();
			// La cadena debe de tener un máximo de 64 caracteres
			if(cont<65) {
				w.println("<cadena, \"" + cadena + "\">");
				setToken("cadena",cadena);
			}
			else {
				err.println("Error en la linea " + linea + ". Error léxico: La cadena no puede contener más de 64 caracteres");
				ASt.error=true;
			}
		}// Cadenas
		
		if(estado==5) {
			c=(char)br.read();
			if(c.equals('*')) {
				estado=6;
				}
			else {
				err.println("Error en línea " + linea + ". Error sintáctico: El caracter * es invalido, para iniciar un comentario debes poner /*");
			}
		} // Inicio de un comentario
				
		if(estado==6) {
			Character d=(char)br.read();
			while(!c.equals('*') || !d.equals('/')) {
				c=d;
				d=(char)br.read();
			}
			c = (char)br.read();
			Leer();
		} // Comentario

		if(estado == 7){
			c = (char)br.read();
			// == -> Operador relacional de asignación comparativa
			if(c == '='){
				w.println("<asignacioncomparativa,>");
				c = (char)br.read();
				setToken("asignacioncomparativa","");
			}
			// = -> Asignación 
			else {
				w.println("<asignacion,>");
				setToken("asignacion","");
			}
		} // Elementos =
		
		return fin;
	} // Alex 
	
	public static void close() throws IOException {
		f.close();
		r.close();
		br.close();
		w.close();
		w2.close();
		err.close();
	}
	
	// esPR: Comprueba que una cadena de caracteres corresponda a una de las palabras reservadas
	
	public static int esPR(String s, int i) {
		for(i=0; i<11; i++) {
			if(PR[i].equals(s)) {
				return i;
			}
		}
		return i;
	}
	
	
	// setToken: Modifica el codigo y el atributo del token devuelto
	
	public static void setToken(String codigo, String atributo) {
		fin.setAtributo(atributo);
		fin.setCodigo(codigo);
	}
	
}