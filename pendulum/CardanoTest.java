package pendulum;
import java.util.Random;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class CardanoTest {
	private static final double DELTA = 0.0001;
	
	private Random random = new Random();
	
	@Test public void realTest() {
		CubicEquationRoot root;
		root = Cardano.solve(1, 3, -1, -3);
		assertEquals(-3, root.getReal1(), DELTA);
		assertEquals(-1, root.getReal2(), DELTA);
		assertEquals(1, root.getReal3(), DELTA);
		assertEquals(0, root.getImage1(), DELTA);
		assertEquals(0, root.getImage2(), DELTA);
		assertEquals(0, root.getImage3(), DELTA);
		assertFalse(root.hasMultipleRoot());
		assertTrue(root.isReal());
		
		root = Cardano.solve(2, 14, -36, 0);
		assertEquals(-9, root.getReal1(), DELTA);
		assertEquals(0, root.getReal2(), DELTA);
		assertEquals(2, root.getReal3(), DELTA);
		assertEquals(0, root.getImage1(), DELTA);
		assertEquals(0, root.getImage2(), DELTA);
		assertEquals(0, root.getImage3(), DELTA);
		assertFalse(root.hasMultipleRoot());
		assertTrue(root.isReal());
		
		root = Cardano.solve(1, -1, -1, 1);
		assertEquals(-1, root.getReal1(), DELTA);
		assertEquals(1, root.getReal2(), DELTA);
		assertEquals(1, root.getReal3(), DELTA);
		assertEquals(0, root.getImage1(), DELTA);
		assertEquals(0, root.getImage2(), DELTA);
		assertEquals(0, root.getImage3(), DELTA);
		
		assertTrue(root.hasMultipleRoot());
		assertTrue(root.isReal());
		
		root = Cardano.solve(1, -3, 3, -1);
		assertEquals(1, root.getReal1(), DELTA);
		assertEquals(1, root.getReal2(), DELTA);
		assertEquals(1, root.getReal3(), DELTA);
		assertEquals(0, root.getImage1(), DELTA);
		assertEquals(0, root.getImage2(), DELTA);
		assertEquals(0, root.getImage3(), DELTA);
		
		assertTrue(root.hasMultipleRoot());
		assertTrue(root.isReal());
	}
	
	@Test public void realTest2() {
		double scale = 30.0;
		int count = 0; 
		for (int i = 0; i < 20000; i++) {
			double a = 2 * scale * random.nextDouble() - scale;
			double b = 2 * scale * random.nextDouble() - scale;
			double c = 2 * scale * random.nextDouble() - scale;
			double d = 2 * scale * random.nextDouble() - scale;
			//System.out.println(a + "," + b + "," + c + "," + d);
			CubicEquationRoot root = Cardano.solve(a, b, c, d);
			if (root.isReal()) {
				count++;
				double actual1 = evaluate(a, b, c, d, root.getReal1());
				double actual2 = evaluate(a, b, c, d, root.getReal2());
				double actual3 = evaluate(a, b, c, d, root.getReal3());
				assertEquals(0, actual1, DELTA);
				assertEquals(0, actual2, DELTA);
				assertEquals(0, actual3, DELTA);
			} else {
				assertTrue(root.getImage1() == 0.0);
				assertTrue(root.getImage2() != 0.0);
				assertTrue(root.getImage3() != 0.0);
			}
		}
		System.out.println(count + " cases have been tested at realTest2() method");
	}
	
	private double evaluate(double a, double  b, double c, double d, double x) {
		return a*x*x*x + b*x*x + c*x + d;
	}
}
