package pendulum;


import java.awt.GraphicsConfiguration;

import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Node;
import javax.media.j3d.PointLight;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;

import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.universe.SimpleUniverse;

public class DefaultViewer3d implements Viewer3d {
	private Canvas3D canvas;
	private BranchGroup scene;
	private BranchGroup root;
	private SimpleUniverse universe;
	public DefaultViewer3d() {
		GraphicsConfiguration config =
			SimpleUniverse.getPreferredConfiguration();
		canvas = new Canvas3D(config);
		universe = new SimpleUniverse(canvas);
		universe.getViewingPlatform().setNominalViewingTransform();
		OrbitBehavior orbit = new OrbitBehavior(canvas, OrbitBehavior.REVERSE_ALL);
		orbit.setSchedulingBounds(
				new BoundingSphere(new Point3d(0, 0, 0), 100.0));
		universe.getViewingPlatform().setViewPlatformBehavior(orbit);
		
		initRoot();
		
		scene = new BranchGroup();
		scene.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		scene.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		scene.addChild(root);
		
		PointLight light1 = new PointLight(true,
                new Color3f(1.0f, 1.0f, 1.0f),
                new Point3f(2.0f, 2.0f, 2.0f),
                new Point3f(0.8f, 0.0f, 0.0f));
		PointLight light2 = new PointLight(true,
                new Color3f(1.0f, 1.0f, 1.0f),
                new Point3f(-2.0f, -2.0f, -2.0f),
                new Point3f(0.8f, 0.0f, 0.0f));
		light1.setInfluencingBounds(new BoundingSphere(new Point3d(), 100.0));
		light2.setInfluencingBounds(new BoundingSphere(new Point3d(), 100.0));
		scene.addChild(light1);
		scene.addChild(light2);
		
		universe.addBranchGraph(scene);
	}
	private void initRoot() {
		root = new BranchGroup();
		root.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		root.setCapability(BranchGroup.ALLOW_DETACH);
		root.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
	}

	@Override
	public void add(Node node) {
		if (node == null)
			return;
		BranchGroup b = new BranchGroup();
		b.addChild(node);
		root.addChild(b);
	}

	@Override
	public void addBranchGroup(BranchGroup group) {
		if (group == null)
			return;
		root.addChild(group);
	}
	
	@Override
	public void clear() {
		root.detach();
		initRoot();
		scene.addChild(root);
	}

	@Override
	public Canvas3D getComponent() {
		return canvas;
	}
}
