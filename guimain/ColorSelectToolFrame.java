package guimain;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

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