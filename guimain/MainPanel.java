package guimain;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

class MainPanel extends JPanel implements ListSelectionListener {
	private static final long serialVersionUID = -5547378255486874712L;

	private ListPanel listPanel;
	private MainParamPanel paramPanel = new MainParamPanel();
	private DefaultListModel listModel = new DefaultListModel();
	private OperatePanel operatePanel = new OperatePanel();
	
	private int prevSelectedIndex = -1;
	
	public MainPanel() {
		super(new BorderLayout());
		
		listPanel = new ListPanel(listModel, this);
		add(operatePanel, BorderLayout.NORTH);
		add(listPanel, BorderLayout.CENTER);
		add(paramPanel, BorderLayout.SOUTH);
		
		init();
	}
	
	private void init() {
		prevSelectedIndex = -1;
		listModel.clear();
		listModel.addElement(DefaultListElementFactory.create());
		listPanel.setSelectedIndex(0);
	}
	
	class OperatePanel extends JPanel {
		private static final long serialVersionUID = 1L;
		
		private JButton newButton = new JButton("New");
		private JButton upButton = new JButton("Up");
		private JButton downButton = new JButton("Down");
		private JButton updateButton = new JButton("Update");
		private JButton removeButton = new JButton("Remove");
		private JCheckBox updateCheckBox = new JCheckBox("Update Automatically", true);
		
		public OperatePanel() {
			super();
			setOpaque(false);
			add(newButton);
			add(removeButton);
			add(updateButton);
			add(updateCheckBox);
			add(upButton);
			add(downButton);
			
			newButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					listModel.addElement(DefaultListElementFactory.create());
					listPanel.setSelectedIndex(listModel.size() - 1);
				}
			});
			
			removeButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					int index = listPanel.getSelectedIndex();
					if (index >= 0)
						listModel.remove(index);

					prevSelectedIndex = -1;
					if (listModel.getSize() == 0) {
						listModel.addElement(DefaultListElementFactory.create());
						listPanel.setSelectedIndex(0);
						updateParamPanel();
					} else {
						
						listPanel.setSelectedIndex(0);
					}
				}
			});
			
			updateButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					int index = listPanel.getSelectedIndex();
					if (index < 0)
						return;
					updateListElement(index, true);
				}				
			});
			
			upButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					int index = listPanel.getSelectedIndex();
					if (index <= 0)
						return;
					
					Object obj = listModel.get(index);
					listModel.remove(index);
					listModel.add(index - 1, obj);
					prevSelectedIndex = index - 1;
					listPanel.setSelectedIndex(index - 1);
				}
			});
			
			downButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					int index = listPanel.getSelectedIndex();
					if (index < 0)
						return;
					if (index >= listModel.getSize() - 1)
						return;
					
					Object obj = listModel.get(index);
					listModel.remove(index);
					listModel.add(index + 1, obj);
					prevSelectedIndex = index + 1;
					listPanel.setSelectedIndex(index + 1);
				}
			});
		}
		public boolean isAutomaticUpdate() {
			return updateCheckBox.isSelected();
		}
	}

	@Override
	public synchronized void valueChanged(ListSelectionEvent arg0) {
		int index = listPanel.getSelectedIndex();
		if (index < 0)
			return;
		updateListElement(prevSelectedIndex, false);
		updateParamPanel();
		prevSelectedIndex = index;
	}
	
	private void updateListElement(int index, boolean forced) {
		if (!forced && !operatePanel.isAutomaticUpdate())
			return;
		if (index < 0 || listModel.getSize() <= index)
			return;
		listModel.set(index, paramPanel.getListElement());
	}
	
	private void updateParamPanel() {
		int index = listPanel.getSelectedIndex();
		if (index < 0)
			return;
		Object obj = listModel.get(index);
		if (obj == null)
			return;
		paramPanel.setListElement((ListElement)obj);
	}
	
	public void setData(DefaultListModel listModel) {
		this.listModel = listModel;
		listPanel.setModel(listModel);
		prevSelectedIndex = -1;
		listPanel.setSelectedIndex(0);
	}
	public DefaultListModel getData() {
		return listModel;
	}
	public void clearData() {
		init();
	}
}
