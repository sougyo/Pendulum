package guimain;

import java.util.ArrayList;

public class Schedule {
	private ArrayList<ScheduleElement> list = new ArrayList<ScheduleElement>();
	private boolean isSimultaneous = false;
	public Schedule() {
		
	}
	public Schedule(boolean isSimultaneous) {
		this.isSimultaneous = isSimultaneous;
	}
	public void add(ScheduleElement element) {
		list.add(element);
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (ScheduleElement element : list) {
			builder.append("  ");
			builder.append(element.toString());
			builder.append("\n");
		}
		return builder.toString();
	}
	public ScheduleElement get(int index) {
		return list.get(index);
	}
	public int size() {
		return list.size();
	}
	public boolean isSimultaneous() {
		return isSimultaneous;
	}
}
