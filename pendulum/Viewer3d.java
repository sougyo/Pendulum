package pendulum;


import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Node;

public interface Viewer3d {
	public void add(Node node);
	public void addBranchGroup(BranchGroup group);
	public void clear();
	public Canvas3D getComponent();
}
