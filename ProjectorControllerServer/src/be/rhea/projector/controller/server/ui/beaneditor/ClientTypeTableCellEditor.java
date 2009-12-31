package be.rhea.projector.controller.server.ui.beaneditor;

import java.awt.Component;
import java.awt.Font;

import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import be.rhea.projector.controller.server.scenario.ClientType;

public class ClientTypeTableCellEditor extends AbstractCellEditor implements
		TableCellEditor {
	private static final long serialVersionUID = 1L;
	private JComboBox comboBox;
	private final String value;

	public ClientTypeTableCellEditor(String value) {
		this.value = value;
	}

	public Component getTableCellEditorComponent(JTable jtable, Object obj,
			boolean flag, int i, int j) {
		comboBox = new JComboBox();
		Font font = comboBox.getFont();
		comboBox.setFont(font.deriveFont(Font.PLAIN));
		
		ClientType[] clientTypes = ClientType.values();
		for (int k = 0; k < clientTypes.length; k++) {
			ClientType clientType = clientTypes[k];
			comboBox.addItem(clientType.getName());
			if (clientType == ClientType.getByName(value)) {
				comboBox.setSelectedItem(clientType.getName());
			}
			
		}
		return comboBox;
		
	}

	public Object getCellEditorValue() {
		String name = (String)comboBox.getSelectedItem();
		return ClientType.getByName(name).toString();
	}
}
