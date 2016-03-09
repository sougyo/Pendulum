package pendulum;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class BifurcationDiagramPanel extends JPanel implements MouseMotionListener {
	private static final long serialVersionUID = 1L;
	
	class HlPoint {
		private double h;
		private double l;
		private Color color;
		
		public HlPoint(double h, double l, Color color) {
			this.h = h;
			this.l = l;
			this.color = color;
		}
		public double getH() {
			return h;
		}
		public double getL() {
			return l;
		}
		public Color getColor() {
			return color;
		}
	}

	private SpParam spParam;
	private double hMin, hMax, lMin, lMax;
	private ArrayList<HlPoint> hlPointList = new ArrayList<HlPoint>();
	private String coordinateString = "";
	private HlPoint touchedPoint = null;
	
	public BifurcationDiagramPanel(SpParam spParam, double hMin, double hMax, double lMin, double lMax) {
		super();
		this.spParam = spParam;
		
		this.hMin = hMin;
		this.hMax = hMax;
		this.lMin = lMin;
		this.lMax = lMax;
		
		addMouseMotionListener(this);
	}
	
	public BifurcationDiagramPanel(SpParam spParam) {
		super();
		this.spParam = spParam;
		
		double amg = spParam.getA() * spParam.getM() * spParam.getG();
		
		this.hMin = - amg * 1.3;
		this.hMax = amg * 1.3;
		this.lMin = - calSingularL(hMax) * 1.3;
		this.lMax = calSingularL(hMax) * 1.3;
		
		addMouseMotionListener(this);
	}
	
	public void addPoint(double h, double l, Color color) {
		hlPointList.add(new HlPoint(h, l, color));
		
		if (h > hMax)
			hMax = h * 1.3;
		if (h < hMin)
			hMin = h * 1.3;
		if (Math.abs(l) > lMax) {
			lMin = -Math.abs(l) * 1.3;
			lMax = Math.abs(l) * 1.3;
		}
		
		repaint();
	}
	
	private int toPanelX(double h) {
		double width = getWidth();
		double x = (h - hMin) * width / (hMax - hMin);
		return (int)x;
	}
	
	private int toPanelY(double l) {
		double height = getHeight();
		double y = (l - lMin) * height / (lMax - lMin);
		return (int)(height - y);
	}
	
	private double toH(int x) {
		double h = (double)(x * (hMax - hMin)) / getWidth() + hMin;
		return h;
	}
	
	private double toL(int y) {
		double tmp = (getHeight() - y);
		double l = (double)((tmp * (lMax - lMin))) / getHeight() + lMin;
		return l;
	}
	
	private double calSingularL(double h) {
		double a = spParam.getA();
		double m = spParam.getM();
		double g = spParam.getG();
		
		double amg = a * m * g;
		double amg2 = amg * amg;
		double tmp = 3 * amg2 - h*h + h*Math.sqrt(h*h + 3*amg2);
		double tmp2 = h+Math.sqrt(h*h+3*amg2);
		double coefficient = 2 / (9 * a * g * g * m * Math.sqrt(m));
		
		return coefficient * tmp * Math.sqrt(tmp2);
	}
	
	private void drawSingularLine(Graphics g0) {
		Graphics2D g = (Graphics2D)g0;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
				RenderingHints.VALUE_ANTIALIAS_ON);

		double amg = spParam.getA() * spParam.getM() * spParam.getG();
		if (hMax < -amg)
			return;
		
		g.setColor(Color.BLACK);
		
		g.drawLine(toPanelX(0), 0, toPanelX(0), getHeight());
		g.drawLine(0, toPanelY(0), getWidth(), toPanelY(0));
		
		g.setStroke(new BasicStroke(2 ,BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
		
		double prevH = 0;
		double prevL = 0;
		final int DIV = 100;
		for (int i = 0; i <= DIV; i++) {
			double h = (hMax + amg) * i / DIV - amg;
			double l = calSingularL(h);
			
			if (i == 0) {
				prevH = h;
				prevL = l;
				continue;
			}
			
			g.drawLine(toPanelX(prevH), toPanelY(prevL), toPanelX(h), toPanelY(l));
			g.drawLine(toPanelX(prevH), toPanelY(-prevL), toPanelX(h), toPanelY(-l));
			final int pointSize = 6;
			g.fillOval(toPanelX(-amg) - pointSize / 2, toPanelY(0) -pointSize / 2, pointSize, pointSize);
			g.fillOval(toPanelX(amg) - pointSize / 2, toPanelY(0) - pointSize / 2, pointSize, pointSize);
			
			prevH = h;
			prevL = l;
		}
		
		g.setStroke(new BasicStroke(1 ,BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
		
		final int size = 10;
		for (HlPoint point : hlPointList) {
			g.setColor(point.getColor());
			g.fillOval(toPanelX(point.getH()) - size / 2, toPanelY(point.getL()) - size / 2, size, size);
			g.setColor(Color.BLACK);
			g.drawOval(toPanelX(point.getH()) - size / 2, toPanelY(point.getL()) - size / 2, size, size);
		}
	}
	
	private String valToString(double val){
		return String.format("%.1f", val);
	}
	
	public SpParam getSpParam() {
		return spParam;
	}
	
	public double getHMin() {
		return hMin;
	}
	public double getHMax() {
		return hMax;
	}
	public double getLMin() {
		return lMin;
	}
	public double getLMax() {
		return lMax;
	}
	
	private Color bgColor = Color.WHITE;
	
	@Override
	public void paintComponent(Graphics g) {
		g.setColor(bgColor);
		g.fillRect(0, 0, getWidth(), getHeight());

		drawSingularLine(g);
		
		g.setColor(Color.BLACK);
		g.drawString(coordinateString, 5, 15);
		
		if (touchedPoint != null) {
			double h = touchedPoint.getH();
			double l = touchedPoint.getL();
			String str = "(" + h + ", " + l + ")";
			g.drawString(str, toPanelX(h) - 22, toPanelY(l) - 10);
		}
	}
	
	public static void main(String[] args) {
		SpParam spParam = new SpParam(1.0, 1.0, 1.0);
		BifurcationDiagramPanel bifPanel = new BifurcationDiagramPanel(spParam, -1.5, 2, -1.5, 1.5);
		JFrame frame = new JFrame("Bifurcation Diagram(H, L)");
		frame.setSize(500, 500);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.getContentPane().add(bifPanel);
		frame.setVisible(true);
		
		frame.repaint();
		
		bifPanel.addPoint(1.0, -0.5, Color.RED);
		bifPanel.addPoint(1.0, 0, Color.pink);
		bifPanel.addPoint(1.0, 0.5, Color.blue);
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		
		coordinateString = "(h,l)=(" + valToString(toH(x)) + "," + valToString(toL(y)) + ")";
		
		for (HlPoint point : hlPointList) {
			double pointX = toPanelX(point.getH());
			double pointY = toPanelY(point.getL());
			
			if (Math.pow((pointX-x), 2) + Math.pow((pointY - y), 2) < 100) {
				touchedPoint = point;
				break;
			} else
				touchedPoint = null;
		}
		repaint();
	}
}