package guimain;

import java.io.Serializable;

class AmgParam implements Serializable {
	private static final long serialVersionUID = 1L;
	private String a;
	private String m;
	private String g;
	public AmgParam(String a, String m, String g) {
		this.a = a;
		this.m = m;
		this.g = g;
	}
	public String getA() {
		return a;
	}
	public String getM() {
		return m;
	}
	public String getG() {
		return g;
	}
}
