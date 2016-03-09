package guimain;

import javax.vecmath.Color3f;

import pendulum.EmParam;

public class ScheduleElement {
	private EmParam emParam;
	private Color3f color;
	private int frame;
	
	public ScheduleElement(EmParam emParam, Color3f color, int frame) {
		this.emParam = emParam;
		this.color = color;
		this.frame = frame;
	}
	
	public EmParam getEmParam() {
		return emParam;
	}
	public Color3f getColor() {
		return color;
	}
	public int getFrame() {
		return frame;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(emParam.toString());
		builder.append(" color:");
		builder.append(color.toString());
		builder.append(" frame:");
		builder.append(frame);
		return builder.toString();
	}
}
