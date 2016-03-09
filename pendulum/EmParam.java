package pendulum;

import java.io.Serializable;

/**
 * Energy-momentum parameter
 * @author shogom
 *
 */
public class EmParam implements Serializable {
	private static final long serialVersionUID = 1L;

	private double h;
	private double l;
	private double delta;
	private double phi0;
	
	public EmParam(double h, double l, double delta, double phi0) {
		this.h = h;
		this.l = l;
		this.delta = delta;
		this.phi0 = phi0;
	}
	
	public double getH() {
		return h;
	}
	public double getL() {
		return l;
	}
	public double getDelta() {
		return delta;
	}
	public double getPhi0() {
		return phi0;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("h:");
		builder.append(h);
		builder.append(" l:");
		builder.append(l);
		builder.append(" delta:");
		builder.append(delta);
		builder.append(" phi0:");
		builder.append(phi0);
		return builder.toString();
	}
}
