package pendulum;
import javax.media.j3d.Node;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import com.sun.j3d.utils.geometry.Sphere;

public class Pendulum {
	private Node node;
	private Transform3D rotationY;
	private Transform3D rotationZ;
	private TransformGroup rotationY_transformgroup;
	private TransformGroup rotationZ_transformgroup;
	//private double radius;
	
	public Pendulum(double radius) {
		//this.radius = radius;
		rotationY_transformgroup = new TransformGroup();
		rotationZ_transformgroup = new TransformGroup();
		rotationY_transformgroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		rotationZ_transformgroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		rotationY = new Transform3D();
		rotationZ = new Transform3D();
		rotationY.rotY(0);
		rotationZ.rotZ(0);
		rotationY_transformgroup.setTransform(rotationY);
		rotationZ_transformgroup.setTransform(rotationZ);
		
		rotationY_transformgroup.addChild(makeRod());
		rotationY_transformgroup.addChild(makeSphere());
		
		rotationZ_transformgroup.addChild(rotationY_transformgroup);
		
		node = rotationZ_transformgroup;
	}
	
	public void setPosition(double x, double y, double z) {
		rotationY.rotY(Math.acos(z));
		rotationZ.rotZ(Math.atan2(y, x));
		rotationY_transformgroup.setTransform(rotationY);
		rotationZ_transformgroup.setTransform(rotationZ);
	}
	
	public Node getPendulum() {
		return node;
	}
	
	private Shape3D makeRod() {
		LineFactory factory = new LineFactory();
		factory.add(new Point3d(0, 0, 0));
		factory.add(new Point3d(0, 0, 1));
		return factory.makeLine();
	}
	
	private Node makeSphere() {
		Vector3d spherePos = new Vector3d(0.0, 0.0, 1.0);
		TransformGroup transformgroup = new TransformGroup();
		transformgroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		Transform3D translation = new Transform3D();
		translation.set(spherePos);
		transformgroup.setTransform(translation);
		transformgroup.addChild(new Sphere(0.07f, Sphere.GENERATE_NORMALS, 50));
		return transformgroup;
	}
}
