package guimain;

import java.io.Serializable;

public class ListElement implements Serializable {
	private static final long serialVersionUID = 1L;

	private String name;
	private AmgParam amgParam;
	private ScheduleParam scheduleParam;
	private ViewParam viewParam;
	
	public ListElement(String name, AmgParam amgParam, ScheduleParam scheduleParam, ViewParam viewParam) {
		this.name = name;
		this.amgParam = amgParam;
		this.scheduleParam = scheduleParam;
		this.viewParam = viewParam;
	}
	
	public String getName() {
		return name;
	}
	
	public AmgParam getAmgParam() {
		return amgParam;
	}
	
	public ScheduleParam getScheduleParam() {
		return scheduleParam;
	}
	
	public ViewParam getViewParam() {
		return viewParam;
	}
	
	@Override
	public String toString() {
		if (name == null || name.length() == 0)
			return "(null string)";
		return name;
	}
}
