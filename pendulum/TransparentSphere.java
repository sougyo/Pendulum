package pendulum;


import javax.media.j3d.Appearance;
import javax.media.j3d.TransparencyAttributes;

import com.sun.j3d.utils.geometry.Sphere;

public class TransparentSphere {
	private Sphere sphere;
	public TransparentSphere(float transparency, float size, int div) {
		Appearance ap = new Appearance();
		TransparencyAttributes ta = new TransparencyAttributes();
		ta.setTransparency(transparency);
		ta.setTransparencyMode(TransparencyAttributes.FASTEST);
		ap.setTransparencyAttributes(ta);
		sphere = new Sphere(size, Sphere.GENERATE_NORMALS, div, ap);
	}
	public Sphere getSphere() {
		return sphere;
	}
}
