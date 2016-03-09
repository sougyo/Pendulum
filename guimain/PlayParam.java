package guimain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.vecmath.Color3f;

import pendulum.EmParam;
import pendulum.EulerAngle;
import pendulum.SpParam;

class PlayParam {
	private SpParam spParam;
	
	private int sleepTime;
	private int taskPerFrame;
	private double timeInterval;
	private ViewParam viewParam;
	private ScheduleParam scheduleParam;
	private Schedule schedule;
	
	public PlayParam(ListElement paramSrc) throws PlayParamConstructException {
		AmgParam amgParam = paramSrc.getAmgParam();
		
		spParam = constructAmgParam(amgParam);
		viewParam = paramSrc.getViewParam();
		scheduleParam = paramSrc.getScheduleParam();
		
		schedule = constructScheduleParam();
		constructViewParam();
	}
	
	private SpParam constructAmgParam(AmgParam amgParam) throws PlayParamConstructException {
		double a, m, g;
		try {
			a = Double.valueOf(amgParam.getA());
			m = Double.valueOf(amgParam.getM());
			g = Double.valueOf(amgParam.getG());
		} catch(NumberFormatException e) {
			throw new PlayParamConstructException("spparam");
		}
		
		if (a <= 0 || m <= 0 || g <= 0)
			throw new PlayParamConstructException("spparam");
		
		return new SpParam(a, m, g);
	}
	
	private Schedule constructScheduleParam() throws PlayParamConstructException {
		if (scheduleParam.getFieldFlag())
			return constructFieldInfo(scheduleParam.getFieldInfo());
		
		return constructAreaInfo(scheduleParam.getAreaInfo());
	}
	
	private Schedule constructFieldInfo(ScheduleParam.FieldInfo fieldInfo) throws PlayParamConstructException {
		double h, l, delta, phi0;
		Color3f color;
		int frame;
		try {
			h = Double.valueOf(fieldInfo.getH());
			l = Double.valueOf(fieldInfo.getL());
			delta = Double.valueOf(fieldInfo.getDelta());
			phi0 = Double.valueOf(fieldInfo.getPhi0()) * Math.PI / 180.0;
			
			color = constructColor(fieldInfo.getColor());
			
			frame = Integer.valueOf(fieldInfo.getFrame());
		} catch (NumberFormatException e) {
			throw new PlayParamConstructException("field info");
		}
		Schedule schedule = new Schedule();
		ScheduleElement element = 
			new ScheduleElement(new EmParam(h, l, delta, phi0), color, frame);
		schedule.add(element);
		return schedule;
	}
	
	private static final String POSITIVEINTEGERREGEX = "\\d+";
	private static final String POSITIVEDOUBLEREGEX = "\\d+\\.?\\d*";
	private static final String DOUBLEREGEX = "-?" + POSITIVEDOUBLEREGEX;
	private static final String COLORREGEX = "\\(\\s*" + 
											POSITIVEDOUBLEREGEX + "\\s*,\\s*" +
											POSITIVEDOUBLEREGEX + "\\s*,\\s*" +
											POSITIVEDOUBLEREGEX + "\\s*\\)";
	private Color3f constructColor(String colorStr) throws PlayParamConstructException {
		float r = 0, g = 0, b = 0;
		if (colorStr.matches(COLORREGEX)) {
			Pattern p = Pattern.compile(POSITIVEDOUBLEREGEX);
			Matcher m = p.matcher(colorStr);
			
			m.find();
				r = Float.valueOf(colorStr.substring(m.start(), m.end()));
			m.find();
				g = Float.valueOf(colorStr.substring(m.start(), m.end()));
			m.find();
				b = Float.valueOf(colorStr.substring(m.start(), m.end()));
		} else
			throw new PlayParamConstructException("color:" + colorStr);

		if (isColorOutOfRange(r) || isColorOutOfRange(g) || isColorOutOfRange(b))
			throw new PlayParamConstructException("color2");
		return new Color3f(r, g, b);
	}
	
	private boolean isColorOutOfRange(float val) {
		return val < 0 || val > 1.0f;
	}
	
