package guimain;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

class AmgParamPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JTextField aTextField = new JTextField(5);
	private JTextField mTextField = new JTextField(5);
	private JTextField gTextField = new JTextField(5);
	
	public AmgParamPanel() {
		super();
		add(new JLabel("Radius(m):"));
		add(aTextField);
		add(new JLabel("Mass(kg):"));
		add(mTextField);
		add(new JLabel("Gravity(m/s^2):"));
		add(gTextField);
	}
	
	public void setAmgParam(AmgParam param) {
		aTextField.setText(param.getA());
		mTextField.setText(param.getM());
		gTextField.setText(param.getG());
	}
	
	public AmgParam getAmgParam() {
		return new AmgParam(aTextField.getText(), mTextField.getText(), gTextField.getText());
	}
}
