package be.rhea.projector.controller.server.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

public class ButtonTableCellEditor extends AbstractCellEditor implements
		TableCellEditor, ActionListener {
	private static final long serialVersionUID = 1L;
	private Color color;

	public ButtonTableCellEditor(Color color) {
		this.color = color;
	}

	@Override
	public Component getTableCellEditorComponent(JTable jtable, Object obj,
			boolean flag, int i, int j) {
		// TODO Auto-generated method stub
		JButton button = new JButton("Choose Color");
		button.addActionListener(this);
		return button;
	}

	@Override
	public Object getCellEditorValue() {
		System.out.println("getCellEditorValue " + color);
		return color.getRed()+ "," + color.getGreen() + "," + color.getBlue();
	}

	@Override
	public void actionPerformed(ActionEvent actionevent) {
		JColorChooser chooser = new JColorChooser(color);
		color = chooser.showDialog(null, "Choose a color", color);
		
	}
}
