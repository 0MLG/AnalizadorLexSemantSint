package PdL;

// Los nodos serán los elementos de la TS. Incluyen atributos necesarios para el análisis como el lexema, tipo o ancho del id

public class Nodo {
	
	private String lexema;
	private String tipo;
	private String tipoRetorno;
	private int ancho;
	private int posicion;
	private Nodo siguiente;

	public Nodo(String lexema, int posicion) {
		this.lexema = lexema;
		this.posicion=posicion;
		this.siguiente = null;
	}

	public String getLexema() {
		return lexema;
	}
	
	public String getTipo(){
		return tipo;
	}
	
	public String getTipoRetorno() {
		return tipoRetorno;
	}
	
	public int getAncho() {
		return ancho;
	}
	
	public int getPosicion() {
		return posicion;
	}

	public Nodo getSiguiente() {
		return siguiente;
	}
	
	public void setTipo(String tipo) {
		this.tipo=tipo;
	}
	
	public void setTipoRetorno(String tipo) {
		this.tipoRetorno=tipo;
	}
	
	public void setAncho(int ancho) {
		this.ancho=ancho;
	}

	public void setSiguiente(Nodo siguiente) {
		this.siguiente = siguiente;
	}
}
