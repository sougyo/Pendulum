package pendulum;


import javax.vecmath.Color3f;
import javax.vecmath.Point3d;

public class PointColor {
	private Point3d point;
	private Color3f color;
	
	public PointColor(Point3d point, Color3f color) {
		this.point = point;
		this.color = color;
	}
	
	public Point3d getPoint() {
		return point;
	}
	
	public Color3f getColor() {
		return color;
	}
}
