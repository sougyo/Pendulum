package guimain;

import java.io.Serializable;

import pendulum.EulerAngle;
public class ScheduleParam implements Serializable {
	private static final long serialVersionUID = 1L;

	private FieldInfo fieldInfo;
	private AreaInfo areaInfo;
	private EulerAngle eulerAngle;
	private boolean fieldFlag;
	private boolean areaFlag;
	
	public ScheduleParam(boolean fieldFlag, boolean areaFlag, FieldInfo fieldInfo, AreaInfo areaInfo, EulerAngle eulerAngle){
		this.fieldFlag = fieldFlag;
		this.areaFlag = areaFlag;
		this.fieldInfo = fieldInfo;
		this.areaInfo = areaInfo;
		this.eulerAngle = eulerAngle;
	}
	
	public FieldInfo getFieldInfo() {
		return fieldInfo;
	}
	
	public AreaInfo getAreaInfo() {
		return areaInfo;
	}
	
	public EulerAngle getEulerAngle() {
		return eulerAngle;
	}
	
	public boolean getFieldFlag() {
		return fieldFlag;
	}
	public boolean getAreaFlag() {
		return areaFlag;
	}
	
	public static class FieldInfo implements Serializable {
		private static final long serialVersionUID = 1L;
		private String h;
		private String l;
		private String delta;
		private String phi0;
		private String color;
		private String frame;
		public FieldInfo(String h, String l, String delta, String phi0, String color, String frame) {
			this.h = h;
			this.l = l;
			this.delta = delta;
			this.phi0 = phi0;
			this.color = color;
			this.frame = frame;
		}
		public String getH() {
			return h;
		}
		public String getL() {
			return l;
		}
		public String getDelta() {
			return delta;
		}
		public String getPhi0() {
			return phi0;
		}
		public String getColor() {
			return color;
		}
		public String getFrame() {
			return frame;
		}
	}
	public static class AreaInfo implements Serializable {
		private static final long serialVersionUID = 1L;
		private boolean isSequential;
		private boolean isSimultaneous;
		private String areaText;
		
		public AreaInfo(boolean isSequential, boolean isSimultaneous, String areaText) {
			this.isSequential = isSequential;
			this.isSimultaneous = isSimultaneous;
			this.areaText = areaText;
		}
		public boolean isSequential() {
			return isSequential;
		}
		public boolean isSimultaneous() {
			return isSimultaneous;
		}
		public String getAreaText() {
			return areaText;
		}
	}
}
