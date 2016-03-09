package pendulum;

/**
 * solve a cubic equation by Cardano's method
 * @author shogom
 *
 */
public class Cardano {
	private static double cuberoot(double x){
		boolean pos = true;
		
		if (x == 0)
			return 0;
		if (x < 0) {
			pos = false;  x = -x;  }
		double s = (x > 1) ? x : 1, prev;

		do {
			prev = s;  s = (x / (s * s) + 2 * s) / 3;
		} while (s < prev);
		return pos ? prev : -prev;
    }
    
    /**
     * This method solve the cubic equation a x^3 + b x^2 + c x + d = 0
     *  where 'a' is not equal to 0. 
     * @param a coefficient of x^3
     * @param b coefficient of x^2
     * @param c coefficient of x
     * @param d constant
     * @return 
     */
    public static CubicEquationRoot solve(double a, double b, double c, double d) {
    	b /= (3 * a);  c /= a;  d /= a;
    	double p = b * b - c / 3, q = (b * (c - 2 * b * b) - d) / 2;

    	a = q * q - p * p * p;
    	if (a == 0) {
    		q = cuberoot(q);
    		double x1 = 2 * q - b, x2 = -q - b;
    		return new CubicEquationRoot(x1, x2, x2, true);
    	} else if (a > 0) {
    		double a3;
    		if (q > 0)
    			a3 = cuberoot(q + Math.sqrt(a));
    		else
    			a3 = cuberoot(q - Math.sqrt(a));
    		double b3 = p / a3;
    		double x1 = a3 + b3 - b;
    		double x2 = -0.5 * (a3 + b3) - b;
    		double x3 = Math.abs(a3 - b3) * Math.sqrt(3.0) / 2;
    		return new CubicEquationRoot(x1, x2, x3);
    	} else {
    		a = Math.sqrt(p);
    		double t = Math.acos(q / (p * a));  a *= 2;
    		double x1 = a * Math.cos( t                / 3) - b;
    		double x2 = a * Math.cos((t + 2 * Math.PI) / 3) - b;
    		double x3 = a * Math.cos((t + 4 * Math.PI) / 3) - b;
    		return new CubicEquationRoot(x1, x2, x3, false);
    	}
    }
}
