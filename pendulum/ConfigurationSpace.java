package pendulum;
import javax.media.j3d.BranchGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;

class OrbitColorTable {
	private static final Color3f DEFAULTCOLOR = new Color3f(1.0f, 1.0f, 1.0f);
	private Color3f[] colors;
	public OrbitColorTable(int div) {
		colors = new Color3f[div];
		for (int i = 0; i < colors.length; i++) {
			float c = (float)i / (div - 1);
			colors[i] = new Color3f(c*0.8f, 1-c*0.5f, 1.0f);
		}
	}
	public Color3f getColor(double z) {
		if (z < -1 || z > 1)
			return DEFAULTCOLOR;
		int index = (int)((z + 1) * (colors.length - 1) / 2);
		return colors[index];
	}
}

public class ConfigurationSpace implements SpCoordinateReceiver {
	private BranchGroup root;
	private Pendulum pendulum;
	private LineManager manager;
	private LineManager floorManager;
	private LineManager sideManager;
	private OrbitColorTable colorTable = new OrbitColorTable(20);
	Color3f color = new Color3f(1.0f, 1.0f, 1.0f);
	private int count;
	
	public ConfigurationSpace(SpParam spParam) {
		root = new BranchGroup();
		
		pendulum = new Pendulum(spParam.getA());
		manager = new RefreshLineManager(100);
		floorManager = new RefreshLineManager(100);
		sideManager = new RefreshLineManager(100);
		
		root.addChild(pendulum.getPendulum());
		root.addChild(manager.getNode());
		root.addChild(floorManager.getNode());
		root.addChild(sideManager.getNode());
		root.addChild(makeFloorMesh(-1.5));
		root.addChild(makeSideMesh(-1.5));
		root.addChild(new TransparentSphere(0.5f, 1.0f, 50).getSphere());
		
		count = 0;
	}
	
	@Override
	public void clear() {
		manager.clear();
		floorManager.clear();
		sideManager.clear();
		count = 0;
	}
	
	@Override
	public void setCoordinate(SpCoordinate coordinate) {
		if (count >= 1000) {
			manager.clear();
			floorManager.clear();
			sideManager.clear();
			count = 0;
		}
		double x = coordinate.getX();
		double y = coordinate.getY();
		double z = coordinate.getZ();
		pendulum.setPosition(x, y, z);
		Color3f color = colorTable.getColor(z);
		manager.add(new Point3d(x, y, z), color);
		floorManager.add(new Point3d(x, y, -1.5), color);
		sideManager.add(new Point3d(x, -1.5, z), color);
		
		count++;
	}
	
	@Override
	public void setCoordinate(SpCoordinate coordinate, Color3f color) {
		setCoordinate(coordinate);
	}
	
	public BranchGroup getNode() {
		return root;
	}
	
	
	private BranchGroup makeSideMesh(double y) {
		BranchGroup group = new BranchGroup();
		LineFactory factory = new LineFactory();
		for (double t = -1.0; t < 1 + 0.1; t += 0.2) {
			factory.clear();
			factory.add(new Point3d(t, y, -1));
			factory.add(new Point3d(t, y, 1));
			group.addChild(factory.makeLine());
			
			factory.clear();
			factory.add(new Point3d(-1, y, t));
			factory.add(new Point3d(1, y, t));
			group.addChild(factory.makeLine());
		}
		return group;
	}
	private BranchGroup makeFloorMesh(double z) {
		BranchGroup group = new BranchGroup();
		LineFactory factory = new LineFactory();
		for (double t = -1.0; t < 1 + 0.1; t += 0.2) {
			factory.clear();
			factory.add(new Point3d(t, -1, z));
			factory.add(new Point3d(t, 1, z));
			group.addChild(factory.makeLine());
			
			factory.clear();
			factory.add(new Point3d(-1, t, z));
			factory.add(new Point3d(1, t, z));
			group.addChild(factory.makeLine());
		}
		return group;
	}
}
