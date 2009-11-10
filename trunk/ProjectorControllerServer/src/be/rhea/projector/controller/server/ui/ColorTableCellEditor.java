package be.rhea.projector.controller.server.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

public class ColorTableCellEditor extends AbstractCellEditor implements
		TableCellEditor, ActionListener {
	private static final long serialVersionUID = 1L;
	private Color color;
	private JTextField textField;

	public ColorTableCellEditor(Color color) {
		this.color = color;
	}

	@Override
	public Component getTableCellEditorComponent(JTable jtable, Object obj,
			boolean flag, int i, int j) {
		// TODO Auto-generated method stub
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		textField = new JTextField(colorToString());
		textField.setEditable(false);
		textField.setBackground(color);
		panel.add(textField, BorderLayout.CENTER);
		JButton button = new JButton("...");
		button.setPreferredSize(new Dimension(20,20));
		button.addActionListener(this);
		panel.add(button, BorderLayout.EAST);
		return panel;
	}

	@Override
	public Object getCellEditorValue() {
		return colorToString();
	}

	private String colorToString() {
		return color.getRed()+ "," + color.getGreen() + "," + color.getBlue();
	}

	@Override
	public void actionPerformed(ActionEvent actionevent) {
		Color newColor = JColorChooser.showDialog(null, "Choose a color", color);
		System.out.println(color);
		if (newColor != null) {
			color = newColor;
			textField.setText(colorToString());
			textField.setBackground(color);
		}
		
		
	}
}
