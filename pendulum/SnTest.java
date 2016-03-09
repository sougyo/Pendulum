package pendulum;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class SnTest {
	private double EPSILON = 0.00001;
	private boolean nearlyEqual(double x, double y) {
		return Math.abs(x - y) < EPSILON;
	}
	
	@Test public void valueTest() throws IOException {
		class Data {
			private double t, k, sn, cn, dn;
			public Data(double t, double k, double sn, double cn, double dn) {
				this.t = t;
				this.k = k;
				this.sn = sn;
				this.cn = cn;
				this.dn = dn;
			}
			@Override
			public String toString() {
				return t + " " + k + " " + sn + " " + cn + " " + dn;
			}
		};
	
		ArrayList<Data> dataList = new ArrayList<Data>();
		BufferedReader br = new BufferedReader(new FileReader("/home/shogom/workspace/test/bin/jacobi_elliptic_funcs_test.dat"));
		String line = br.readLine();
		while (line != null && line.length() != 0) {
			StringTokenizer st = new StringTokenizer(line);
			double t = Double.valueOf(st.nextToken());
			double k = Double.valueOf(st.nextToken());
			double sn = Double.valueOf(st.nextToken());
			double cn = Double.valueOf(st.nextToken());
			double dn = Double.valueOf(st.nextToken());
			
			dataList.add(new Data(t, k, sn, cn, dn));
			
			line = br.readLine();
		}
		br.close();
 		
		System.out.println(dataList.size());
		for(Data data : dataList) {
			System.out.println(data.toString());
			assertTrue(nearlyEqual(JacobiEllipticFunction.sn(data.t, data.k), data.sn));
			assertTrue(nearlyEqual(JacobiEllipticFunction.cn(data.t, data.k), data.cn));
			assertTrue(nearlyEqual(JacobiEllipticFunction.dn(data.t, data.k), data.dn));
		}
		
		for (double t = -10.0; t < 10.0; t += 0.3) {
			assertTrue(nearlyEqual(JacobiEllipticFunction.sn(t, 0), Math.sin(t)));
			assertTrue(nearlyEqual(JacobiEllipticFunction.cn(t, 0), Math.cos(t)));
			assertTrue(nearlyEqual(JacobiEllipticFunction.dn(t, 0), 1.0));
			
			assertTrue(nearlyEqual(JacobiEllipticFunction.sn(t, 1.0), Math.tanh(t)));
			assertTrue(nearlyEqual(JacobiEllipticFunction.cn(t, 1.0), 1.0 / Math.cosh(t)));
			assertTrue(nearlyEqual(JacobiEllipticFunction.dn(t, 1.0), 1.0 / Math.cosh(t)));
		}
	}

	@Test public void fomulaTest() {
		double[] karray = { 0.0, 0.2, 0.9, 1.0, 2.0};
		
		for (double k : karray)
			for (double t = -200.0; t < 200.0; t += 1.11) {
				assertTrue(nearlyEqual(JacobiEllipticFunction.sn(-t, k), -JacobiEllipticFunction.sn(t, k)));
				assertTrue(nearlyEqual(JacobiEllipticFunction.cn(-t, k), JacobiEllipticFunction.cn(t, k)));
				assertTrue(nearlyEqual(1.0, JacobiEllipticFunction.sn(t, k) * JacobiEllipticFunction.sn(t, k) + JacobiEllipticFunction.cn(t, k) * JacobiEllipticFunction.cn(t, k)));
				
				if (k <= 1.0)
					assertTrue(nearlyEqual(1.0, k * k * JacobiEllipticFunction.sn(t, k) * JacobiEllipticFunction.sn(t, k) + JacobiEllipticFunction.dn(t, k) * JacobiEllipticFunction.dn(t, k)));
			}	
	}
}
