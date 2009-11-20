package be.rhea.projector.controller.server.ui.beaneditor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableCellEditor;

public class ArtNetTableCellEditor extends AbstractCellEditor implements
		TableCellEditor, ActionListener {
	private static final long serialVersionUID = 1L;
	private ArrayList<Integer> data;
	private JTextField textField;

	public ArtNetTableCellEditor(ArrayList<Integer> data) {
		this.data = data;
	}

	@Override
	public Component getTableCellEditorComponent(JTable jtable, Object obj,
			boolean flag, int i, int j) {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		textField = new JTextField(dataToString());
		textField.setEditable(false);
		panel.add(textField, BorderLayout.CENTER);
		JButton button = new JButton("...");
		button.setPreferredSize(new Dimension(20,20));
		button.addActionListener(this);
		button.setFocusable(false);
		panel.add(button, BorderLayout.EAST);
		
		return panel;
	}
	
	@Override
	public Object getCellEditorValue() {
		return dataToString();
	}

	private String dataToString() {
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		for (Integer integer : data) {
			if (!first) {
				builder.append(",");
			}
			builder.append(integer);
			first = false;
		}
		return builder.toString();
	}
	
	@Override
	public void actionPerformed(ActionEvent actionevent) {
        ArtNetValueChangeDialog myDialog = new ArtNetValueChangeDialog(null, true, "Edit Values", data);
        if(myDialog.getAnswer()) {
        	data = myDialog.getData();
        	textField.setText(dataToString());
        }
	}
	
	class ArtNetValueChangeDialog extends JDialog implements ActionListener {
		private static final long serialVersionUID = 4024038618923174543L;
		private JPanel panel = null;
	    private JButton okButton = null;
	    private JButton cancelButton = null;
	    private boolean answer = false;
		private ArrayList<Integer> data;
	    private ArrayList<SliderAndTextComponent> sliders = new ArrayList<SliderAndTextComponent>();
		
		public boolean getAnswer() { 
	    	return answer; 
	    }
		
		public ArrayList<Integer> getData() {
			return data;
		}

	    public ArtNetValueChangeDialog(JFrame frame, boolean modal, String title, ArrayList<Integer> data) {
	        super(frame, modal);
			this.data = data;
	        this.setTitle(title);
	        this.setResizable(false);
	        panel = new JPanel();
	        getContentPane().add(panel);
	        panel.setLayout(new BorderLayout());
	        
	        JPanel slidersPanel = new JPanel();
	        slidersPanel.setLayout(new GridLayout(512,1));
	        for (int i = 0; i < 512; i++) {
	        	int value = 0;
	        	try {
	        		value = data.get(i);
	        	} catch (Exception e) {
	        	}
	        	
	        	SliderAndTextComponent sliderAndTextComponent = new SliderAndTextComponent(i, value);
	        	sliders.add(sliderAndTextComponent);
				slidersPanel.add(sliderAndTextComponent);
	        }
	        JScrollPane scrollPane = new JScrollPane(slidersPanel);
	        scrollPane.setPreferredSize(new Dimension(300, 300));
	        panel.add(scrollPane, BorderLayout.CENTER);
	        
	        JPanel buttonPanel = new JPanel();
	        buttonPanel.setLayout(new BorderLayout());
	        okButton = new JButton("OK");
	        okButton.addActionListener(this);
	        buttonPanel.add(okButton, BorderLayout.WEST); 
	        cancelButton = new JButton("Cancel");
	        cancelButton.addActionListener(this);
	        buttonPanel.add(cancelButton, BorderLayout.EAST);  
	        panel.add(buttonPanel, BorderLayout.SOUTH);
	        pack();
	        setLocationRelativeTo(frame);
	        setVisible(true);
	    }

	    public void actionPerformed(ActionEvent e) {
	        if(okButton == e.getSource()) {
	        	data = new ArrayList<Integer>();
		        for (int i = 0; i < 512; i++) {
		        	data.add(sliders.get(i).getValue());
		        }	        	
	            answer = true;
	            setVisible(false);
	        }
	        else if(cancelButton == e.getSource()) {
	            answer = false;
	            setVisible(false);
	        }
	    }
	    
	}	
	class SliderAndTextComponent extends JPanel implements ChangeListener {
		private static final long serialVersionUID = 1L;
		private int value;
		private final int index;
		private JTextField valueField;
		private JSlider slider;
		
		public int getValue() {
			return value;
		}

		public SliderAndTextComponent(int index, int value) {
			this.index = index;
			this.value = value;
			JLabel label = new JLabel();
			label.setText(String.valueOf(index + 1));
			label.setPreferredSize(new Dimension(25,20));
			this.add(label);
			valueField = new JTextField();
			valueField.setText(String.valueOf(value));
			valueField.setPreferredSize(new Dimension(25,20));
			valueField.setEditable(false);
			this.add(valueField);
			slider = new JSlider();
			slider.addChangeListener(this);
			slider.setMinimum(0);
			slider.setMaximum(255);
			slider.setValue(value);
			this.add(slider);
		}

		@Override
		public void stateChanged(ChangeEvent changeevent) {
			value = slider.getValue();
			valueField.setText(String.valueOf(value));
		}
		
	}
}
