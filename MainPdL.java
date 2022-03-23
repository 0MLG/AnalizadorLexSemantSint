package PdL;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.util.Scanner;

public class MainPdL {
	
	static String ruta = "";
	static Tabla_de_Simbolos TSG = new Tabla_de_Simbolos(100);
	static Tabla_de_Simbolos TSL = new Tabla_de_Simbolos(100);
	static Tabla_de_Simbolos TSAct = TSG;
	static Token tk=new Token("","");
	
	static boolean ZD=false;
	static boolean errFich=false;

	public static void main (String[] args) throws IOException {
		//Bienvenida e introducción de la ruta del fichero a analizar
		System.out.println("Bienvenido a la Práctica de Procesadores de Lenguajes");
		System.out.println("Inserte la ruta completa del fichero:");
		Scanner teclado = new Scanner(System.in);
		String ruta = teclado.nextLine();
		System.out.println("Se efectuará el análisis del fichero con ruta:");
		System.out.println(ruta);
		//Inicio del análisis. Inicializa el ALex y el ASt
		new ALex(ruta);
		ASt.crearASt();
		if(errFich)
			// Si ha habido un error de lectura de algun fichero se finaliza la ejecucion
			return;
		ASt.main();
		//En caso de error durante en analisis (consultar errores.txt)
		if(ASt.error) {
			System.out.println("Ha habido un error");
			ALex.err.print("Error en la línea " + ALex.linea);
		}
		//Al final de la ejecucución se escribe la TSG en TS.txt
		ALex.w2.println("Tabla Global # 1:");
		int cont=0;
		int funcionesEscritas=0;
		int k=0;
		for(int i=0;TSG.getCasillas()[i]!=null;i++) {
			ALex.w2.println("* LEXEMA: '" + TSG.getCasillas()[i].getLexema()+ "'");
			ALex.w2.println("  + tipo: '" + TSG.getCasillas()[i].getTipo()+ "'");
			if(TSG.getCasillas()[i].getTipo()=="function") {
				ALex.w2.println("  + numParam: " + ASt.numParamFunciones[funcionesEscritas]);
			    if(ASt.numParamFunciones[funcionesEscritas]!=0){
			        while(k<ASt.numParamFunciones[funcionesEscritas]){
			            ALex.w2.println("   + TipoParam"+(cont+1)+": '"+ASt.paramFunciones[cont]+"'");
			            ALex.w2.println("     + ModoParam"+(cont+1)+": 1 (es por valor)");
			            cont++;
			            k++;
			        }
			    }
			    funcionesEscritas++;
			    
				ALex.w2.println("    + TipoRetorno: '" + TSG.getCasillas()[i].getTipoRetorno() + "'");
				ALex.w2.println("  + EtiqFuncion: 'Et" + TSG.getCasillas()[i].getLexema()+ "01'");
			}
			else {
				ALex.w2.println("  + despl: '" + ASt.desActual+ "'");
				ASt.desActual+=TSG.getCasillas()[i].getAncho();
			}
		}
		//Fin del analisis
		ALex.close();
		ASt.parse.close();
		ASt.err.close();
		System.out.println("Fin del programa");
	}

}
