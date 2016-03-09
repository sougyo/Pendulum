package guimain;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import pendulum.EulerAngle;

class EulerAnglePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final int DIV = 100;
	private JSlider phiSlider = new JSlider(0, DIV, 0);
	private JSlider thetaSlider = new JSlider(0, DIV, 0);
	private JSlider psiSlider = new JSlider(0, DIV, 0);
	
	private JPanel phiPanel = new JPanel();
	private JPanel thetaPanel = new JPanel();
	private JPanel psiPanel = new JPanel();
	
	private final int TEXTFIELDCOLUMNS = 4;
	private JTextField phiTextField = new JTextField(TEXTFIELDCOLUMNS);
	private JTextField thetaTextField = new JTextField(TEXTFIELDCOLUMNS);
	private JTextField psiTextField = new JTextField(TEXTFIELDCOLUMNS);
	public EulerAnglePanel() {
		super();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		add(new JLabel("View Parameters (Euler Angles)"));
		
		JPanel phiLabelPanel = new JPanel();
		JPanel thetaLabelPanel = new JPanel();
		JPanel psiLabelPanel = new JPanel();
		phiLabelPanel.add(new JLabel("Phi[rad] (0-2*Pi)"));
		thetaLabelPanel.add(new JLabel("Theta[rad] (0-Pi)"));
		psiLabelPanel.add(new JLabel("Psi[rad] (0-2*pi)"));
		
		int labelWidth = 150;
		int labelHeight = 27;
		phiLabelPanel.setPreferredSize(new Dimension(labelWidth, labelHeight));
		thetaLabelPanel.setPreferredSize(new Dimension(labelWidth, labelHeight));
		psiLabelPanel.setPreferredSize(new Dimension(labelWidth, labelHeight));
		
		phiTextField.setEditable(false);
		thetaTextField.setEditable(false);
		psiTextField.setEditable(false);
		
		setEulerAngle(new EulerAngle(getPhiValue(), getThetaValue(), getPsiValue()));
		
		phiPanel.add(phiLabelPanel);
		thetaPanel.add(thetaLabelPanel);
		psiPanel.add(psiLabelPanel);
		
		phiPanel.add(phiSlider);
		thetaPanel.add(thetaSlider);
		psiPanel.add(psiSlider);
		
		phiPanel.add(phiTextField);
		thetaPanel.add(thetaTextField);
		psiPanel.add(psiTextField);
		
		add(phiPanel);
		add(thetaPanel);
		add(psiPanel);
		
		phiSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				phiTextField.setText(valToString(getPhiValue()));
			}
		});
		thetaSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				thetaTextField.setText(valToString(getThetaValue()));
			}
		});
		psiSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				psiTextField.setText(valToString(getPsiValue()));
			}
		});
	}
	public EulerAngle getEulerAngle() {
		return new EulerAngle(getPhiValue(), getThetaValue(), getPsiValue());
	}
	public void setEulerAngle(EulerAngle e) {
		e.normalize();
		phiSlider.setValue((int)(0.5 * e.getPhi() * phiSlider.getMaximum() / Math.PI));
		thetaSlider.setValue((int)(e.getTheta() * thetaSlider.getMaximum() / Math.PI));
		psiSlider.setValue((int)(0.5 * e.getPsi() * psiSlider.getMaximum() / Math.PI));
		
		phiTextField.setText(valToString(e.getPhi()));
		thetaTextField.setText(valToString(e.getTheta()));
		psiTextField.setText(valToString(e.getPsi()));
	}
	private double getPhiValue() {
		double val = phiSlider.getValue() * 2.0 * Math.PI / phiSlider.getMaximum();
		return val;
	}
	private double getThetaValue() {
		double val = thetaSlider.getValue() * Math.PI / thetaSlider.getMaximum();
		return val;
	}
	private double getPsiValue() {
		double val = psiSlider.getValue() * 2.0 * Math.PI / psiSlider.getMaximum();
		return val;
	}
	private String valToString(double val) {
		String temp = String.valueOf(val);
		return (temp.length() <= TEXTFIELDCOLUMNS) ? temp : temp.substring(0, TEXTFIELDCOLUMNS);
	}
}

class ScheduleParamPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JRadioButton fieldRadioButton = new JRadioButton("Single Schedule");
	private JRadioButton areaRadioButton = new JRadioButton("Multi Schedule");
	private ButtonGroup buttonGroup = new ButtonGroup();
	
	private JRadioButton simultaneousRadioButton = new JRadioButton("Simultaneous");
	private JRadioButton sequentialRadioButton = new JRadioButton("Sequential", true);
	private ButtonGroup orderButtonGroup = new ButtonGroup();
	
	private JPanel radioButtonPanel = new JPanel();
	private JPanel inputPanel = new JPanel(new BorderLayout());
	private JPanel fieldPanel = new JPanel(new BorderLayout());
	private JPanel areaPanel = new JPanel(new BorderLayout());
	
	private Color fieldPanelBgColor = new Color(1.0f, 0.6f, 1.0f);
	private Color areaPanelBgColor = new Color(0.6f, 1.0f, 0.6f);
	
	private JTextField hTextField = new JTextField(5);
	private JTextField lTextField = new JTextField(6);
	private JTextField deltaTextField = new JTextField(5);
	private JTextField phi0TextField = new JTextField(5);
	private JTextField colorTextField = new JTextField(10);
	private JTextField frameTextField = new JTextField(7);
	
	private JTextArea inputArea = new JTextArea(5, 45);
	private JScrollPane scrollPane = new JScrollPane();
	
	private JButton eulerAngleButton = new JButton("Euler Angle");
	private JFrame eulerAngleFrame = new JFrame("Euler Angle Frame");
	private EulerAnglePanel eulerAnglePanel = new EulerAnglePanel();
	
	public ScheduleParamPanel() {
		super(new BorderLayout());
		
		buttonGroup.add(fieldRadioButton);
		buttonGroup.add(areaRadioButton);

		orderButtonGroup.add(sequentialRadioButton);
		orderButtonGroup.add(simultaneousRadioButton);
		
		radioButtonPanel.add(fieldRadioButton);
		radioButtonPanel.add(areaRadioButton);

		add(radioButtonPanel, BorderLayout.NORTH);
		
		fieldPanel.setOpaque(false);
		areaPanel.setOpaque(false);
		
		inputPanel.add(fieldPanel, BorderLayout.NORTH);
		inputPanel.add(areaPanel, BorderLayout.CENTER);
		inputPanel.setOpaque(false);
		add(inputPanel, BorderLayout.CENTER);
		
		JPanel southPanel = new JPanel(new BorderLayout());
		JPanel toolPanel = new JPanel();
		JButton colorSelectToolButton = new JButton("Show color select tool");
		toolPanel.add(colorSelectToolButton);
		toolPanel.add(eulerAngleButton);
		final JFrame colorSelectToolFrame = new ColorSelectToolFrame();
		colorSelectToolButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				colorSelectToolFrame.setVisible(true);
			}
		});
		southPanel.add(toolPanel, BorderLayout.CENTER);
		add(southPanel, BorderLayout.SOUTH);
		
		JPanel fieldNorthPanel = new JPanel();
		JPanel fieldSouthPanel = new JPanel();
		fieldNorthPanel.setOpaque(false);
		fieldSouthPanel.setOpaque(false);
		fieldNorthPanel.add(new JLabel("h"));
		fieldNorthPanel.add(hTextField);
		fieldNorthPanel.add(new JLabel("l"));
		fieldNorthPanel.add(lTextField);
		fieldNorthPanel.add(new JLabel("delta"));
		fieldNorthPanel.add(deltaTextField);
		fieldNorthPanel.add(new JLabel("phi0(degree)"));
		fieldNorthPanel.add(phi0TextField);
		fieldSouthPanel.add(new JLabel("color(r,g,b)"));
		fieldSouthPanel.add(colorTextField);
		fieldSouthPanel.add(new JLabel("frame"));
		fieldSouthPanel.add(frameTextField);
		fieldPanel.add(fieldNorthPanel, BorderLayout.NORTH);
		fieldPanel.add(fieldSouthPanel, BorderLayout.SOUTH);
		
		scrollPane.getViewport().add(inputArea);
		JPanel areaNorthPanel = new JPanel();
		JPanel areaCenterPanel = new JPanel();
		areaNorthPanel.setOpaque(false);
		areaCenterPanel.setOpaque(false);
		areaNorthPanel.add(new JLabel("h l delta phi0 (r,g,b) frame"));
		//areaNorthPanel.add(sequentialRadioButton);
		//areaNorthPanel.add(simultaneousRadioButton);
		areaCenterPanel.add(scrollPane);
		areaPanel.add(areaNorthPanel, BorderLayout.NORTH);
		areaPanel.add(areaCenterPanel, BorderLayout.SOUTH);
		
		setFieldEnabled(false);
		setAreaEnabled(false);
		
		fieldRadioButton.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					setFieldEnabled(true);
					fieldPanel.setOpaque(true);
					fieldPanel.setBackground(fieldPanelBgColor);
				}
				if (e.getStateChange() == ItemEvent.DESELECTED) {
					setFieldEnabled(false);
					fieldPanel.setOpaque(false);
				}
				inputPanel.repaint();
			}
		});
		areaRadioButton.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					setAreaEnabled(true);
					areaPanel.setOpaque(true);
					areaPanel.setBackground(areaPanelBgColor);
				}
				
				if (e.getStateChange() == ItemEvent.DESELECTED) {
					setAreaEnabled(false);
					areaPanel.setOpaque(false);
				}
			}
		});
		
		initEulerAngle();
	}
	
	public void initEulerAngle() {
		eulerAngleButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				eulerAngleFrame.setVisible(true);
			}			
		});
		eulerAngleFrame.getContentPane().add(eulerAnglePanel);
		eulerAngleFrame.setSize(450, 150);
		eulerAngleFrame.setResizable(false);
		eulerAngleFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	}
	
	private void setFieldEnabled(boolean enabled) {
		hTextField.setEnabled(enabled);
		lTextField.setEnabled(enabled);
		deltaTextField.setEnabled(enabled);
		phi0TextField.setEnabled(enabled);
		colorTextField.setEnabled(enabled);
		frameTextField.setEnabled(enabled);
	}
	
	private void setAreaEnabled(boolean enabled) {
		inputArea.setEnabled(enabled);
		scrollPane.setEnabled(enabled);
		simultaneousRadioButton.setEnabled(enabled);
		sequentialRadioButton.setEnabled(enabled);
	}
	
	public void setScheduleParam(ScheduleParam param) {
		fieldRadioButton.setSelected(param.getFieldFlag());
		areaRadioButton.setSelected(param.getAreaFlag());
		ScheduleParam.FieldInfo fieldInfo = param.getFieldInfo();
		hTextField.setText(fieldInfo.getH());
		lTextField.setText(fieldInfo.getL());
		deltaTextField.setText(fieldInfo.getDelta());
		phi0TextField.setText(fieldInfo.getPhi0());
		colorTextField.setText(fieldInfo.getColor());
		frameTextField.setText(fieldInfo.getFrame());
		ScheduleParam.AreaInfo areaInfo = param.getAreaInfo();
		sequentialRadioButton.setSelected(areaInfo.isSequential());
		simultaneousRadioButton.setSelected(areaInfo.isSimultaneous());
		inputArea.setText(areaInfo.getAreaText());
		eulerAnglePanel.setEulerAngle(param.getEulerAngle());
	}
	public ScheduleParam getScheduleParam() {
		EulerAngle eulerAngle = eulerAnglePanel.getEulerAngle();
		ScheduleParam.FieldInfo fieldInfo = new ScheduleParam.FieldInfo(
				hTextField.getText(),
				lTextField.getText(),
				deltaTextField.getText(),
				phi0TextField.getText(),
				colorTextField.getText(),
				frameTextField.getText());
		ScheduleParam.AreaInfo areaInfo = new ScheduleParam.AreaInfo(
				sequentialRadioButton.isSelected(),
				simultaneousRadioButton.isSelected(),
				inputArea.getText());
		return new ScheduleParam(
				fieldRadioButton.isSelected(),
				areaRadioButton.isSelected(),
				fieldInfo,
				areaInfo,
				eulerAngle);
	}
}
