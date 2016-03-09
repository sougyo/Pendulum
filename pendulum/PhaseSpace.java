package pendulum;
import javax.media.j3d.BranchGroup;

import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point4d;

public class PhaseSpace implements SpCoordinateReceiver {
	private BranchGroup root;
	private Rp3Projector projector;
	private LineManager manager;
	private Color3f color = new Color3f(1.0f, 1.0f, 1.0f);
	
	public PhaseSpace(EulerAngle bias) {
		projector = new Rp3Projector(bias);
		manager = new RefreshLineManager(100);
		root = new BranchGroup();
		root.addChild(manager.getNode());
		root.addChild(new TransparentSphere(0.6f, 1.0f, 50).getSphere());
	}
	
	@Override
	public void clear() {
		manager.resetPrevPoint();
	}
	
	@Override
	public void setCoordinate(SpCoordinate coordinate) {
		setCoordinate(coordinate, color);
	}
	
	@Override
	public void setCoordinate(SpCoordinate coordinate, Color3f color) {
		Point3d point = projector.toPoint3d(coordinate);
		manager.add(point, color);
	}
	
	public BranchGroup getNode() {
		return root;
	}
}

class Rp3Projector {
	private static final Point3d ORIGIN = new Point3d();
	
	private Point3d center;
	private EulerAngle bias;
	public Rp3Projector(EulerAngle bias) {
		center = ORIGIN;
		this.bias = bias;
	}
	public Rp3Projector(Point3d point) {
		this.center = point;
	}
	
	private Point3d outerProduct(Point3d a, Point3d b) {
		Point3d result = new Point3d();
		result.setX(a.getY() * b.getZ() - a.getZ() * b.getY());
		result.setY(a.getZ() * b.getX() - a.getX() * b.getZ());
		result.setZ(a.getX() * b.getY() - a.getY() * b.getX());
		
		return result;
	}
	
	private Point3d stereographiProjection(Point4d point) {
		double sgn = 1.0;
		if (point.getW() > 0)
			sgn = -1.0;
		double x = -sgn * point.getX() / (1.0 - point.getW() * sgn);
		double y = sgn * point.getY() / (1.0 - point.getW() * sgn);
		double z = sgn * point.getZ() / (1.0 - point.getW() * sgn);
		return new Point3d(x, y, z);
	}
	
	private void translate(Point3d point) {
		point.setX(point.getX() + center.getX());
		point.setY(point.getY() + center.getY());
		point.setZ(point.getZ() + center.getZ());
	}

	public Point3d toPoint3d(SpCoordinate coordinate) {
		double x = coordinate.getX();
		double y = coordinate.getY();
		double z = coordinate.getZ();
		double xd = coordinate.getDotX();
		double yd = coordinate.getDotY();
		double zd = coordinate.getDotZ();
		Point3d e1 = new Point3d(x, y, z);
		Point3d e2 = new Point3d(xd, yd, zd);
		double norm = e2.distance(ORIGIN);
		e2.setX(e2.getX() / norm);
		e2.setY(e2.getY() / norm);
		e2.setZ(e2.getZ() / norm);
		Point3d e3 = outerProduct(e1, e2);
		
		double theta = Math.acos(e3.getZ());
		double psi, phi;

		if (theta == 0) {
			phi = Math.atan2(e1.getY(), e1.getX());
			psi = 0;
		} else if (theta == Math.PI) {
			phi = Math.atan2(-e1.getY(), e1.getX());
			psi = 0;
		} else {
			phi = Math.atan2(e3.getY(), e3.getX());
			psi = Math.atan2(-e2.getZ(), e1.getZ());
		}
		
		if (bias != null) {
			EulerAngle angle = new EulerAngle(phi + bias.getPhi(), theta + bias.getTheta(), psi + bias.getPsi());
			angle.normalize();
			phi = angle.getPhi();
			theta = angle.getTheta();
			psi = angle.getPsi();
		}
		
		double a1 = Math.cos(theta / 2.0) * Math.cos((phi + psi) / 2.0);
		double a2 = Math.cos(theta / 2.0) * Math.sin((phi + psi) / 2.0);
		double a3 = Math.sin(theta / 2.0) * Math.cos((psi - phi) / 2.0);
		double a4 = Math.sin(theta / 2.0) * Math.sin((psi - phi) / 2.0);
		
		Point3d point = stereographiProjection(new Point4d(a1, a2, a3, a4));
		translate(point);
		return point;
	}
}
