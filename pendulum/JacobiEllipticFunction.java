package pendulum;

public class JacobiEllipticFunction {
	private static double sn, cn, dn;
	private static final double EPSILON = 0.0003;
	public static synchronized double sn(double t, double k) {
		cal_sncndn(t, k);
		return sn;
	}
	public static double cn(double t, double k) {
		cal_sncndn(t, k);
		return cn;
	}
	public static double dn(double t, double k) {
		cal_sncndn(t, k);
		return dn;
	}
	
	private static void cal_sncndn_kneq0(double t, double emc) {
		double d = 0;
		double[] em = new double[14];
		double[] en = new double[14];
		int l = 0;
		if (emc < 0.0) {
			d = 1.0 - emc;
			emc /= -1.0/d;
			d = Math.sqrt(d);
			t *= d;
		}
		dn = 1.0;
		double a = 1.0, c = 0.0;
		for (int i = 1; i <= 13; i++) {
			l = i;
			em[i] = a;
			emc = Math.sqrt(emc);
			en[i] = emc;
			c = 0.5 * (a + emc);
			if (Math.abs(a - emc) <= EPSILON * a)
				break;
			emc *= a;
			a = c;
		}
		t *= c;
		sn = Math.sin(t);
		cn = Math.cos(t);
		if (sn != 0) {
			a = cn / sn;
			c *= a;
		
			for (int i = l; i >= 1; i--) {
				double b = em[i];
				a *= c;
				c *= dn;
				dn = (en[i] + a) / (b + a);
				a = c / b;
			}
			a = 1.0 / Math.sqrt(c * c + 1.0);
			sn = (sn >= 0.0) ? a : -a;
			cn = c * sn;
		}
		if (emc < 0.0) {
			a = dn;
			dn = cn;
			cn = a;
			sn /= d;
		}
	}
	
	private static void cal_sncndn(double t, double k) {
		double emc = 1 - k * k;
		if (emc != 0) {
			cal_sncndn_kneq0(t, emc);
		} else {
			cn = 1.0 / Math.cosh(t);
			dn = cn;
			sn = Math.tanh(t);
		}
	}
}
