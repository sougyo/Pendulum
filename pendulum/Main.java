package pendulum;
import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JFrame;
import javax.vecmath.Color3f;


public class Main {
	public static void makeFrame(Component component, String title, int x, int y, int size) {
		JFrame frame = new JFrame(title);
		frame.setLayout(new BorderLayout());
		frame.getContentPane().add(component);
		frame.setLocation(x, y);
		frame.setSize(size, size);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	private static ConfigurationSpace configurationSpace;
	private static PhaseSpace phaseSpace;
	private static Viewer3d configurationSpaceViewer;
	private static Viewer3d phaseSpaceViewer;
	private static Viewer3d hogeViewer;
	
	public static void main(String[] args) {
		SpParam spParam = new SpParam(1.0, 1.0, 1.0);
		
			
		configurationSpaceViewer = new DefaultViewer3d();
		phaseSpaceViewer = new DefaultViewer3d();
		hogeViewer = new DefaultViewer3d();
		makeFrame(configurationSpaceViewer.getComponent(), "Motion of a Spherical Pendulum", 50, 100, 500);
		makeFrame(phaseSpaceViewer.getComponent(), "Phase space", 500, 100, 500);
		makeFrame(hogeViewer.getComponent(), "hoge", 500, 400, 200);
		
		configurationSpace = new ConfigurationSpace(spParam);
		phaseSpace = new PhaseSpace(null);
		
		configurationSpaceViewer.add(configurationSpace.getNode());
		phaseSpaceViewer.add(phaseSpace.getNode());
		
		Color3f color = new Color3f(1.0f, 1.0f, 1.0f);
		
		EmParam emParam;
		/*emParam = new EmParam(0.9, 0.03, 0, 0);
		draw(spParam, emParam, color, 10000);
		emParam = new EmParam(0.9, -0.03, 0, 0);
		draw(spParam, emParam, color, 10000);
		emParam = new EmParam(0.9, 1, 0, 0);
		draw(spParam, emParam, color, 10000);
		emParam = new EmParam(0.9, -1, 0, 0);
		draw(spParam, emParam, color, 10000);
		*/
		
		for (double theta = 0; theta < Math.PI; theta += 0.1) {
			emParam = new EmParam(0.9, 0, -5, theta);
			
			color = new Color3f(1.0f, (float)theta / (float)Math.PI, 0);
			draw(spParam, emParam, color, 600);
		}
		color = new Color3f(1.0f, 1.0f, 1.0f);
		emParam = new EmParam(0.9, 0.03, 0, 0);
		draw(spParam, emParam, color, 10000);
		emParam = new EmParam(0.9, -0.03, 0, 0);
		draw(spParam, emParam, color, 10000);
		
	}
	
	
	
	private static void draw(SpParam spParam, EmParam emParam, Color3f color, int step) {
		double timeInterval = 0.03;
		SpSolution solution = 
			EnergyMomentumSpSolutionFactory.create(spParam, emParam, timeInterval);
	
		if (solution == null) {
			System.out.println("There are no solutions");
			return;
		}
		
		
		for(int i = 0; i < step; i++) {
			SpCoordinate coordinate = solution.next();
			configurationSpace.setCoordinate(coordinate);
			
			if (emParam.getL() == 0 && coordinate.getDotZ() > 0)
				phaseSpace.setCoordinate(coordinate, new Color3f(0.0f, 0.0f, 1.0f));
			else
				phaseSpace.setCoordinate(coordinate, color);
			//phaseSpace.setCoordinate(coordinate, color);
			
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}