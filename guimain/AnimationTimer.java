package guimain;

import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.WakeupOnElapsedTime;
import javax.vecmath.Point3d;

class AnimationTimer extends Behavior {
	protected WakeupOnElapsedTime wup = null;
	private int taskPerFrame;
	private AnimationTimerTask task;
	public AnimationTimer(int sleep, int taskPerFrame, AnimationTimerTask task) {
		super();

		this.taskPerFrame = taskPerFrame;
		this.task = task;
		
		wup = new WakeupOnElapsedTime(sleep);
		setSchedulingBounds(new BoundingSphere(new Point3d(), 100.0));
	}

	@Override
	public void initialize() {
		wakeupOn(wup);
	}
	
	@SuppressWarnings({"rawtypes"})
	@Override
	public void processStimulus(Enumeration arg0) {
		for (int i = 0; i < taskPerFrame; i++)
			task.run();
		if (task.isActive())
			wakeupOn(wup);
	}
}