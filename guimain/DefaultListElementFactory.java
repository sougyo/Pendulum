package guimain;

import pendulum.EulerAngle;

public class DefaultListElementFactory {
	
	static public ListElement create() {
		AmgParam amgParam = new AmgParam("1.0", "1.0", "1.0");
		ScheduleParam.FieldInfo fieldInfo = new ScheduleParam.FieldInfo("1.1", "0.5", "0", "0", "(1.0,1.0,0.0)", "10000");
		ScheduleParam.AreaInfo areaInfo = new ScheduleParam.AreaInfo(true, false, "1.1 0.5 0 0 (1.0,1.0,0.0) 10000");
		EulerAngle eulerAngle = new EulerAngle(0.0, 0.0, 0.0);
		ScheduleParam scheduleParam = new ScheduleParam(true, false, fieldInfo, areaInfo, eulerAngle);
		ViewParam viewParam = new ViewParam(false, true, false, "40", "1", "0.01");
		
		return new ListElement("new schedule", amgParam, scheduleParam, viewParam);
	}
}
