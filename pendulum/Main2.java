package pendulum;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.WakeupOnElapsedTime;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;

interface AnimationTimerTask {
	public void run();
}

class AnimationTimer extends Behavior {
	protected WakeupOnElapsedTime wup = null;
	private int step;
	private int count;
	private int taskPerFrame;
	private AnimationTimerTask task;
	public AnimationTimer(int step, int sleep, int taskPerFrame, AnimationTimerTask task) {
		super();
		
		this.step = step;
		count = 0;
		this.taskPerFrame = taskPerFrame;
		this.task = task;
		
		wup = new WakeupOnElapsedTime(sleep);
		setSchedulingBounds(new BoundingSphere(new Point3d(), 100.0));
	}

	@Override
	public void initialize() {
		wakeupOn(wup);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void processStimulus(Enumeration arg0) {
		if (count < step) {
			for (int i = 0; i < taskPerFrame; i++) {
				task.run();count++;
			}
			wakeupOn(wup);
		}
	}
}

class ColorSelectToolPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JTextField colorChooseTextField = new JTextField("(1.0,1.0,1.0)", 10);
	private JPanel colorTestPanel = new JPanel();
	private JButton colorCopyButton = new JButton("copy to clipboard");
	private JButton colorChooseButton = new JButton("choose color");
	private JPanel colorChoosePanel = new JPanel();
	
	public ColorSelectToolPanel() {
		super();
		
		colorChooseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Color color = JColorChooser.showDialog(null, "Choose Color", Color.WHITE);
				if (color == null)
					return;
				float[] comp = new float[4];
				color.getRGBComponents(comp);
				
				String text = "(" + comp[0] + "," + comp[1] + "," + comp[2] + ")";
				colorChooseTextField.setText(text);
				colorTestPanel.setBackground(color);
			}
		});
		colorCopyButton.addActionListener(new ActionListener() {
			//private 
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Clipboard clipboard = Toolkit.getDefaultToolkit()
	            .getSystemClipboard();
				StringSelection selection = new StringSelection(colorChooseTextField.getText());
				clipboard.setContents(selection, selection);
			}
		});
		colorTestPanel.setPreferredSize(new Dimension(25, 25));
		
		colorChooseTextField.setEditable(false);
		colorChoosePanel.add(colorChooseButton);
		colorChoosePanel.add(colorTestPanel);
		colorChoosePanel.add(colorChooseTextField);
		colorChoosePanel.add(colorCopyButton);
		
		add(colorChoosePanel);
	}	
}

class ColorSelectToolFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	public ColorSelectToolFrame() {
		super("color select tool");
		add(new ColorSelectToolPanel());
		setSize(500, 80);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setVisible(false);
	}
}

class AnimationFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private JFrame makeFrame(Component component, String title, int x, int y, int size) {
		JFrame frame = new JFrame(title);
		frame.getContentPane().add(component);
		frame.setLocation(x, y);
		frame.setSize(size, size);
		frame.setVisible(true);
		return frame;
	}
	public AnimationFrame(final SpParam spParam, final EmParam emParam, double timeInterval, int step, final Color3f color, int sleep, int taskPerFrame, boolean showConfigration, boolean showPhase) {
		if (!showConfigration && !showPhase)
			return;
		
		final SpSolution solution = 
			EnergyMomentumSpSolutionFactory.create(spParam, emParam, timeInterval);
		
		if (solution == null) {
			JOptionPane.showMessageDialog(null, "There are no solutions", "Message", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		final ArrayList<SpCoordinateReceiver> receivers = new ArrayList<SpCoordinateReceiver>();
		final ArrayList<Viewer3d> viewers = new ArrayList<Viewer3d>();
		
		if (showConfigration) {
			ConfigurationSpace configurationSpace = new ConfigurationSpace(spParam);
			Viewer3d configurationSpaceViewer = new DefaultViewer3d();
			configurationSpaceViewer.add(configurationSpace.getNode());
			
			receivers.add(configurationSpace);
			viewers.add(configurationSpaceViewer);
		}
		
		if (showPhase) {
			PhaseSpace phaseSpace = new PhaseSpace(null);
			Viewer3d phaseSpaceViewer = new DefaultViewer3d();
			phaseSpaceViewer.add(phaseSpace.getNode());
			
			receivers.add(phaseSpace);
			viewers.add(phaseSpaceViewer);
		}
		
		if (viewers.size() == 0 || viewers.size() > 2)
			return;
		
		AnimationTimer timer = new AnimationTimer(step, sleep, taskPerFrame, new AnimationTimerTask() {
			@Override
			public void run() {
				SpCoordinate coordinate = solution.next();
				for (SpCoordinateReceiver receiver : receivers)
					receiver.setCoordinate(coordinate, color);
			}
		});
		
		int size = 400;
		for (int i = 0; i < viewers.size(); i++) {
			JFrame mainFrame = makeFrame(viewers.get(i).getComponent(), "main", 50 + size * i, 100, size);
			mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		}
		viewers.get(0).add(timer);
	}
}

class EmInputPanel extends JPanel {
	class EulerAngle {
		private double phi, theta, psi;
		public EulerAngle(double phi, double theta, double psi) {
			this.phi = phi;
			this.theta = theta;
			this.psi = psi;
		}
		public double getPhi() {
			return phi;
		}
		public double getTheta() {
			return theta;
		}
		public double getPsi() {
			return psi;
		}
	}
	
	class EulerAnglePanel extends JPanel {
		private static final long serialVersionUID = 1L;
		private JSlider phiSlider = new JSlider(0, 100, 0);
		private JSlider thetaSlider = new JSlider(0, 100, 0);
		private JSlider psiSlider = new JSlider(0, 100, 0);
		
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
			phiLabelPanel.add(new JLabel("phi[rad] (0-2*Pi)"));
			thetaLabelPanel.add(new JLabel("theta[rad] (0-Pi)"));
			psiLabelPanel.add(new JLabel("psi[rad] (0-2*pi)"));
			
			int labelWidth = 150;
			int labelHeight = 27;
			phiLabelPanel.setPreferredSize(new Dimension(labelWidth, labelHeight));
			thetaLabelPanel.setPreferredSize(new Dimension(labelWidth, labelHeight));
			psiLabelPanel.setPreferredSize(new Dimension(labelWidth, labelHeight));
			
			//phiLabelPanel.setBackground(Color.white);
			//thetaLabelPanel.setBackground(Color.white);
			//psiLabelPanel.setBackground(Color.white);
			
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
	
	private static final long serialVersionUID = 1L;
	private JRadioButton fieldRadioButton = new JRadioButton("field");
	private JRadioButton areaRadioButton = new JRadioButton("area");
	private ButtonGroup buttonGroup = new ButtonGroup();
	
	private JRadioButton simultaneousRadioButton = new JRadioButton("simultaneous");
	private JRadioButton sequentialRadioButton = new JRadioButton("sequential", true);
	private ButtonGroup orderButtonGroup = new ButtonGroup();
	
	private JPanel radioButtonPanel = new JPanel();
	private JPanel inputPanel = new JPanel(new BorderLayout());
	private JPanel fieldPanel = new JPanel(new BorderLayout());
	private JPanel areaPanel = new JPanel(new BorderLayout());
	
	private Color fieldPanelBgColor = new Color(1.0f, 0.6f, 1.0f);
	private Color areaPanelBgColor = new Color(0.6f, 1.0f, 0.6f);
	
	private JTextField hTextField = new JTextField("1", 5);
	private JTextField lTextField = new JTextField("0.1", 6);
	private JTextField deltaTextField = new JTextField("0", 5);
	private JTextField phi0TextField = new JTextField("0", 5);
	private JTextField colorTextField = new JTextField("(1.0,1.0,1.0)", 10);
	private JTextField frameTextField = new JTextField("10000", 7);
	
	//private JPopupMenu hPopupMenu = new JPopupMenu("hogehoge");
	
	private JTextArea inputArea = new JTextArea("1,0.1,0,0,(1.0,1.0,1.0),10000", 5, 30);
	private JScrollPane scrollPane = new JScrollPane();
	public EmInputPanel() {
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
		JButton colorSelectToolButton = new JButton("show color select tool");
		final JFrame colorSelectToolFrame = new ColorSelectToolFrame();
		colorSelectToolButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				colorSelectToolFrame.setVisible(true);
			}
		});
		southPanel.add(colorSelectToolButton);
		southPanel.add(new EulerAnglePanel(), BorderLayout.SOUTH);
		add(southPanel, BorderLayout.SOUTH);
		
		//hPopupMenu.add(new JLabel("hoge"));
		//hTextField.add(hPopupMenu);
		
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
		areaNorthPanel.add(new JLabel("h,l,delta,phi0,(r,g,b),frame"));
		areaNorthPanel.add(sequentialRadioButton);
		areaNorthPanel.add(simultaneousRadioButton);
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
}

class SpParamPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JTextField aTextField = new JTextField("1.0", 5);
	private JTextField mTextField = new JTextField("1.0", 5);
	private JTextField gTextField = new JTextField("1.0", 5);
	
	public SpParamPanel() {
		super();
		add(new JLabel("a:"));
		add(aTextField);
		add(new JLabel("m:"));
		add(mTextField);
		add(new JLabel("g:"));
		add(gTextField);
	}
	
	public void setParam(SpParam param) {
		aTextField.setText(String.valueOf(param.getA()));
		mTextField.setText(String.valueOf(param.getM()));
		gTextField.setText(String.valueOf(param.getG()));
	}
	
	public SpParam getParam() throws NumberFormatException {
		double a, m, g;
		a = Double.valueOf(aTextField.getText());
		m = Double.valueOf(mTextField.getText());
		g = Double.valueOf(gTextField.getText());
		return new SpParam(a, m, g);
	}
}

class ParamMainPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JButton playButton = new JButton("play");
	private JCheckBox configrationCheckBox = new JCheckBox("show conf sp", true);
	private JCheckBox phaseCheckBox = new JCheckBox("show phase sp", true);
	private JCheckBox diagramCheckBox = new JCheckBox("show bif diagram", false);
	private JTextField hTextField = new JTextField("1", 5);
	private JTextField lTextField = new JTextField("0.1", 6);
	private JTextField deltaTextField = new JTextField("0", 5);
	private JTextField phi0TextField = new JTextField("0", 5);
	private JTextField timeIntervalTextField = new JTextField("0.03", 5);
	private JTextField sleepTextField = new JTextField("40", 4);
	private JTextField stepTextField = new JTextField("10000", 6);
	private JTextField taskPerFrameTextField = new JTextField("5", 5);
	private JTextField colorRTextField = new JTextField("1.0", 4);
	private JTextField colorGTextField = new JTextField("1.0", 4);
	private JTextField colorBTextField = new JTextField("1.0", 4);
	private JButton colorChooseButton = new JButton("color choose");
	private JButton colorTestButton = new JButton("color test");
	public ParamMainPanel(final SpParam spParam) {
		super();
		
		add(new SpParamPanel());
		
		JPanel emPanel = new JPanel();
		
		add(emPanel);
		add(playButton);
		add(new JLabel("h"));
		add(hTextField);
		add(new JLabel("l"));
		add(lTextField);
		add(new JLabel("delta"));
		add(deltaTextField);
		add(new JLabel("phi0"));
		add(phi0TextField);
		add(new JLabel("°"));
		add(new JLabel("delta_t"));
		add(timeIntervalTextField);
		add(new JLabel("sleep(ms)"));
		add(sleepTextField);
		add(new JLabel("step"));
		add(stepTextField);
		add(new JLabel("task/frame"));
		add(taskPerFrameTextField);
		add(new JLabel("color r"));
		add(colorRTextField);
		add(new JLabel("g"));
		add(colorGTextField);
		add(new JLabel("b"));
		add(colorBTextField);
		add(colorTestButton);
		add(colorChooseButton);
		add(diagramCheckBox);
		add(configrationCheckBox);
		add(phaseCheckBox);
		
		add(new EmInputPanel());
		
		colorChooseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Color color = JColorChooser.showDialog(null, "Choose Color", Color.WHITE);
				if (color == null)
					return;
				float[] comp = new float[4];
				color.getRGBComponents(comp);
				colorRTextField.setText(String.valueOf(comp[0]));
				colorGTextField.setText(String.valueOf(comp[1]));
				colorBTextField.setText(String.valueOf(comp[2]));
				colorTestButton.setBackground(color);
			}
		});
		
		colorTestButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					float r, g, b;
					r = Float.valueOf(colorRTextField.getText());
					g = Float.valueOf(colorGTextField.getText());
					b = Float.valueOf(colorBTextField.getText());
					colorTestButton.setBackground(new Color(r, g, b));
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(null, "Syntax Error", "Message", JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
					return;
				}
			}
		});
		
		playButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				double h;
				double l;
				double delta;
				double phi0;
				double timeInterval;
				int sleep;
				int step;
				int taskPerFrame;
				float r, g, b;
				try {
					h = Double.valueOf(hTextField.getText());
					l = Double.valueOf(lTextField.getText());
					delta = Double.valueOf(deltaTextField.getText());
					phi0 = Double.valueOf(phi0TextField.getText()) * Math.PI / 180;
					timeInterval = Double.valueOf(timeIntervalTextField.getText());
					sleep = Integer.valueOf(sleepTextField.getText());
					step = Integer.valueOf(stepTextField.getText());
					taskPerFrame = Integer.valueOf(taskPerFrameTextField.getText());
					r = Float.valueOf(colorRTextField.getText());
					g = Float.valueOf(colorGTextField.getText());
					b = Float.valueOf(colorBTextField.getText());
					colorTestButton.setBackground(new Color(r, g, b));
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(null, "Syntax Error", "Message", JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
					return;
				}
				
				EmParam emParam = new EmParam(h, l, delta, phi0);
				boolean showConfigurationSpace = configrationCheckBox.isSelected();
				boolean showPhaseSpace = phaseCheckBox.isSelected();
				new AnimationFrame(spParam, emParam, timeInterval, step, new Color3f(r, g, b), sleep, taskPerFrame, showConfigurationSpace, showPhaseSpace);
			}			
		});
	}
}

class ParamMainFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public ParamMainFrame(SpParam spParam) {
		super("param frame " + spParam.toString());
		setLocation(100, 100);
		setSize(500, 520);
		setVisible(true);
		getContentPane().add(new ParamMainPanel(spParam));
	}
}

class MainPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JButton createButton = new JButton("create");
	public MainPanel() {
		super();
		add(new JLabel("a=1, m=1, g=1"));
		add(createButton);
		
		createButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new ParamMainFrame(new SpParam(1.0, 1.0, 1.0));
			}
		});
	}
}

public class Main2 {
	public static void main(String[] args) {
		JPanel panel = new MainPanel();
		
		JFrame frame = new JFrame("Main Frame");
		frame.getContentPane().add(panel);
		frame.setResizable(false);
		frame.setLocation(100, 100);
		frame.setSize(250, 60);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}