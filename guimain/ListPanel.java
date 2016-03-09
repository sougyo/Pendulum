package guimain;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;

class ListPanel extends JPanel {
	private static final long serialVersionUID = 755202780182108457L;
	
	private JScrollPane scrollPane = new JScrollPane();
	private JList list;
	public ListPanel(DefaultListModel listModel, ListSelectionListener listener) {
		super(new BorderLayout());
		setBackground(new Color(0.2f, 1.0f, 1.0f));

		list = new JList(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		list.addListSelectionListener(listener);
		
		scrollPane.getViewport().add(list);
		add(scrollPane, BorderLayout.CENTER);
	}
	
	public int getSelectedIndex() {
		return list.getSelectedIndex();
	}
	public void setSelectedIndex(int index) {
		list.setSelectedIndex(index);
	}
	public void setModel(ListModel listModel) {
		list.setModel(listModel);
	}
}
