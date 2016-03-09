package guimain;

import java.awt.Component;

import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import pendulum.BifurcationDiagramPanel;
import pendulum.ConfigurationSpace;
import pendulum.DefaultViewer3d;
import pendulum.EnergyMomentumSpSolutionFactory;
import pendulum.PhaseSpace;
import pendulum.SpCoordinate;
import pendulum.SpCoordinateReceiver;
import pendulum.SpSolution;
import pendulum.Viewer3d;
import pendulum.Viewer3dImageWriter;

class PendulumAnimationTimerTask implements AnimationTimerTask {
	private PlayParam param;
	private int frame;
	private int scheduleIndex;
	private SpSolution spSolution;
	private ArrayList<SpCoordinateReceiver> receivers;
	private boolean isActive;
	private ScheduleElement scheduleElement;
	public PendulumAnimationTimerTask(PlayParam param, ArrayList<SpCoordinateReceiver> receivers) {
		this.param = param;
		
		if (param.getSchedule().size() == 0) {
			isActive = false;
			return;
		}
		
		this.receivers = receivers;
		this.frame = 0;
		this.scheduleIndex = 0;
		this.frame = 0;
		this.isActive = true;
	}
	@Override
	public void run() {
		if (scheduleIndex >= param.getSchedule().size()) {
			isActive = false;
			return;
		}
		
		if (frame == 0) {
			scheduleElement = param.getSchedule().get(scheduleIndex);
			spSolution = EnergyMomentumSpSolutionFactory.create(param.getSpParam(), scheduleElement.getEmParam(), param.getTimeInterval());
			for (SpCoordinateReceiver receiver : receivers)
				receiver.clear();
		}
		
		if (spSolution != null) {
			SpCoordinate coordinate = spSolution.next();
			for (SpCoordinateReceiver receiver : receivers)
				receiver.setCoordinate(coordinate, scheduleElement.getColor());
		} else
			JOptionPane.showMessageDialog(null, "There are no solutions: [" + scheduleElement.getEmParam().toString() + "]");
		
		frame++;
		
		if (spSolution == null || frame > scheduleElement.getFrame()) {
			frame = 0;
			scheduleIndex++;
		}
	}
	@Override
	public boolean isActive() {
		return isActive;
	}
}

class PendulumMovieAnimationTimerTask extends PendulumAnimationTimerTask {
	private ArrayList<Viewer3dImageWriter> writers;
	
	public PendulumMovieAnimationTimerTask(PlayParam param,
			ArrayList<SpCoordinateReceiver> receivers,
			ArrayList<Viewer3dImageWriter> writers) {
		super(param, receivers);
		this.writers = writers;
	}
	
	@Override
	public void run() {
		super.run();
		for (Viewer3dImageWriter writer : writers)
			writer.write();
	}
}

public class PendulumPlayer {
	private PlayParam param;
	public PendulumPlayer(PlayParam param) {
		if (param == null)
			return;
		if (param.getSchedule().size() == 0)
			return;
		this.param = param;
		
		if (param.showBifurcationDiagram())
			makeBifurcationDiagramFrame();
		
		
		ArrayList<SpCoordinateReceiver> receivers = new ArrayList<SpCoordinateReceiver>();
		ArrayList<Viewer3d> viewers = new ArrayList<Viewer3d>();
		
		if (param.showConfigurationSpace()) {
			ConfigurationSpace configurationSpace = new ConfigurationSpace(param.getSpParam());
			Viewer3d configurationSpaceViewer = new DefaultViewer3d();
			configurationSpaceViewer.add(configurationSpace.getNode());
			
			receivers.add(configurationSpace);
			viewers.add(configurationSpaceViewer);
			
			makeFrame(configurationSpaceViewer.getComponent(), "Configuration Space", 100, 50, 400, 400).setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		}
		
		if (param.showPhaseSpace()) {
			PhaseSpace phaseSpace = new PhaseSpace(param.getEulerAngle());
			Viewer3d phaseSpaceViewer = new DefaultViewer3d();
			phaseSpaceViewer.add(phaseSpace.getNode());
			
			receivers.add(phaseSpace);
			viewers.add(phaseSpaceViewer);
			
			makeFrame(phaseSpaceViewer.getComponent(), "Phase Space", 300, 50, 400, 400).setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		}
		
		if (viewers.size() == 0 || viewers.size() > 2)
			return;
		
		
		AnimationTimer timer = new AnimationTimer(param.getSleepTime(), param.getTaskPerFrame(), new PendulumAnimationTimerTask(param, receivers));
		viewers.get(0).add(timer);
	}
	
	private JFrame makeFrame(Component component, String title, int x, int y, int width, int height) {
		JFrame frame = new JFrame(title);
		frame.getContentPane().add(component);
		frame.setLocation(x, y);
		frame.setSize(width, height);
		frame.setVisible(true);
		return frame;
	}
	
	private void makeBifurcationDiagramFrame() {
		BifurcationDiagramPanel panel = new BifurcationDiagramPanel(param.getSpParam());
		Schedule schedule = param.getSchedule();
		
		for (int i = 0; i < schedule.size(); i++) {
			ScheduleElement element = schedule.get(i);
			double h = element.getEmParam().getH();
			double l = element.getEmParam().getL();
			panel.addPoint(h, l, element.getColor().get());
		}
		
		makeFrame(panel, "BifurcationDiagram(Energy-Momentum)", 500, 50, 400, 400);
	}
}
