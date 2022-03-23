package PdL;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;


public class ASt {
	
	// Conjunto de las reglas del analizador semantico
	static Map <Integer,String[]> mapaReglas = new HashMap<Integer, String[]>();
	
	// Pilas P y Aux con pilas correspondientes para almacenar atributos
	static Stack<String> P = new Stack<String>();
	static Stack<String> PAtr = new Stack<String>(); 
	static Stack<String> Aux = new Stack<String>();
	static Stack<String> AuxAtr = new Stack<String>();
	
	// Si error=true ha habido algún error léxico, sintáctico o semántico y finaliza la ejecución
	static boolean error = false;
	// Si terminado=true finaliza la ejecución sin errores
	static boolean terminado = false;
	// Si cuerpoFuncion=true el analizador se encuentra dentro de una funcion y puede consultar identificadores tanto en la TSActual como en la Global
	static boolean cuerpoFuncion=false;
	static PrintWriter parse;
	static PrintWriter err;
	
	// Variables necesarias para la gestión de la TS
	static int desGlobal = 0;
	static int desLocal = 0; 
	static int desActual = desGlobal;
	static int NumeroTabla = 1; 
	
	static int numParam;
	static int numFunciones = 0;
	static int[] numParamFunciones= {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
	static String[] paramFunciones= {"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""};

	//Conjunto de los símbolos terminales
	static String[] T = {"id","puntocoma","abreparentesis","cierraparentesis","abrellave","cierrallave","asignacion",
						"coma","ylogico","asignacionylogico","asignacioncomparativa","mas","entero","cadena",
						"boolean", "for", "function", "if", "input", "int", "let", "print", "return", "string", "true", "false" };
	
	//Conjunto de los símbolos no terminales
	static String[] NT = {"P2","P", "B", "T", "S", "S2", "X", "C", "L", "Q", "F", "H", "A", "K", "E", "E2", "R", "R2", "U", "U2", "V", "V2", "EOF"};
	
	//Conjunto de las acciones semánticas
	static String[] AccSem = {"{1.1}","{1.2}","{2.1}", "{3.1}","{4.1}","{5.1}","{5.2}","{5.3}","{6.1}","{7.1}","{8.1}","{8.2}","{8.3}","{8.4}","{8.5}","{9.1}","{10.1}",
							"{11.1}","{12.1}","{13.1}","{14.1}","{15.1}","{16.1}","{16.2}","{17.1}","{17.2}","{18.1}","{18.2}","{19.1}","{20.1}",
							"{21.1}","{22.1}","{23.1}","{24.1}","{25.1}","{26.1}","{27.1}","{27.2}","{27.3}","{27.4}","{28.1}","{29.1}","{30.1}","{30.2}",
							"{31.1}","{32.1}","{32.2}","{32.3}","{33.1}","{34.1}","{35.1}","{36.1}","{37.1}","{38.1}","{39.1}","{40.1}",
							"{41.1}","{42.1}","{43.1}","{44.1}","{45.1}","{46.1}","{47.1}","{48.1}"};
	
	
	static public void crearASt() throws FileNotFoundException, UnsupportedEncodingException {
		mapaReglas.put(1, new String[] {"{1.1}","P","{1.2}"});
		mapaReglas.put(2, new String[] {"B","P", "{2.1}"});
		mapaReglas.put(3, new String[] {"F","P", "{3.1}"});
		mapaReglas.put(4, new String[] {"","{4.1}"});
		
		mapaReglas.put(5, new String[] {"let","{5.1}","T","id","{5.2}","puntocoma", "{5.3}"});
		mapaReglas.put(6, new String[] {"if","abreparentesis","E","cierraparentesis","S","{6.1}"});
		mapaReglas.put(7, new String[] {"S","{7.1}"});
		mapaReglas.put(8, new String[] {"for","abreparentesis","let","{8.1}","T","id","{8.2}","puntocoma","E","{8.3}","puntocoma","U","{8.4}","cierraparentesis","abrellave","C","cierrallave", "{8.5}"});
		
		mapaReglas.put(9, new String[] {"int","{9.1}"});
		mapaReglas.put(10, new String[] {"boolean","{10.1}"});
		mapaReglas.put(11, new String[] {"string","{11.1}"});
		
		mapaReglas.put(12, new String[] {"id","S2","{12.1}"});
		mapaReglas.put(13, new String[] {"return","X","puntocoma","{13.1}"});
		mapaReglas.put(14, new String[] {"print","abreparentesis","E","cierraparentesis","puntocoma","{14.1}"});
		mapaReglas.put(15, new String[] {"input","abreparentesis","id","cierraparentesis","puntocoma","{15.1}"});
		
		mapaReglas.put(16, new String[] {"asignacion","E","{16.1}","puntocoma", "{16.2}"});
		mapaReglas.put(17, new String[] {"abreparentesis","L","{17.1}","cierraparentesis","puntocoma", "{17.2}"});

		mapaReglas.put(18, new String [] {"asignacionylogico","E", "{18.1}" , "puntocoma", "{18.2}"}); 
		mapaReglas.put(19, new String [] {"E", "{19.1}"}); 
		mapaReglas.put(20, new String [] {"", "{20.1}"}); 
		mapaReglas.put(21, new String [] {"B","C", "{21.1}"}); 
		mapaReglas.put(22, new String [] {"", "{22.1}"}); 
		mapaReglas.put(23, new String [] {"E", "Q", "{23.1}"}); 
		mapaReglas.put(24, new String [] {"", "{24.1}"}); 
		mapaReglas.put(25, new String [] {"coma","E", "Q", "{25.1}"}); 
		mapaReglas.put(26, new String [] {"", "{26.1}"}); 
		mapaReglas.put(27, new String [] {"{27.1}","function","id", "{27.2}", "H", "abreparentesis", "A", "cierraparentesis", "{27.3}" , "abrellave", "C", "cierrallave", "{27.4}"}); 
		mapaReglas.put(28, new String [] {"T", "{28.1}"}); 
		mapaReglas.put(29, new String [] {"", "{29.1}"}); 
		mapaReglas.put(30, new String [] {"T", "id", "{30.1}", "K", "{30.2}"}); 
		mapaReglas.put(31, new String [] {"", "{31.1}"}); 
		mapaReglas.put(32, new String [] {"coma","{32.1}","T","id", "{32.2}", "K", "{32.3}"}); 
		mapaReglas.put(33, new String [] {"", "{33.1}"}); 
		mapaReglas.put(34, new String [] {"R","E2", "{34.1}"});
		
		mapaReglas.put(35, new String [] {"", "{35.1}"});
		mapaReglas.put(36, new String [] {"ylogico", "R", "E2", "{36.1}"});
		mapaReglas.put(37, new String [] {"U","R2", "{37.1}"});
		mapaReglas.put(38, new String [] {"", "{38.1}"});
		mapaReglas.put(39, new String [] {"asignacioncomparativa", "U", "R2", "{39.1}"});
		mapaReglas.put(40, new String [] {"V","U2", "{40.1}"});
		mapaReglas.put(41, new String [] {"", "{41.1}"});
		mapaReglas.put(42, new String [] {"mas", "V", "U2", "{42.1}"});
		mapaReglas.put(43, new String [] {"id","V2", "{43.1}"});
		mapaReglas.put(44, new String [] {"abreparentesis", "E", "cierraparentesis", "{44.1}"});
		mapaReglas.put(45, new String [] {"entero", "{45.1}"});
		mapaReglas.put(46, new String [] {"cadena", "{46.1}"});
		mapaReglas.put(47, new String [] {"abreparentesis", "L", "cierraparentesis", "{47.1}"});
		mapaReglas.put(48, new String [] {"", "{48.1}"});
		
		parse = new PrintWriter("parse.txt", "UTF-8");
		err = new PrintWriter("errores.txt", "UTF-8");
		parse.print("descendente ");
	}
	
	//First de cada regla
	
	static String[] r1 = {"let", "if", "for", "id", "return", "print", "input","function"};
	static String[] r2 = {"let", "if", "for", "id", "return", "print", "input"};
	static String[] r3 = {"function"};
	static String[] r4 = {"EOF"};
	static String[] r5 = {"let"};
	static String[] r6 = {"if"};
	static String[] r7 = {"id", "return", "print", "input"};
	static String[] r8 = {"for"};
	static String[] r9 = {"int"};
	static String[] r10 = {"boolean"};
	static String[] r11 = {"string"};
	static String[] r12 = {"id"};
	static String[] r13 = {"return"};
	static String[] r14 = {"print"};
	static String[] r15 = {"input"};
	
	static String [] r16 = {"asignacion"};
	static String [] r17 = {"abreparentesis"};
	static String [] r18 = {"asignacionylogico"}; 
	static String [] r19 = {"true" , "false" , "id" , "abreparentesis" , "entero" , "cadena"}; 
	static String [] r20 = {"puntocoma"};
	static String [] r21 = {"let", "if", "for", "id", "return", "print", "input"};
	static String [] r22 = {"cierrallave"};
	static String [] r23 = {"true" , "false" , "id" , "abreparentesis" , "entero", "cadena" };
	static String [] r24 = {"cierraparentesis"}; 
	static String [] r25 = {"coma"}; 
	static String [] r26 = {"cierraparentesis"}; 
	static String [] r27 = {"function"}; 
	static String [] r28 = {"int" , "boolean" , "string"}; 
	static String [] r29 = {"abreparentesis"}; 
	static String [] r30 = {"int" , "boolean" , "string"};
	static String [] r31 = {"cierraparentesis"};
	static String [] r32 = {"coma"};
	static String [] r33 = {"cierraparentesis"}; 
	static String [] r34 = {"id", "abreparentesis", "entero", "cadena"};
	static String [] r35 = {"cierraparentesis", "puntocoma", "coma"};
	static String [] r36 = {"ylogico"};
	static String [] r37 = {"id", "abreparentesis", "entero", "cadena"};
	static String [] r38 = {"ylogico", "cierraparentesis", "puntocoma", "coma"};
	static String [] r39 = {"asignacioncomparativa"};
	static String [] r40 = {"id", "abreparentesis", "entero", "cadena"};
	static String [] r41 = {"asignacioncomparativa", "ylogico", "cierraparentesis", "puntocoma", "coma"};
	static String [] r42 = {"mas"};
	static String [] r43 = {"id"};
	static String [] r44 = {"abreparentesis"};
	static String [] r45 = {"entero"};
	static String [] r46 = {"cadena"};
	static String [] r47 = {"abreparentesis"};
	static String [] r48 = {"mas", "asignacioncomparativa", "ylogico", "cierraparentesis", "puntocoma", "coma"};
	
	//Conjunto de las palabras reservadas y símbolos terminales
	static String[] PR = {"boolean", "for", "function", "if", "input", "int", "let", "print", "return", "string", "true", "false"};
	static String[] terminales = {"id","puntocoma","abreparentesis","cierraparentesis","abrellave","cierrallave","asignacion","coma","ylogico","asignacionylogico","asignacioncomparativa","mas","entero","cadena","EOF"};
	
	// tokenFirst: Comprueba si el token devuelto por el ALex se encuentra en el first de la regla que se quiere aplicar
	
	static public boolean tokenFirst(String[] first) {
		boolean resul=false;
		for(int i=0; i<first.length && !resul; i++) {
			resul=MainPdL.tk.getCodigo().equals(first[i]);
		}
		if(!resul && MainPdL.tk.getCodigo()=="PR") {
			int posicionPR=Integer.parseInt(MainPdL.tk.getAtributo());
			for(int i=0; i<first.length && !resul; i++) {
				resul=PR[posicionPR].equals(first[i]);
			}
		}
		return resul;
	}
	
	// actualizarP: Actualiza la pila P al aplicar una regla

	static public void actualizarP(int regla) {
		transferencia();
		for (int i = mapaReglas.get(regla).length-1; i>=0; i--) {
			P.push(mapaReglas.get(regla)[i]);
			PAtr.push(" ");
		}
		parse.print(regla + " ");
		imprimirPilas();
		return;
	}
	
	// esTerminal: Esta funcion se aplica a los simbolos terminales. Son enviados a la pila AUX y se lee el siguiente token
	
	static public void esTerminal() throws IOException {
		boolean terminal=true;
		while(terminal&&!error) {
			if(MainPdL.tk.getCodigo()=="PR" && P.lastElement()==PR[Integer.parseInt(MainPdL.tk.getAtributo())]) {
					transferencia();
					MainPdL.tk = ALex.Leer();
					System.out.println("<"+MainPdL.tk.getCodigo()+","+MainPdL.tk.getAtributo()+">");
			}
			else if(!P.isEmpty() && MainPdL.tk.getCodigo()==P.lastElement()) {
				for (int i=0; i<terminales.length; i++) {
					if(P.lastElement()==terminales[i]) {
						transferencia();
						MainPdL.tk = ALex.Leer();	
						System.out.println("<"+MainPdL.tk.getCodigo()+","+MainPdL.tk.getAtributo()+">");
						}
					}
				}
			else{
				terminal=false;
			}
		}
		imprimirPilas();
		return;
	}
	
	// 	transferencia: Inserta los símbolos de la pila P en la pila AUX, así como los atributos de PAtr en AuxAtr. En caso de un identificador inserta su tipo (int,bool,string) como su atributo. En caso de
	//	que id sea de tipo function su atributo será su tipo de retorno

	static public void transferencia () {
		String elem = P.pop();
		String atr_elem="";
		Aux.add(elem); 
		if(elem=="id") {
			if(MainPdL.TSAct.getTamaño()>Integer.parseInt(MainPdL.tk.getAtributo())) {
				atr_elem=MainPdL.TSAct.getNodo(Integer.parseInt(MainPdL.tk.getAtributo())).getTipo();
			}
			if(atr_elem=="function")
				atr_elem=MainPdL.TSAct.getNodo(Integer.parseInt(MainPdL.tk.getAtributo())).getTipoRetorno();
			if(atr_elem==""  && cuerpoFuncion) {
				System.out.println("Entro aqui");
				atr_elem=MainPdL.TSG.getNodo(Integer.parseInt(MainPdL.tk.getAtributo())).getTipo();
			}
			PAtr.pop();
		}
		else
			atr_elem=PAtr.pop();
		if(atr_elem==null)
			atr_elem=" ";
		AuxAtr.push(atr_elem);
	}
	
	// popAux: Expulsa de Aux y AuxAtr tantos elementos como se pasen por parametro. Usado al final de las reglas
	
	static public void popAux (int num) {
		for(int i=0;i<num;i++) {
			Aux.pop();
			AuxAtr.pop();
		}
		P.pop(); 
		PAtr.pop();
	}
	
	// imprimirPilas: Usado para debuggeo. Muestra el contenido de las 4 pilas y el token actual en cada ejecución
	
	static public void imprimirPilas() {
		System.out.println("P: "+P);
		System.out.println("PAtr: "+PAtr);
		System.out.println("Aux: "+Aux);
		System.out.println("AuxAtr: "+AuxAtr);
		System.out.println("Token: <"+MainPdL.tk.getCodigo() + ", " + MainPdL.tk.getAtributo()+">");
		System.out.println();
	}
		
	public static void main() throws IOException {
		//Inicialmente la pila P contiene EOF y P2 (axioma)
		P.push("EOF");
		PAtr.push(" ");
		P.push("P2");
		PAtr.push(" ");
		//Lectura del primer token
		MainPdL.tk = ALex.Leer(); 
		System.out.println("<"+MainPdL.tk.getCodigo()+","+MainPdL.tk.getAtributo()+">");
		// El analizador entra en un bucle que terminará cuando la pila P esté vacía o detecte algún error
		while(!P.isEmpty()&&!error) {
			// el estado es el último elemento de la pila. Puede ser un símbolo terminal, un símbolo no terminal o una acción semántica
			String estado=P.lastElement();
			imprimirPilas();
			// encontrado=true cuando averigue si estado se trata de un terminal, un no terminal o una accion semantica
			boolean encontrado=false;
			for(int i=0;i<NT.length && !encontrado; i++) {
				if(NT[i]==estado) {
					encontrado=true;
					// Switch de estado es no terminal. Se detendrá en el símbolo no terminal correspondiente al último elemento de la pila.
					// Comprueba si el token pertenece al first de la regla de la cual estado es la parte izquierda. 
					// Si es así sustituye el símbolo no terminal por la parte derecha de la regla en orden inverso
					// Si no dará un error
					switch(estado) {
						case("EOF"):
							if (MainPdL.tk.getCodigo()=="EOF") {
								transferencia(); 
								terminado=true;
							}
						break;
						case ("P2"):
							if(tokenFirst(r1)) {
								actualizarP(1);
							}
							else {
								error=true;
							}
						break;
						case ("P"):
							if(tokenFirst(r2)) {
								actualizarP(2);
							}
							else if (tokenFirst(r3)) {
								actualizarP(3);
							}
							else if (tokenFirst(r4)){
								parse.print(4 + " ");
								System.out.println("4");
								transferencia();
							}
							else {
								error=true;
							}
						break;
						case ("B"):
							if(tokenFirst(r5)) {
								actualizarP(5);
							}
							else if (tokenFirst(r6)) {
								actualizarP(6);
							}
							else if(tokenFirst(r7)) {
								actualizarP(7);
							}
							else if (tokenFirst(r8)) {
								actualizarP(8);
							}
							else {
								error=true;
							}
						break;	
						case ("T"):
							if(tokenFirst(r9)) {
								actualizarP(9);
							}
							else if (tokenFirst(r10)) {
								actualizarP(10);
							}
							else if(tokenFirst(r11)) {
								actualizarP(11);
							}
							else {
								error=true;
							}
						break;	
						case ("S"):
							if(tokenFirst(r12)) {
								actualizarP(12);
							}
							else if (tokenFirst(r13)) {
								actualizarP(13);
							}
							else if(tokenFirst(r14)) {
								actualizarP(14);
							}
							else if (tokenFirst(r15)) {
								actualizarP(15);
							}
							else {
								error=true;
							}
						break;
	
						case ("S2") : 
							if (tokenFirst(r16)){
								actualizarP(16);
							}
							else if (tokenFirst(r17)){
								actualizarP(17);
							}
							else if (tokenFirst(r18)){
								actualizarP(18);
							}
							else {
								error=true; 
							}
						break; 
	
						case ("X") : 
							if (tokenFirst(r19)){
								actualizarP(19);
							}	
							else if (tokenFirst(r20)) {
								parse.print(20+" ");
								transferencia();
							}
							else {
								error = true; 
							}
						break; 
	
						case ("C") :			
							if (tokenFirst(r21)){
								actualizarP(21);
							}	
							else if (tokenFirst(r22)){
								parse.print(22+" ");
								transferencia();
							}
							else {
								error = true;
							}
						break; 
	
						case ("L") :			
							if (tokenFirst(r23)){
								actualizarP(23);
							}
							else if (tokenFirst(r24)){
								parse.print(24 + " ");
								transferencia();
							}
							else {
								error = true; 
							}
						break;
						
						case ("Q") : 
							if (tokenFirst(r25)){
								actualizarP(25);
							}
							else if (tokenFirst(r26)){
								parse.print(26 + " ");
								transferencia();
							}
							else {
								error = true; 
							}
						break;
	
						case ("F") : 
							if (tokenFirst(r27)){
								actualizarP(27);
							}
							else {
								error = true;
							}
						break; 
	
						case ("H") : 
							if (tokenFirst(r28)){
								actualizarP(28);
							}	
							else if (tokenFirst(r29)){
								parse.print(29 + " ");
								transferencia();
							}
							else {
								error = true; 
							}
						break; 
	
						case ("A"): 
							if (tokenFirst(r30)){
								actualizarP(30);
							}	
							else if (tokenFirst(r31)){
								parse.print(31 + " ");
								transferencia();
							}
							else {
								error = true; 
							}
						break; 
	
						case ("K"): 
							if (tokenFirst(r32)){
								actualizarP(32);
							}	
							else if (tokenFirst(r33)){
								parse.print(33 + " ");
								transferencia();
							}
							else {
								error = true; 
							}
						break;	
	
						case ("E"):
							if (tokenFirst(r34)){
								actualizarP(34);
							}
							else{
								error = true;
							}
						break;
						case ("E2"):
							if (tokenFirst(r36)){
								actualizarP(36);
							} 
							else if (tokenFirst(r35)){
								parse.print(35 + " ");
								System.out.println("35");
								transferencia();
							}
							else{
								error = true;
							}
						break;
						case("R"):
							if (tokenFirst(r37)){
								actualizarP(37);
							}
							else{
								error = true;
							}
						break;
						case("R2"):
							if (tokenFirst(r39)){
								actualizarP(39);
							}
							else if (tokenFirst(r38)){
								parse.print(38 + " ");
								System.out.println("38");
								transferencia();
							}
							else {
								error = true;
							}
						break;
						case("U"):
							if (tokenFirst(r40)){
								actualizarP(40);
							}
							else{
								error = true;
							}
						break;
						case("U2"):
							if (tokenFirst(r42)){
								actualizarP(42);
							}
							else if (tokenFirst(r41)){
								parse.print(41 + " ");
								transferencia();
							}
							else{
								error = true;
							}
						break;
						case("V"):
							if (tokenFirst(r43)){
								actualizarP(43);
							}
							else if (tokenFirst(r44)){
								actualizarP(44);
							}
							else if (tokenFirst(r45)){
								actualizarP(45);
							}
							else if (tokenFirst(r46)){
								actualizarP(46);
							}
							else{
								error = true;
							}
						break;
						case("V2"):
							if (tokenFirst(r47)){
								actualizarP(47);
							}
							else if (tokenFirst(r48)){
								parse.print(48 + " ");
								transferencia();
							}
							else {
								error = true;
							}
						break;
					} // fin del switch
				} // fin del if
			}// fin del for
			
			for(int i=0; i<T.length && !encontrado; i++){
				if(T[i] == estado){
					// Estado es terminal. Ejecuta la funcion esTerminal
					encontrado = true;
					esTerminal(); 
				}				
			} // en caso de que sea terminal
			
			for(int i=0; i<AccSem.length && !encontrado; i++){
				if(AccSem[i] == estado){
					encontrado = true;
					// Estado es una acción semántica. Hace un switch para encontrar la regla concreta que se debe ejecutar. Al final expulsará dicha regla de las pilas P y PAtr
					// Para ver qué hace cada regla es recomendable consultar el apartado de "Analizador Semántico" de la memoria del proyecto
					switch(estado){
						case ("{1.1}"): 
							NumeroTabla += 1; 
							P.pop();
							PAtr.pop();
						break;
					
						case ("{1.2}"):
							popAux(1);
						break;
						
						case ("{2.1}"):
							popAux(2); //NUEVA
						break;
						
						case ("{3.1}"):
							popAux(2); //NUEVA
						break;

						case ("{4.1}"): 
							P.pop();
							PAtr.pop();
						break;
							
						case("{5.1}"):
							MainPdL.ZD = true; 
							P.pop();
							PAtr.pop();
						break; 

						case ("{5.2}") : 
							AuxAtr.pop();
						 	String cincopuntodos = AuxAtr.peek();
							if(cincopuntodos=="entero") {
								MainPdL.TSAct.getNodo(MainPdL.TSAct.getTamaño()-1).setTipo("entero");
								MainPdL.TSAct.getNodo(MainPdL.TSAct.getTamaño()-1).setAncho(2);
							}
							else if (cincopuntodos=="string") {
								MainPdL.TSAct.getNodo(MainPdL.TSAct.getTamaño()-1).setTipo("string");
								MainPdL.TSAct.getNodo(MainPdL.TSAct.getTamaño()-1).setAncho(128);
							}
							else if (cincopuntodos == "boolean"){
								MainPdL.TSAct.getNodo(MainPdL.TSAct.getTamaño()-1).setTipo("boolean");
								MainPdL.TSAct.getNodo(MainPdL.TSAct.getTamaño()-1).setAncho(2);
							}
							System.out.println(MainPdL.TSAct.getNodo(MainPdL.TSAct.getTamaño()-1).getLexema()+": "+MainPdL.TSAct.getNodo(MainPdL.TSAct.getTamaño()-1).getTipo());
							AuxAtr.push(" ");
							MainPdL.ZD = false; 
							P.pop(); 
							PAtr.pop();
						break; 

						case ("{5.3}"):
							popAux(4);
						break;

						case ("{6.1}"): 
							String eseSeis = AuxAtr.pop();
							AuxAtr.pop(); 
							String eSeis = AuxAtr.pop(); 
							AuxAtr.pop(); 
							AuxAtr.pop(); 
							String beSeis = AuxAtr.pop();
							if(eSeis == "boolean"/* && eseSeis == "OK"*/){
								beSeis = "OK"; 
							}
							else {
								beSeis = "ERROR";
								error=true;
							}
							AuxAtr.push(beSeis); 
							AuxAtr.push(" "); 
							AuxAtr.push(" "); 
							AuxAtr.push(eSeis); 
							AuxAtr.push(" "); 
							AuxAtr.push(eseSeis);
							popAux(5);
						break;

						case ("{7.1}"): 
							String S7 = AuxAtr.pop();
							AuxAtr.pop();
							AuxAtr.push(S7);
							AuxAtr.push(S7);
							popAux(1);
						break;

						case("{8.1}"):
							MainPdL.ZD = true; 
							P.pop(); 
							PAtr.pop();
						break; 

						case ("{8.2}"): 
							AuxAtr.pop();
						 	String ochopuntodos = AuxAtr.peek();
							if(ochopuntodos=="entero") {
								MainPdL.TSAct.getNodo(MainPdL.TSAct.getTamaño()-1).setTipo("entero");
								MainPdL.TSAct.getNodo(MainPdL.TSAct.getTamaño()-1).setAncho(2);
							}
							else if (ochopuntodos=="string") {
								MainPdL.TSAct.getNodo(MainPdL.TSAct.getTamaño()-1).setTipo("string");
								MainPdL.TSAct.getNodo(MainPdL.TSAct.getTamaño()-1).setAncho(128);
							}
							else if (ochopuntodos == "boolean"){
								MainPdL.TSAct.getNodo(MainPdL.TSAct.getTamaño()-1).setTipo("boolean");
								MainPdL.TSAct.getNodo(MainPdL.TSAct.getTamaño()-1).setAncho(2);
							}
							
							AuxAtr.push(" ");
							MainPdL.ZD = false; 
							P.pop();
							PAtr.pop();
						break;

						case ("{8.3}") :
							String E_atr8 = AuxAtr.pop();//E
							AuxAtr.push(" "); //;
							String id_atr8 = AuxAtr.pop();//id
							String T_atr8 = AuxAtr.pop();//T
							AuxAtr.pop();//let
							AuxAtr.pop();//(
							AuxAtr.pop();//for
							String B_atr8 = AuxAtr.pop();//B
							if(E_atr8.equals("boolean"))
								B_atr8="OK";
							else {
								B_atr8="ERROR";
								error=true;
							}
							AuxAtr.push(B_atr8);
							AuxAtr.push(" ");
							AuxAtr.push(" ");
							AuxAtr.push(" ");
							AuxAtr.push(T_atr8);
							AuxAtr.push(id_atr8);
							AuxAtr.push(" ");
							AuxAtr.push(E_atr8);
							P.pop(); 
							PAtr.pop(); 
						break; 

						case ("{8.4}") :
							String U84_atr = AuxAtr.pop();//U
							AuxAtr.pop();//;
							String E84_atr = AuxAtr.pop();//E
							AuxAtr.pop();//;
							String id84_atr = AuxAtr.pop();//id
							String T84_atr = AuxAtr.pop();//T
							AuxAtr.pop();//let
							AuxAtr.pop();//(
							AuxAtr.pop();//for
							String B84_atr = AuxAtr.pop();//B
							if(U84_atr=="entero"){
								B84_atr = "OK";
							}
							else{
								B84_atr = "ERROR";
								error=true;
							}
							AuxAtr.push(B84_atr);
							AuxAtr.push(" ");
							AuxAtr.push(" ");
							AuxAtr.push(" ");
							AuxAtr.push(T84_atr);
							AuxAtr.push(id84_atr);
							AuxAtr.push(" ");
							AuxAtr.push(E84_atr);
							AuxAtr.push(" ");
							AuxAtr.push(U84_atr);
							P.pop(); 
							PAtr.pop(); 
						break;
						
						case("{8.5}"):
							popAux(13); //NUEVA
						break;

						case ("{9.1}"): 
							AuxAtr.pop(); // atr de int
							AuxAtr.pop(); // antiguo atr de T 
							AuxAtr.push("entero");
							AuxAtr.push(" ");
							popAux(1);
						break;	

						case ("{10.1}"):
							AuxAtr.pop(); // atr de bool
							AuxAtr.pop(); // antiguo atr de T 
							AuxAtr.push("boolean");
							AuxAtr.push(" ");
							popAux(1);
						break;
						 	
						case ("{11.1}"): 
							AuxAtr.pop(); // atr de str
							AuxAtr.pop(); // antiguo atr de T 
							AuxAtr.push("string");
							AuxAtr.push(" ");
							popAux(1);
						break;

						case ("{12.1}"):
							String eseDos = AuxAtr.pop(); 
							String idDoce = AuxAtr.pop(); 
							String eseDoce = AuxAtr.pop(); 
							//if (idDoce == eseDos){
								eseDoce = "OK";
							/*}
							else {
								eseDoce = "ERROR"; 
								error=true;
							}*/
							AuxAtr.push(eseDoce); 
							AuxAtr.push(idDoce); 
							AuxAtr.push(eseDos); 
							popAux(2);
						break;

						case ("{13.1}"): 
							AuxAtr.pop(); 
							String equis = AuxAtr.pop(); 
							AuxAtr.pop(); 
							String ese = AuxAtr.pop();
							ese = equis; 
							AuxAtr.push(ese); 
							AuxAtr.push(" "); 
							AuxAtr.push(equis); 
							AuxAtr.push(" ");
							popAux(3);
						break;

						case ("{14.1}"):
							AuxAtr.pop(); //;
							AuxAtr.pop(); //)
							String E_atr=AuxAtr.pop();
							AuxAtr.pop(); //(
							AuxAtr.pop(); //print
							String S_atr=AuxAtr.pop();
							if(E_atr.equals(" ")||E_atr.equals("boolean")) {
								S_atr="ERROR";
								error=true;
							}	
							else
								S_atr="OK";
							AuxAtr.push(S_atr);
							AuxAtr.push(" ");
							AuxAtr.push(" ");
							AuxAtr.push(E_atr);
							AuxAtr.push(" ");
							AuxAtr.push(" ");
							popAux(5);
						break;

						case ("{15.1}"): 
							AuxAtr.pop(); //;
							AuxAtr.pop(); //)
							String id_atr=AuxAtr.pop();
							AuxAtr.pop(); //(
							AuxAtr.pop(); //input
							String S_atr15=AuxAtr.pop();
							if(id_atr.equals("entero")||id_atr.equals("string")) {
								S_atr15="OK";
							}	
							else{
								S_atr15="ERROR";
								error=true;
							}
							AuxAtr.push(S_atr15);
							AuxAtr.push(" ");
							AuxAtr.push(" ");
							AuxAtr.push(id_atr);
							AuxAtr.push(" ");
							AuxAtr.push(" ");
							popAux(5);
						break;

						case ("{16.1}"):
							String e16_atr = AuxAtr.pop();
							AuxAtr.pop();
							String s16_atr = AuxAtr.pop();
							if(e16_atr=="entero" || e16_atr=="boolean" || e16_atr=="string"||e16_atr=="function"){
								s16_atr = e16_atr;
							}
							else{
								s16_atr = "ERROR";
								error = true;
							}
							AuxAtr.push(s16_atr);
							AuxAtr.push(" ");
							AuxAtr.push(e16_atr);
							P.pop();
							PAtr.pop();
						break;
						
						case ("{16.2}"): 
							popAux(3); //NUEVA
						break;

						case ("{17.1}"):
							String ele = AuxAtr.pop(); 
							AuxAtr.pop(); 
							String sdos = AuxAtr.pop(); 
							if (ele == "function" || ele==" "){
								sdos = ele; 								
							}
							else {
								sdos = "ERROR"; 
								error=true;
							}
							AuxAtr.push(sdos); 
							AuxAtr.push(" "); 
							AuxAtr.push(ele); 
							P.pop();
							PAtr.pop();
						break;
						
						case ("{17.2}"): 
							popAux(4); //NUEVA
						break;

						case ("{18.1}"): 
							String E=AuxAtr.pop();
							AuxAtr.pop();
							String S2=AuxAtr.pop();
							if(E.equals("boolean"))
								S2="boolean";
							else {
								S2="ERROR";
								error=true;
							}
							AuxAtr.push(S2);
							AuxAtr.push(" ");
							AuxAtr.push(E);
							P.pop();
							PAtr.pop();
						break;
						
						case ("{18.2}"): 
							popAux(3); 
						break;

						case ("{19.1}"): 
							String e19_atr = AuxAtr.pop();
							AuxAtr.pop();
							AuxAtr.push(e19_atr);
							AuxAtr.push(e19_atr);
							popAux(1);
						break;

						case ("{20.1}"): 
							P.pop();
							PAtr.pop();
						break;

						case ("{21.1}"): 
							String cdos = AuxAtr.pop(); 
							String be = AuxAtr.pop(); 
							String cuno = AuxAtr.pop(); 
							if (cdos ==  " "){
								cuno = be;
							}
							else {
								cuno = cdos;
							}
							AuxAtr.push(cuno); 
							AuxAtr.push(be); 
							AuxAtr.push(cdos); 
							popAux(2);
						break;

						case ("{22.1}"): 
							P.pop();
							PAtr.pop();
						break;
						
						case ("{23.1}"):
							String q23_atr = AuxAtr.pop();
							String e23_atr = AuxAtr.pop();
							String l23_atr = AuxAtr.pop();
							if(e23_atr == "ERROR" || q23_atr == "ERROR"){
								l23_atr = "ERROR";
								error = true;
							}
							AuxAtr.push(l23_atr);
							AuxAtr.push(e23_atr);
							AuxAtr.push(q23_atr);
							popAux(2); //NUEVA
						break;

						case ("{24.1}"): 
							P.pop();
							PAtr.pop();
						break;
						
						case ("{25.1}"): 
							String cudos = AuxAtr.pop(); 
							String eVeinticinco = AuxAtr.pop(); 
							AuxAtr.pop(); 
							String cuuno = AuxAtr.pop(); 
							if (cudos == " "){
								cuuno = eVeinticinco; 
							}
							else {
								cuuno = cudos; 
							}
							AuxAtr.push(cuuno); 
							AuxAtr.push(" ");
							AuxAtr.push(eVeinticinco);
							AuxAtr.push(cudos);	
							popAux(3);
						break;

						case ("{26.1}"): 
							P.pop();
							PAtr.pop();
						break;

						case ("{27.1}"): 
							MainPdL.ZD = true; 
							P.pop();
							PAtr.pop();
						break;

						case ("{27.2}"): 
								AuxAtr.pop();
								String function = Aux.pop();
								AuxAtr.pop(); AuxAtr.pop();
								MainPdL.TSAct.getNodo(MainPdL.TSAct.getTamaño()-1).setTipo("function");
								AuxAtr.push(function);
								AuxAtr.push(" ");
								MainPdL.TSG=MainPdL.TSAct;
								MainPdL.TSAct=MainPdL.TSL;
								desGlobal = desActual;
								desActual = desLocal; 								
								P.pop();
								PAtr.pop();
								
						break;
						
						case ("{27.3}"): 
							MainPdL.ZD = false; 
							cuerpoFuncion=true;
							P.pop(); 
							PAtr.pop();
						break; 

						case ("{27.4}"):
							//AccSem
							AuxAtr.pop(); //cierrallave
							String C_atr=AuxAtr.pop(); //C
							AuxAtr.pop(); //abrellave
							AuxAtr.pop(); //cierraparentesis
							String A_atr=AuxAtr.pop(); //A
							AuxAtr.pop(); //abreparentesis
							String H_atr=AuxAtr.pop(); //H
							String fun_atr27 = AuxAtr.pop(); // function
							String F_atr=AuxAtr.pop(); //F
							if(C_atr.equals(H_atr) || (C_atr=="OK"&&H_atr==" "))
								F_atr=H_atr;
							else {
								F_atr="ERROR";
								error=true;
							}
							AuxAtr.push(F_atr);
							AuxAtr.push(fun_atr27);
							AuxAtr.push(H_atr);
							AuxAtr.push(" ");
							AuxAtr.push(A_atr);
							AuxAtr.push(" ");
							AuxAtr.push(" ");
							AuxAtr.push(C_atr);
							AuxAtr.push(" ");
							//DestruirTSL
							ALex.w2.println("Tabla Local de la funcion #" + NumeroTabla + ":" );
							NumeroTabla +=1;
							numParamFunciones[numFunciones]=numParam;
							numParam=0;
							numFunciones +=1;
							cuerpoFuncion=false;
							for(int j=0; MainPdL.TSAct.getCasillas()[j]!=null;j++) {
								ALex.w2.println("* Lexema: '" + MainPdL.TSAct.getCasillas()[j].getLexema()+ "'");
								ALex.w2.println("  + Tipo: '" + MainPdL.TSAct.getCasillas()[j].getTipo()+ "'");
								ALex.w2.println("  + Desplazamiento: '" + ASt.desActual+ "'");
								ASt.desActual+=MainPdL.TSAct.getCasillas()[j].getAncho();
							}
							ALex.w2.println("-----------------------------------");
							ALex.w2.println();
							MainPdL.TSAct=MainPdL.TSG;
							desActual = desGlobal; 
							popAux(8);
						break; 		
						
						case("{28.1}"):
							String Tatr = AuxAtr.pop();
							MainPdL.TSG.getNodo(MainPdL.TSG.getTamaño()-1).setTipoRetorno(Tatr);
							AuxAtr.pop();
							AuxAtr.push(Tatr);
							AuxAtr.push(Tatr);
							popAux(1);
						break;
						
						case("{29.1}"):
							P.pop();
							PAtr.pop();
						break;
						
						case("{30.1}"):
							AuxAtr.pop();
						 	String treintapuntouno = AuxAtr.peek();
							if(treintapuntouno=="entero") {
								paramFunciones[numParam]="entero";
								MainPdL.TSAct.getNodo(MainPdL.TSAct.getTamaño()-1).setTipo("entero");
								MainPdL.TSAct.getNodo(MainPdL.TSAct.getTamaño()-1).setAncho(2);
							}
							else if (treintapuntouno=="string") {
								paramFunciones[numParam]="string";
								MainPdL.TSAct.getNodo(MainPdL.TSAct.getTamaño()-1).setTipo("string");
								MainPdL.TSAct.getNodo(MainPdL.TSAct.getTamaño()-1).setAncho(128);
							}
							else if (treintapuntouno == "boolean"){
								paramFunciones[numParam]="boolean";
								MainPdL.TSAct.getNodo(MainPdL.TSAct.getTamaño()-1).setTipo("boolean");
								MainPdL.TSAct.getNodo(MainPdL.TSAct.getTamaño()-1).setAncho(2);
							}
							
							AuxAtr.push(" ");
							MainPdL.ZD = false;
							numParam++;
							P.pop();
							PAtr.pop();
						break;
						
						case ("{30.2}"): 
							popAux(3); //NUEVA
						break;
						
						case("{31.1}"):
							P.pop();
							PAtr.pop();
						break;
						
						case("{32.1}"):
							MainPdL.ZD = true;
							P.pop();
							PAtr.pop();
						break;
						
						case("{32.2}"):
							AuxAtr.pop();
						 	String treintaydospuntodos = AuxAtr.peek();
							if(treintaydospuntodos=="entero") {
								paramFunciones[numParam]="entero";
								MainPdL.TSAct.getNodo(MainPdL.TSAct.getTamaño()-1).setTipo("entero");
								MainPdL.TSAct.getNodo(MainPdL.TSAct.getTamaño()-1).setAncho(2);
							}
							else if (treintaydospuntodos=="string") {
								paramFunciones[numParam]="string";
								MainPdL.TSAct.getNodo(MainPdL.TSAct.getTamaño()-1).setTipo("string");
								MainPdL.TSAct.getNodo(MainPdL.TSAct.getTamaño()-1).setAncho(128);
							}
							else if (treintaydospuntodos == "boolean"){
								paramFunciones[numParam]="boolean";
								MainPdL.TSAct.getNodo(MainPdL.TSAct.getTamaño()-1).setTipo("boolean");
								MainPdL.TSAct.getNodo(MainPdL.TSAct.getTamaño()-1).setAncho(2);
							}
							numParam++;
							AuxAtr.push(" ");
							MainPdL.ZD = false;
							P.pop();
							PAtr.pop();
						break;
						
						case ("{32.3}"): 
							popAux(4); //NUEVA
						break;
						
						case("{33.1}"):
							P.pop();
							PAtr.pop();
						break;
						
						case("{34.1}"):
							String edos_atr = AuxAtr.pop();
							String erre_atr=AuxAtr.pop();
							String e_atr=AuxAtr.pop();
							if(!(edos_atr.equals(" "))) {
								if(edos_atr.equals(erre_atr)) {
									e_atr=erre_atr;
								}
								else {
									e_atr="ERROR";
									error=true;
								}
							}
							else {
								e_atr=erre_atr;
							}
							AuxAtr.push(e_atr);
							AuxAtr.push(erre_atr);
							AuxAtr.push(edos_atr);
							popAux(2);
						break;
						
						case("{35.1}"):
							P.pop();
							PAtr.pop();
						break;
						
						case("{36.1}"):
							String edosdos = AuxAtr.pop(); 
							String erre = AuxAtr.pop(); 
							AuxAtr.pop();
							String edos = AuxAtr.pop();
							if (edosdos == " "){
								if (erre == "boolean"){
									edos = "boolean";
								}
								else {
									edos = "ERROR"; 
									error=true;
								}
							}	
							else {
								edos = edosdos; 
							}	
							AuxAtr.push(edos); 
							AuxAtr.push(" ");
							AuxAtr.push(erre);
							AuxAtr.push(edosdos);
							popAux(3);
						break;
						
						case("{37.1}"):
							String rdos_atr = AuxAtr.pop();
							String u_atr = AuxAtr.pop();
							String r_atr = AuxAtr.pop();
							if(!(rdos_atr==" ")) {
								if(rdos_atr.equals(u_atr)) {
									r_atr="boolean";
								}
								else {
									r_atr="ERROR";
									error=true;
								}
							}
							else {
								r_atr=u_atr;
							}

							AuxAtr.push(r_atr);
							AuxAtr.push(u_atr);
							AuxAtr.push(rdos_atr);
							popAux(2);
						break;
						
						case("{38.1}"):
							P.pop();
							PAtr.pop();
						break;
						
						case("{39.1}"):
							String rdosdos = AuxAtr.pop(); 
							String uTreintaNueve = AuxAtr.pop();
							AuxAtr.pop(); 
							String ere = AuxAtr.pop();

							if (rdosdos == " "){
								ere = uTreintaNueve; 
							}
							else {
								ere = rdosdos; 
							}

							AuxAtr.push(ere); 
							AuxAtr.push(" "); 
							AuxAtr.push(uTreintaNueve); 
							AuxAtr.push(rdosdos); 
							popAux(3);
						break;
						
						case("{40.1}"):
							String udos_atr = AuxAtr.pop();
							String v_atr=AuxAtr.pop();
							String uu_atr=AuxAtr.pop();
							if(!(udos_atr==" ")) {
								if(udos_atr.equals(v_atr)) {
									uu_atr=v_atr;
								}
							else {
								uu_atr="ERROR";
								error=true;
								}
							}
							else {
								uu_atr=v_atr;
							}
							AuxAtr.push(uu_atr);
							AuxAtr.push(v_atr);
							AuxAtr.push(udos_atr);
							popAux(2);
						break;
						
						case("{41.1}"):
							P.pop();
							PAtr.pop();
						break;
						
						case("{42.1}"):
							String udos422_atr = AuxAtr.pop();
							String v42_atr = AuxAtr.pop();
							AuxAtr.pop();
							String udos42_atr = AuxAtr.pop();
							if(udos422_atr==" "){
								udos42_atr = v42_atr;
							}
							else{
								udos42_atr = udos422_atr;
							}
							AuxAtr.push(udos42_atr);
							AuxAtr.push(" ");
							AuxAtr.push(v42_atr);
							AuxAtr.push(udos422_atr);
							popAux(3);
						break;
						
						case("{43.1}"):
							String vdos_atr=AuxAtr.pop();
							String id43_atr=AuxAtr.pop();
							String v_attr=AuxAtr.pop();
							if(vdos_atr.equals(" ")){
								v_attr=id43_atr;
							}
							//else if(vdos_atr.equals(id43_atr))
							else
								v_attr=vdos_atr;
							/*else {
								v_attr="ERROR";
								error=true;
							}*/
							AuxAtr.push(v_attr);
							AuxAtr.push(id43_atr);
							AuxAtr.push(vdos_atr);
							popAux(2);
						break;
						
						case("{44.1}"):
							AuxAtr.pop();//parentesis
							String e44=AuxAtr.pop();
							AuxAtr.pop();//parentesis
							AuxAtr.pop();//atr de V2
							AuxAtr.push(e44);
							AuxAtr.push(" ");
							AuxAtr.push(e44);
							AuxAtr.push(" ");
							popAux(3);
						break;
						
						case("{45.1}"):
							String atrEnt1 = Aux.pop();
							AuxAtr.pop();
							AuxAtr.pop();
							AuxAtr.push(atrEnt1);
							Aux.push(atrEnt1);
							AuxAtr.push(" ");
							popAux(1);
						break;
						
						case("{46.1}"):
							String atrCad = Aux.pop();
							AuxAtr.pop();
							AuxAtr.pop(); 
							AuxAtr.push("string");
							Aux.push(atrCad);
							AuxAtr.push(" ");
							popAux(1);
						break;
						
						case("{47.1}"):
							AuxAtr.pop();//parentesis
							String ele47=AuxAtr.pop();
							AuxAtr.pop();//parentesis
							AuxAtr.pop();//atr de V2
							AuxAtr.push(ele47);
							AuxAtr.push(" ");
							AuxAtr.push(ele47);
							AuxAtr.push(" ");
							popAux(3);
						break;
						
						case("{48.1}"):
							P.pop();
							PAtr.pop();
						break;
					} //fin de switch
				} // fin de if	
			} //fin de for
			
			if(!encontrado){
				// Si estado no pertenece a ninguno de los tres conjuntos se trata de un error
				error=true;
			}
		} // fin de while 
	} // fin de main

} // fin de ASt

