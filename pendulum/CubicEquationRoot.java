package pendulum;
import java.util.Arrays;

/**
 * Objects of this class store the root of cubic equation.
 * @author shogom
 *
 */
public class CubicEquationRoot {
	private double[] real = new double[3];
	private double[] image = new double[3];
	private boolean isReal;
	private boolean hasMultipleRoot;
	
	/**
	 * constructor (this constructor is called when roots are not real number) 
	 * @param x1
	 * @param a
	 * @param b
	 */
	public CubicEquationRoot(double x1, double a, double b) {
		isReal = false;
		hasMultipleRoot = false;
		real[0] = x1; image[0] = 0;
		real[1] = a; image[1] = b;
		real[2] = a; image[2] = -b;
	}
	
	/**
	 * constructor (this constructor is called when all roots are real number)
	 * @param x1
	 * @param x2
	 * @param x3
	 * @param hasMultipleRoot
	 */
	public CubicEquationRoot(double x1, double x2, double x3, boolean hasMultipleRoot) {
		this.hasMultipleRoot = hasMultipleRoot;
		this.isReal = true;
		real[0] = x1; image[0] = 0;
		real[1] = x2; image[1] = 0;
		real[2] = x3; image[2] = 0;
		image[0] = image[1] = image[2] = 0;
		Arrays.sort(real);
	}
	
	public boolean hasMultipleRoot() {
		return hasMultipleRoot;
	}
	
	/**
	 * return whether all roots are real or not
	 * @return if all roots are real number, this method will return true
	 */
	public boolean isReal() {
		return isReal;
	}
	
	public double getReal1() {
		return real[0];
	}
	public double getReal2() {
		return real[1];
	}
	public double getReal3() {
		return real[2];
	}
	public double getImage1() {
		return image[0];
	}
	public double getImage2() {
		return image[1];
	}
	public double getImage3() {
		return image[2];
	}
	
}
