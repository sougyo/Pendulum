package pendulum;

public class Integrator {
	public static double integration(Function f, double a, double b, int DIV) {
		double simpson = 0;
		double h = b - a, trapezoid = h * (f.of(a) + f.of(b)) / 2;
		for (int n = 1; n <= DIV; n *= 2) {
			double midpoint = 0;
			for (int i = 1; i <= n; i++)
				midpoint += f.of(a + h * (i - 0.5));
			midpoint *= h;
			simpson = (trapezoid + 2 * midpoint) / 3;
			h /= 2;
			trapezoid = (trapezoid + midpoint) / 2;
	    }
		return simpson;
	}
}
