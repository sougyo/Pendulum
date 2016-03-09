package pendulum;


import javax.media.j3d.BranchGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;

public interface LineManager {
	public void add(Point3d point, Color3f color);
	public void clear();
	public void resetPrevPoint();
	public BranchGroup getNode();
}