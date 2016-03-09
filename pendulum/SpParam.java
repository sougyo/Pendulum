package pendulum;

public class SpParam {
	private double a;
	private double m;
	private double g;
	public SpParam(double a, double m, double g) {
		this.a = a;
		this.m = m;
		this.g = g;
	}
	public double getA() {
		return a;
	}
	public double getM() {
		return m;
	}
	public double getG() {
		return g;
	}
	@Override
	public String toString() {
		return "a:" + a + " m:" + m + " g:" + g;
	}
}
