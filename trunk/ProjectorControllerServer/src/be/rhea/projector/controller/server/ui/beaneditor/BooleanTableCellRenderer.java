package be.rhea.projector.controller.server.ui.beaneditor;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class BooleanTableCellRenderer extends AbstractCellEditor implements
		TableCellRenderer{
	private static final long serialVersionUID = 1L;
	private boolean boolValue;
	private JCheckBox checkBox;

	public BooleanTableCellRenderer(boolean boolValue) {
		this.boolValue = boolValue;
	}

	public Object getCellEditorValue() {
		return new Boolean(boolValue).toString();
	}

	public Component getTableCellRendererComponent(JTable jtable, Object obj,
			boolean flag, boolean flag1, int i, int j) {
		checkBox = new JCheckBox();
		checkBox.setSelected(boolValue);
		checkBox.setEnabled(false);
		return checkBox;

	}
}
