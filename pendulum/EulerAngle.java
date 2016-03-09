package pendulum;

import java.io.Serializable;

public class EulerAngle implements Serializable {
	private static final long serialVersionUID = 1L;
	private double phi, theta, psi;
	public EulerAngle(double phi, double theta, double psi) {
		this.phi = phi;
		this.theta = theta;
		this.psi = psi;
	}
	public double getPhi() {
		return phi;
	}
	public double getTheta() {
		return theta;
	}
	public double getPsi() {
		return psi;
	}
	public void normalize() {
		phi = mod(phi, 2 * Math.PI);
		theta = mod(theta, Math.PI);
		psi = mod(psi, 2 * Math.PI);
	}
	private double mod(double v, double m) {
		if (m < 0)
			return v;
		if (v > m) while (v >= m) v -= m;
		if (v < m) while (v < 0) v += m;
		return v;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("phi:");
		builder.append(phi);
		builder.append(" theta:");
		builder.append(theta);
		builder.append(" psi:");
		builder.append(psi);
		return builder.toString();
	}
}
