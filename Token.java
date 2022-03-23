package PdL;

// Clase devuelta por el ALex. Tienen como atributos el codigo y atributo de el token del texto analizado

public class Token {
	
	private String codigo;
	private String atributo;
	
	public Token(String codigo, String atributo) {
		this.codigo=codigo;
		this.atributo=atributo;
	}
	
	public String getCodigo() {
		return codigo;
	}
	
	public String getAtributo() {
		return atributo;
	}
	
	public void setCodigo(String codigo) {
		this.codigo=codigo;
	}
	
	public void setAtributo(String atributo) {
		this.atributo=atributo;
	}

}
