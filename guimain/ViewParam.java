package guimain;

import java.io.Serializable;

public class ViewParam implements Serializable {
	private static final long serialVersionUID = 1L;

	private boolean showBifurcationDiagram;
	private boolean showConfigurationSpace;
	private boolean showPhaseSpace;
	
	private String sleepTime;
	private String taskPerFrame;
	private String timeInterval;
	
	public ViewParam(boolean showBif, boolean showConf, boolean showPhase, String sleep, String task, String TimeInterval) {
		this.showBifurcationDiagram = showBif;
		this.showConfigurationSpace = showConf;
		this.showPhaseSpace = showPhase;
		this.sleepTime = sleep;
		this.taskPerFrame = task;
		this.timeInterval = TimeInterval;
	}
	
	public boolean showBirfurcationDiagram() {
		return showBifurcationDiagram;
	}
	public boolean showConfigurationSpace() {
		return showConfigurationSpace;
	}
	public boolean showPhaseSpace() {
		return showPhaseSpace;
	}
	public String getSleepTime() {
		return sleepTime;
	}
	public String getTaskPerFrame() {
		return taskPerFrame;
	}
	public String getTimeInterval() {
		return timeInterval;
	}
}
