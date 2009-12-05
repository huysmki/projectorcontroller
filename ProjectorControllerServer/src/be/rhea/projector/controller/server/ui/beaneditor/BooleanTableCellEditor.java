package be.rhea.projector.controller.server.ui.beaneditor;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

public class BooleanTableCellEditor extends AbstractCellEditor implements
		TableCellEditor {
	private static final long serialVersionUID = 1L;
	private JCheckBox checkBox;
	private final boolean boolValue;

	public BooleanTableCellEditor(boolean boolValue) {
		this.boolValue = boolValue;
	}

	public Component getTableCellEditorComponent(JTable jtable, Object obj,
			boolean flag, int i, int j) {
		checkBox = new JCheckBox();
		checkBox.setSelected(boolValue);
		return checkBox;
	}

	public Object getCellEditorValue() {
		return Boolean.toString(checkBox.isSelected());
	}
}