	private Schedule constructAreaInfo(ScheduleParam.AreaInfo areaInfo) throws PlayParamConstructException {
		Schedule schedule = new Schedule(areaInfo.isSimultaneous());
		String text = areaInfo.getAreaText();
		BufferedReader bf = new BufferedReader(new StringReader(text));
		String line;
		try {
			while ((line = bf.readLine()) != null) {
				if (line.matches("\\s*"))
					continue;
				schedule.add(constructLineInArea(line));
			}
		} catch (IOException e) {
			new PlayParamConstructException("area string io");
		}
		return schedule;
	}
	
	private static final String LINEREGEX = DOUBLEREGEX + " " +
														DOUBLEREGEX + " " +
														DOUBLEREGEX + " " +
														DOUBLEREGEX + " " +
														COLORREGEX + " " +
														POSITIVEINTEGERREGEX;
	
	private ScheduleElement constructLineInArea(String line) throws PlayParamConstructException {
		double h, l, delta, phi0;
		Color3f color;
		int frame;
		if (line.matches(LINEREGEX)) {
			StringTokenizer st = new StringTokenizer(line, " ");
			h = Double.valueOf(st.nextToken());
			l = Double.valueOf(st.nextToken());
			delta = Double.valueOf(st.nextToken());
			phi0 = Double.valueOf(st.nextToken()) * Math.PI / 180.0;
			color = constructColor(st.nextToken());
			frame = Integer.valueOf(st.nextToken());
		} else
			throw new PlayParamConstructException("line in area:" + line);
		return new ScheduleElement(new EmParam(h, l, delta, phi0), color, frame);
	}
	
	private void constructViewParam() throws PlayParamConstructException {
		try {
			sleepTime = Integer.valueOf(viewParam.getSleepTime());
			taskPerFrame = Integer.valueOf(viewParam.getTaskPerFrame());
			timeInterval = Double.valueOf(viewParam.getTimeInterval());
		} catch (NumberFormatException e) {
			throw new PlayParamConstructException("view param");
		}
		if (sleepTime <= 0 || taskPerFrame <= 0 || timeInterval <= 0)
			throw new PlayParamConstructException("view param");
	}
	
	public SpParam getSpParam() {
		return spParam;
	}
	
	public EulerAngle getEulerAngle() {
		return scheduleParam.getEulerAngle();
	}
	
	public boolean showBifurcationDiagram() {
		return viewParam.showBirfurcationDiagram();
	}
	
	public boolean showConfigurationSpace() {
		return viewParam.showConfigurationSpace();
	}
	
	public boolean showPhaseSpace() {
		return viewParam.showPhaseSpace();
	}
	public boolean isFieldSelected() {
		return scheduleParam.getFieldFlag();
	}
	public boolean isAreaSelected() {
		return scheduleParam.getAreaFlag();
	}
	public Schedule getSchedule() {
		return schedule;
	}
	public int getSleepTime() {
		return sleepTime;
	}
	public int getTaskPerFrame() {
		return taskPerFrame;
	}
	public double getTimeInterval() {
		return timeInterval;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Spherical Pendulum Param: ");
		builder.append(getSpParam());
		builder.append("\n");
		builder.append("Schedule:\n");
		builder.append(getSchedule());
		builder.append("isSimultaneous: ");
		builder.append(getSchedule().isSimultaneous());
		builder.append("\n");
		builder.append("Euler angle: ");
		builder.append(getEulerAngle());
		builder.append("\n");
		builder.append("sleepTime: ");
		builder.append(getSleepTime());
		builder.append("\n");
		builder.append("taskPerFrame: ");
		builder.append(getTaskPerFrame());
		builder.append("\n");
		builder.append("timeInterval: ");
		builder.append(getTimeInterval());
		builder.append("\n");
		builder.append("Show Bifurcation Diagram: ");
		builder.append(showBifurcationDiagram());
		builder.append("\n");
		builder.append("Show Configuration Space: ");
		builder.append(showConfigurationSpace());
		builder.append("\n");
		builder.append("Show Phase Space: ");
		builder.append(showPhaseSpace());
		builder.append("\n");
		return builder.toString();
	}
}
