package pendulum;

public class SpCoordinate {
	private double t;
	private double r, phi, z;
	private double dotR, dotPhi, dotZ;
	
	public SpCoordinate(double t, double r, double phi, double z, double dotR, double dotPhi, double dotZ) {
		this.t = t;
		this.r = r;
		this.phi = phi;
		this.z = z;
		this.dotR = dotR;
		this.dotPhi = dotPhi;
		this.dotZ = dotZ;
	}
	public double getT() {
		return t;
	}
	public double getR() {
		return r;
	}
	public double getPhi() {
		return phi;
	}
	public double getZ() {
		return z;
	}
	public double getDotR() {
		return dotR;
	}
	public double getDotPhi() {
		return dotPhi;
	}
	public double getDotZ() {
		return dotZ;
	}
	
	public double getX() {
		return r * Math.cos(phi);
	}
	public double getY() {
		return r * Math.sin(phi);
	}
	public double getDotX() {
		return dotR*Math.cos(phi) - r*dotPhi*Math.sin(phi);
	}
	public double getDotY() {
		return dotR*Math.sin(phi) + r*dotPhi*Math.cos(phi);
	}
	
	@Override
	public String toString() {
		return t + " " + r + " " + phi + " " + z + " " + dotR + " " + dotPhi + " " + dotZ;
	}
}
