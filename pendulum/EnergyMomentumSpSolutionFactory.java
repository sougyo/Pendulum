package pendulum;

public class EnergyMomentumSpSolutionFactory {
	public static SpSolution create(SpParam spParam, EmParam emParam, double timeInterval) {
		double a = spParam.getA();
		double m = spParam.getM();
		double g = spParam.getG();
		double h = emParam.getH();
		double l = emParam.getL();
		CubicEquationRoot root = Cardano.solve(1.0, -h /(g*m), -a*a, a*a*h/(g*m) - l*l/(2*g*m*m));
		
		if (!root.isReal())
			return null;
		
		Z z = new Z(root, spParam, emParam.getDelta());
		DotZ dotZ = new DotZ(z, spParam);
		R r = new R(z, spParam);
		DotPhi dotPhi = new DotPhi(r, spParam, emParam);
		DotR dotR = new DotR(r, z, dotZ, spParam, emParam);
		Phi phi = new Phi(dotPhi, dotZ, r, emParam, emParam.getPhi0());
		
		return new ConcreteSpSolution(timeInterval, r, phi, z, dotR, dotPhi, dotZ);
	}
}

class Z implements Function {
	private double alpha;
	private double beta;
	private double gamma;
	private double delta;
	private double a, g;
	private double k;
	public Z(CubicEquationRoot root, SpParam param, double delta) {
		this.alpha = root.getReal1();
		this.beta = root.getReal2();
		this.gamma = root.getReal3();
		this.delta = delta;
		this.k = Math.sqrt((beta - alpha) / (gamma - alpha));
		this.a = param.getA();
		this.g = param.getG();
	}
	public double of(double t) {
		if (alpha == beta)
			return alpha;
		double coefficient = Math.sqrt((gamma - alpha) * g / 2) / a;
		double sn = JacobiEllipticFunction.sn(coefficient * t + delta, k);
		return alpha + (beta - alpha) * sn * sn;
	}
	public double getAlpha() {
		return alpha;
	}
	public double getBeta() {
		return beta;
	}
	public double getGamma() {
		return gamma;
	}
}

class DotZ implements Function {
	private Z z;
	private double c;
	private double alpha;
	private double beta;
	private double gamma;
	private static final double EPSILON = 0.001;
	public DotZ(Z z, SpParam param) {
		this.z = z;
		this.c = 2 * param.getG() / param.getA();
		this.alpha = z.getAlpha();
		this.beta = z.getBeta();
		this.gamma = z.getGamma();
	}
	private double cal(double t) {
		double z_val = z.of(t);
		return Math.sqrt(c * (z_val - alpha) * (z_val - beta) * (z_val - gamma));
	}
	private int sgn(double t) {
		return (z.of(t + EPSILON) - z.of(t) >= 0) ? 1 : -1;
	}
	public double of(double t) {
		return sgn(t) * cal(t);
	}
}

class R implements Function {
	private Z z;
	private double a;
	public R(Z z, SpParam param) {
		this.z = z;
		this.a = param.getA();
	}
	public double of(double t) {
		double z_val = z.of(t);
		return Math.sqrt(a*a - z_val * z_val);
	}
}

class DotPhi implements Function {
	private double l;
	private double m;
	private R r;
	public DotPhi(R r, SpParam spParam, EmParam emParam) {
		this.r = r;
		this.m = spParam.getM();
		this.l = emParam.getL();
	}
	public double of(double t) {
		if (l == 0)
			return 0;
		double r_val = r.of(t);
		return l / (m * r_val * r_val);
	}
}

class DotR implements Function {
	private static final double EPSILON = 0.001;
	private Z z;
	private DotZ dotZ;
	private R r;
	private double h;
	private double l;
	private double m;
	private double g;
	public DotR(R r, Z z, DotZ dotZ, SpParam spParam, EmParam emParam) {
		this.r = r;
		this.z = z;
		this.dotZ = dotZ;
		this.h = emParam.getH();
		this.l = emParam.getL();
		this.m = spParam.getM();
		this.g = spParam.getG();
	}
	private int sgn(double t) {
		return (r.of(t + EPSILON) - r.of(t) >= 0)? 1 : -1;
	}
	public double of(double t) {
		if (l == 0) {
			return sgn(t) * Math.sqrt(2*(h-m*g*z.of(t)) - dotZ.of(t) * dotZ.of(t));
		} else {
			return - z.of(t) * dotZ.of(t) / r.of(t);
		}
	}
}

class Phi implements Function {
	private DotPhi dotPhi;
	private DotZ dotZ;
	private R r;
	private double tau;
	private double phiTau;
	private double l;
	
	private double prevDotZ;
	private double prevR;
	public Phi(DotPhi dotPhi, DotZ dotZ, R r, EmParam emParam, double phi0) {
		this.dotPhi = dotPhi;
		this.dotZ = dotZ;
		this.r = r;
		this.tau = 0;
		this.phiTau = phi0;
		this.l = emParam.getL();
		
		prevDotZ = dotZ.of(0);
		prevR = r.of(0);
	}
	public double of(double t) {
		if (t == tau)
			return phiTau;
		
		if (l != 0) {
			phiTau += Integrator.integration(dotPhi, tau, t, 64);
			tau = t;
			return phiTau;
		} else {
			if (prevDotZ * dotZ.of(t) < 0 && Math.abs((r.of(t) + prevR)*0.5) < 0.1) 
				phiTau += Math.PI;
			prevDotZ = dotZ.of(t);
			prevR = r.of(t);
			return phiTau;
		}
	}
}

class ConcreteSpSolution implements SpSolution {
	private R r;
	private Phi phi;
	private Z z;
	private DotR dotR;
	private DotPhi dotPhi;
	private DotZ dotZ;
	
	private double timeInterval;
	private double t;
	public ConcreteSpSolution(double timeInterval, R r, Phi phi, Z z, DotR dotR, DotPhi dotPhi, DotZ dotZ) {
		this.r = r;
		this.phi = phi;
		this.z = z;
		this.dotR = dotR;
		this.dotPhi = dotPhi;
		this.dotZ = dotZ;
		
		this.timeInterval = timeInterval;
		t = 0;
	}
	
	public SpCoordinate next() {
		SpCoordinate coordinate = new SpCoordinate(t, r.of(t), phi.of(t), z.of(t),
				dotR.of(t), dotPhi.of(t), dotZ.of(t));
		t += timeInterval;
		return coordinate;
	}
}
