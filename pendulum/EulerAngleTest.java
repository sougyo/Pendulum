package pendulum;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class EulerAngleTest {
	@Test public void normalizeTest() {
		final double DELTA = 0.0001;
		final double PI = Math.PI;
		final double TPI = 2 * PI;
		
		EulerAngle ea1 = new EulerAngle(TPI + 0.1, PI + 0.2, TPI + 0.3);
		ea1.normalize();
		assertEquals(0.1, ea1.getPhi(), DELTA);
		assertEquals(0.2, ea1.getTheta(), DELTA);
		assertEquals(0.3, ea1.getPsi(), DELTA);
		
		EulerAngle ea2 = new EulerAngle(0, 0, 0);
		ea2.normalize();
		assertEquals(0, ea2.getPhi(), DELTA);
		assertEquals(0, ea2.getTheta(), DELTA);
		assertEquals(0, ea2.getPsi(), DELTA);
		
		EulerAngle ea3 = new EulerAngle(TPI + TPI + 0.1, PI + PI + 0.2, TPI + TPI + TPI + 0.3);
		ea3.normalize();
		assertEquals(0.1, ea3.getPhi(), DELTA);
		assertEquals(0.2, ea3.getTheta(), DELTA);
		assertEquals(0.3, ea3.getPsi(), DELTA);
		
		EulerAngle ea4 = new EulerAngle(0.1 - TPI * 11, 0.2 - PI * 7, 0.3 - TPI * 13);
		ea4.normalize();
		assertEquals(0.1, ea4.getPhi(), DELTA);
		assertEquals(0.2, ea4.getTheta(), DELTA);
		assertEquals(0.3, ea4.getPsi(), DELTA);
	}
}