package PdL;

// La TS es un array de Nodos

public class Tabla_de_Simbolos {

	private Nodo[] casillas;
	private int tamaño = 0;

    public Tabla_de_Simbolos(int numeroCasillas) {
        casillas = new Nodo[numeroCasillas];
    }

    public Nodo[] getCasillas() {
        return casillas;
    }
    
    // estaenTS: Comprueba si un id existe en la TS pasando como argumento su nombre
    
    public boolean estaEnTS(String s) {
    	boolean resul=false;
    	if(tamaño>0) {
    		for(int i=0; i<tamaño; i++) {
    			if(casillas[i].getLexema().equals(s)) {
    				resul=true;
    			}
    		}
    	}
    	return resul;
    }
    
    // getNodo: devuelve la posición de un id en la TS pasando como argumento su nombre
    
    public int getNodo(String nodo) {
    	int resul=-1;
    	if(tamaño>0) {
    		for(int i=0; i<tamaño; i++) {
    			if(casillas[i].getLexema().equals(nodo)) {
    				resul=i;
    			}
    		}
    	}
    	return resul;
    }
    
    // getNodo: devuelve el nodo de un id en la TS pasando como argumento su posicion
    
    public Nodo getNodo(int posicion) {
    	return casillas[posicion];
    }
    
    // getTamaño: devuelve el tamaño de la TS (número de nodos)
    
    public int getTamaño() {
    	return tamaño;
    }
    
    // insertarNodo: inserta un nuevo nodo en la TS
    
    public void insertarNodo(String string){
        int posicion =tamaño;
        tamaño++;
        System.out.println(string + " sera insertado en la posicion " + posicion+ "de la TS");
        casillas[posicion] = new Nodo(string, posicion);
    }
    
    // verTabla: permite ver los nombres de todos los nodos de la TS (usado para debugging)
    
    public void verTabla(){
        for (Nodo nodo : casillas){
            if (nodo != null){
                System.out.println(nodo.getLexema());
                while (nodo.getSiguiente() != null){
                    nodo = nodo.getSiguiente();
                    System.out.println("\t" + nodo.getLexema());
                }
            }
        }
    }
}
