package be.rhea.projector.controller.server.ui.beaneditor;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellRenderer;

import be.rhea.projector.controller.server.scenario.ClientType;

public class ClientTypeTableCellRenderer extends AbstractCellEditor implements
		TableCellRenderer{
	private static final long serialVersionUID = 1L;
	private JTextField textField;
	private final String value;

	public ClientTypeTableCellRenderer(String value) {
		this.value = value;
	}

	public Object getCellEditorValue() {
		return value;
	}

	public Component getTableCellRendererComponent(JTable jtable, Object obj,
			boolean flag, boolean flag1, int i, int j) {
		textField = new JTextField();
		textField.setEditable(false);
		textField.setText(ClientType.valueOf(value).getName());
		return textField;

	}
}
