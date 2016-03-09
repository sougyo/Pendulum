package guimain;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

class MainParamPanel extends JPanel {
	private static final long serialVersionUID = 2736734983546209460L;
	
	private AmgParamPanel amgParamPanel = new AmgParamPanel();
	private ScheduleParamPanel scheduleParamPanel = new ScheduleParamPanel();
	private ViewParamPanel viewParamPanel = new ViewParamPanel(new PlayButtonActionListener());
	private JTextField nameTextField = new JTextField(20);
	public MainParamPanel() {
		super();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JPanel namePanel = new JPanel();
		namePanel.add(new JLabel("Schedule Name:"));
		namePanel.add(nameTextField);
		
		add(namePanel);
		add(amgParamPanel);
		add(scheduleParamPanel);
		add(viewParamPanel);
	}
	
	public ListElement getListElement() {
		AmgParam amgParam = amgParamPanel.getAmgParam();
		ScheduleParam scheduleParam = scheduleParamPanel.getScheduleParam();
		ViewParam viewParam = viewParamPanel.getViewParam();
		
		return new ListElement(nameTextField.getText(), amgParam, scheduleParam, viewParam);
	}
	
	public void setListElement(ListElement listElement) {
		nameTextField.setText(listElement.getName());
		amgParamPanel.setAmgParam(listElement.getAmgParam());
		scheduleParamPanel.setScheduleParam(listElement.getScheduleParam());
		viewParamPanel.setViewParam(listElement.getViewParam());
	}
	
	class PlayButtonActionListener implements ActionListener {
		@Override
		public synchronized void actionPerformed(ActionEvent arg0) {
			ListElement element = getListElement();
			try {
				PlayParam param = new PlayParam(element);
				System.out.println(param);
				new PendulumPlayer(param);
			} catch (PlayParamConstructException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "fail", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
