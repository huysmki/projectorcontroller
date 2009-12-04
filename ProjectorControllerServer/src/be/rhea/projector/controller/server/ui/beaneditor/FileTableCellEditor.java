package be.rhea.projector.controller.server.ui.beaneditor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

public class FileTableCellEditor extends AbstractCellEditor implements
		TableCellEditor, ActionListener {
	private static final long serialVersionUID = 1L;
	private String fileName;
	private JTextField textField;

	public FileTableCellEditor(String fileName) {
		this.fileName = fileName;
	}

	public Component getTableCellEditorComponent(JTable jtable, Object obj,
			boolean flag, int i, int j) {
		// TODO Auto-generated method stub
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		textField = new JTextField(fileName);
//		textField.setEditable(false);
		panel.add(textField, BorderLayout.CENTER);
		JButton button = new JButton("...");
		button.setPreferredSize(new Dimension(20,20));
		button.addActionListener(this);
		panel.add(button, BorderLayout.EAST);
		return panel;
	}

	public Object getCellEditorValue() {
		return textField.getText();
	}

	public void actionPerformed(ActionEvent actionevent) {
		JFileChooser fileChooser = new JFileChooser();
		if (fileChooser.showDialog(null, "Select")== JFileChooser.APPROVE_OPTION) {
			fileName = fileChooser.getSelectedFile().getName();
			textField.setText(fileName);
		}
		
		
	}
}
