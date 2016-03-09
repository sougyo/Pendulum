package pendulum;


import java.util.ArrayList;

import javax.media.j3d.GeometryArray;
import javax.media.j3d.LineStripArray;
import javax.media.j3d.Shape3D;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;

public class LineFactory {
	private static final Color3f DEFAULTCOLOR = new Color3f(1.0f, 1.0f, 1.0f);
	private ArrayList<Point3d> pointList = new ArrayList<Point3d>();
	private ArrayList<Color3f> colorList = new ArrayList<Color3f>();
	private Color3f color = DEFAULTCOLOR;
	public void clear() {
		pointList.clear();
		colorList.clear();
		color = DEFAULTCOLOR;
	}
	public void add(Point3d point) {
		pointList.add(point);
		colorList.add(color);
	}
	public void add(Point3d point, Color3f color) {
		pointList.add(point);
		colorList.add(color);
	}
	public void setColor(Color3f color) {
		if (color == null)
			return;
		this.color = color;
	}
	public Shape3D makeLine() {
		int[] a = { size() };
		LineStripArray lsa = new LineStripArray(size(), GeometryArray.COORDINATES | GeometryArray.COLOR_3, a);
		lsa.setCoordinates(0, (Point3d[])pointList.toArray(new Point3d[0]));
		lsa.setColors(0, (Color3f[])colorList.toArray(new Color3f[0]));
		return new Shape3D(lsa);
	}
	public int size() {
		return pointList.size();
	}
}
