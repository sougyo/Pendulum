package guimain;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

class ViewParamPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private JCheckBox configurationCheckBox = new JCheckBox("Configuration Space", true);
	private JCheckBox phaseCheckBox = new JCheckBox("Phase Space", true);
	private JCheckBox diagramCheckBox = new JCheckBox("Bifurcation Diagram", false);
	private JTextField sleepTextField = new JTextField("40", 4);
	private JTextField taskPerFrameTextField = new JTextField("5", 5);
	private JTextField timeIntervalTextField = new JTextField("0.01", 6);
	private JButton playButton = new JButton("Play");
	public ViewParamPanel(ActionListener playButtonActionListener) {
		super(new BorderLayout());
		
		playButton.addActionListener(playButtonActionListener);
		JPanel checkBoxPanel = new JPanel();
		
		checkBoxPanel.add(new JLabel("Window:"));
		checkBoxPanel.add(diagramCheckBox);
		checkBoxPanel.add(configurationCheckBox);
		checkBoxPanel.add(phaseCheckBox);
		
		JPanel inputPanel = new JPanel();
		inputPanel.add(new JLabel("Sleep(ms)"));
		inputPanel.add(sleepTextField);
		inputPanel.add(new JLabel("Task/Frame"));
		inputPanel.add(taskPerFrameTextField);
		inputPanel.add(new JLabel("Delta_t"));
		inputPanel.add(timeIntervalTextField);
		inputPanel.add(playButton);
		add(checkBoxPanel, BorderLayout.CENTER);
		add(inputPanel, BorderLayout.SOUTH);
	}
	public void setViewParam(ViewParam viewParam) {
		diagramCheckBox.setSelected(viewParam.showBirfurcationDiagram());
		configurationCheckBox.setSelected(viewParam.showConfigurationSpace());
		phaseCheckBox.setSelected(viewParam.showPhaseSpace());
		sleepTextField.setText(viewParam.getSleepTime());
		taskPerFrameTextField.setText(viewParam.getTaskPerFrame());
		timeIntervalTextField.setText(viewParam.getTimeInterval());
	}
	public ViewParam getViewParam() {
		boolean showBif = diagramCheckBox.isSelected();
		boolean showConf = configurationCheckBox.isSelected();
		boolean showPhase = phaseCheckBox.isSelected();
		String sleep = sleepTextField.getText();
		String task = taskPerFrameTextField.getText();
		String timeInterval = timeIntervalTextField.getText();
		return new ViewParam(showBif, showConf, showPhase, sleep, task, timeInterval);
	}
}
