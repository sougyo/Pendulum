package pendulum;
import java.util.Random;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class IntegratorTest {
	private static final double EPSILON = 0.001;
	
	private Random random = new Random();
	
	interface IntegrableFunction extends Function {
		@Override
		public double of(double x);
		public Function createIntegratedFunction();
	}
	
	@Test public void someFuncTest() {
		class LinearFunction implements IntegrableFunction {
			private double a;
			private double b;
			public LinearFunction(double a, double b) {
				this.a = a; this.b = b;
			}
			@Override
			public double of(double x) {
				return a * x + b;
			}
			@Override
			public Function createIntegratedFunction() {
				return new IntegratedFunction(this);
			}
			class IntegratedFunction implements Function {
				private LinearFunction f;
				public IntegratedFunction(LinearFunction f) {
					this.f = f;
				}
				@Override
				public double of(double x) {
					return 0.5 * f.a  * x * x + f.b * x;
				}
			}
			@Override
			public String toString() {
				return a + "x+" + b;
			}
		}
		
		class ExpFunction implements IntegrableFunction {
			private double a;
			public ExpFunction(double a) {
				this.a = a;
			}
			class IntegratedFunction implements Function {
				private double a;
				public IntegratedFunction(double a) {
					this.a = a;
				}
				@Override
				public double of(double x) {
					return Math.exp(a * x) / a;
				}
			}
			@Override
			public Function createIntegratedFunction() {
				return new IntegratedFunction(a);
			}

			@Override
			public double of(double x) {
				return Math.exp(a*x);
			}
			@Override
			public String toString() {
				return "exp(" + a + "x)";
			}
		}
		
		class Monomial implements IntegrableFunction {
			int n;
			public Monomial(int n) {
				this.n = n;
			}
			class IntegratedFunction implements Function {
				int n;
				public IntegratedFunction(int n) {
					this.n = n;
				}
				@Override
				public double of(double x) {
					return Math.pow(x, (double)(n + 1)) / (double)(n + 1);
				}
			}
			@Override
			public Function createIntegratedFunction() {
				return new IntegratedFunction(n);
			}

			@Override
			public double of(double x) {
				return Math.pow(x, (double)n);
			}
			@Override
			public String toString() {
				return "x^" + n;
			}
		}
		class SinFunction implements IntegrableFunction {
			private double omega;
			public SinFunction(double omega) {
				this.omega = omega;
			}
			class IntegratedFunction implements Function {
				private double omega;
				public IntegratedFunction(double omega) {
					this.omega = omega;
				}
				@Override
				public double of(double x) {
					return -Math.cos(omega * x) / omega;
				}
			}
			@Override
			public Function createIntegratedFunction() {
				return new IntegratedFunction(omega);
			}

			@Override
			public double of(double x) {
				return Math.sin(omega * x);
			}
			@Override
			public String toString() {
				return "sin(" + omega + "x)";
			}
		}

		testSomeInterval(100, 100, new LinearFunction(2.0, 3.0));
		testSomeInterval(100, 100, new LinearFunction(1.0, -1.0));
		testSomeInterval(100, 2, new ExpFunction(2.0));
		testSomeInterval(100, 2, new ExpFunction(0.1498754));
		testSomeInterval(100, 20, new Monomial(2));
		testSomeInterval(100, 4, new Monomial(5));
		testSomeInterval(100, 50, new SinFunction(2.0));
		testSomeInterval(100, 50, new SinFunction(1.939858392));
	}
	
	private void testSomeInterval(int n, double scale, IntegrableFunction f) {
		for (int i = 0; i < n; i++) {
			double min = random.nextDouble() * (scale * 2.0) - scale;
			double max = random.nextDouble() * (scale * 2.0) - scale;
			test(min, max, f);
		}
	}
	
	private void test(double min, double max, IntegrableFunction f) {
		System.out.println("min:" + min + " max:" + max + " f:" + f.toString());
		Function F = f.createIntegratedFunction();
		assertEquals(F.of(max) - F.of(min), Integrator.integration(f, min, max, 1024), EPSILON);
	}
}
