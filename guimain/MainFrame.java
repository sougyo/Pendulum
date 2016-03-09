package guimain;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

class MainFrame extends JFrame implements WindowListener {
	private static final long serialVersionUID = 1L;
	
	private JMenuBar menuBar = new JMenuBar();
	private JMenu fileMenu = new JMenu("File");
	private JMenuItem newMenu = new JMenuItem("new");
	private JMenuItem openMenu = new JMenuItem("open");
	private JMenuItem saveMenu = new JMenuItem("save");
	private JMenuItem exitMenu = new JMenuItem("exit");
	
	private JFileChooser fileChooser = new JFileChooser();
	private FileNameExtensionFilter fileFilter = new FileNameExtensionFilter("spherical pendulum file(*.spd)","spd");
	public MainFrame() {
		super("Topology of Spherical Pendulum");
		
		fileChooser.setFileFilter(fileFilter);
		
		final MainPanel panel = new MainPanel();
		getContentPane().add(panel);
		setLocation(100, 100);
		setSize(590, 550);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		fileMenu.add(newMenu);
		fileMenu.add(openMenu);
		fileMenu.add(saveMenu);
		fileMenu.add(exitMenu);
		menuBar.add(fileMenu);
		setJMenuBar(menuBar);
		
		setResizable(false);
		
		newMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int result = JOptionPane.showConfirmDialog(null, "ok?", "Confirmation", JOptionPane.YES_NO_OPTION);
				if (result != JOptionPane.OK_OPTION)
					return;
				panel.clearData();
			}
		});
		openMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int result = JOptionPane.showConfirmDialog(null, "ok?", "Confirmation", JOptionPane.YES_NO_OPTION);
				if (result != JOptionPane.OK_OPTION)
					return;
				
				result = fileChooser.showOpenDialog(null);
				if (result == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					
					if (!file.canRead()) {
						JOptionPane.showMessageDialog(null, "cannot read " + file.getName(), "Messages", JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					FileInputStream fileInputStream = null;
					try {
						fileInputStream = new FileInputStream(file);
						ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
						DefaultListModel data = (DefaultListModel)objectInputStream.readObject();
						if (data != null)
							panel.setData(data);
					} catch (IOException e) {
						JOptionPane.showMessageDialog(null, "IO ERROR", "Messages", JOptionPane.ERROR_MESSAGE);
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						JOptionPane.showMessageDialog(null, "cannot open " + file.getName(), "Messages", JOptionPane.ERROR_MESSAGE);
						e.printStackTrace();
					} finally {
						if (fileInputStream != null) {
							try {
								fileInputStream.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		});
		saveMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Serializable serializable = panel.getData();
				if (serializable == null)
					return;
				int result = fileChooser.showSaveDialog(null);
				if (result == JFileChooser.APPROVE_OPTION) {
					panel.valueChanged(null);				
					
					File file = fileChooser.getSelectedFile();
					
					if (file.exists() && !file.canWrite()) {
						JOptionPane.showMessageDialog(null, "cannot write on " + file.getName(), "Messages", JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					FileOutputStream fileOutputStream = null;
					try {
						fileOutputStream = new FileOutputStream(file);
						ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
						objectOutputStream.writeObject(serializable);
						objectOutputStream.flush();
					} catch (IOException e) {
						JOptionPane.showMessageDialog(null, "IO ERROR", "Messages", JOptionPane.ERROR_MESSAGE);
						e.printStackTrace();
					} finally {
						if (fileOutputStream != null){
							try {
								fileOutputStream.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		});
		exitMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int result = JOptionPane.showConfirmDialog(null, "ok?", "Confirmation", JOptionPane.YES_NO_OPTION);
				if (result != JOptionPane.OK_OPTION)
					return;
				System.exit(0);
			}
		});
		addWindowListener(this);
		
		setVisible(true);
	}
	@Override
	public void windowActivated(WindowEvent arg0) {}
	@Override
	public void windowClosed(WindowEvent arg0) {}
	@Override
	public void windowClosing(WindowEvent arg0) {
		int result = JOptionPane.showConfirmDialog(null, "ok?", "Confirmation", JOptionPane.YES_NO_OPTION);
		if (result != JOptionPane.OK_OPTION)
			return;
		System.exit(0);
	}
	@Override
	public void windowDeactivated(WindowEvent arg0) {}
	@Override
	public void windowDeiconified(WindowEvent arg0) {}
	@Override
	public void windowIconified(WindowEvent arg0) {}
	@Override
	public void windowOpened(WindowEvent arg0) {}
}
