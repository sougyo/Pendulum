package pendulum;


import java.util.ArrayList;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Node;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;


public class RefreshLineManager implements LineManager {
	private LineFactory factory = new LineFactory();
	private MutableNode node = new MutableNode();
	private MutableNode tmpNode = new MutableNode();
	private ArrayList<PointColor> list = new ArrayList<PointColor>();
	private int refreshStep;
	public RefreshLineManager(int refreshStep) {
		node.add(tmpNode.getNode());
		this.refreshStep = refreshStep;
	}
	
	@Override
	public void add(Point3d point, Color3f color) {
		if (point == null || color == null)
			return;
		
		list.add(new PointColor(point, color));
		if (list.size() == 1)
			return;
		
		Point3d prevPoint = list.get(list.size() - 2).getPoint();
		Color3f prevColor = list.get(list.size() - 2).getColor();
		
		if (list.size() > refreshStep || isTooLong(prevPoint.distance(point)))
			refresh();
		else {
			factory.clear();
			factory.add(prevPoint, prevColor);
			factory.add(point, color);

			tmpNode.add(cover(factory.makeLine()));
		}
	}

	private BranchGroup cover(Node node) {
		BranchGroup group = new BranchGroup();
		group.addChild(node);
		return group;
	}
	
	private boolean isTooLong(PointColor p1, PointColor p2) {
		return isTooLong(p1.getPoint().distance(p2.getPoint()));
	}
	
	private boolean isTooLong(double distance) {
		return distance > 0.3;
	}
	
	private void refresh() {
		if (list.isEmpty())
			return;
		
		factory.clear();
		for (int i = 0; i < list.size(); i++) {
			if (i != 0 && isTooLong(list.get(i - 1), list.get(i)))
				continue;
			factory.add(list.get(i).getPoint(), list.get(i).getColor());
		}
		if (factory.size() >= 2)
			node.add(cover(factory.makeLine()));
		tmpNode.clear();

		PointColor pointColor = list.get(list.size() - 1);
		list.clear();
		list.add(pointColor);
	}
	
	@Override
	public void clear() {
		list.clear();
		node.clear();
		tmpNode = new MutableNode();
		node.add(tmpNode.getNode());
	}

	@Override
	public BranchGroup getNode() {
		return node.getNode();
	}

	@Override
	public void resetPrevPoint() {
		refresh();
		list.clear();
	}
}
