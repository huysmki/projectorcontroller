package be.rhea.projector.controller.server.ui;

import java.awt.Component;
import java.util.List;

import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import be.rhea.projector.controller.server.scenario.Client;

public class ClientsTableCellRenderer extends AbstractCellEditor implements
		TableCellRenderer{
	private static final long serialVersionUID = 1L;
	private final int clientId;
	private final List<Client> clients;
	private JComboBox comboBox;

	public ClientsTableCellRenderer(int clientId, List<Client> clients) {
		this.clientId = clientId;
		this.clients = clients;
	}

	@Override
	public Object getCellEditorValue() {
		return ((Client)comboBox.getSelectedItem()).getId();
	}

	@Override
	public Component getTableCellRendererComponent(JTable jtable, Object obj,
			boolean flag, boolean flag1, int i, int j) {
		comboBox = new JComboBox();
		comboBox.setEditable(false);
		for (Client client : clients) {
			comboBox.addItem(client);
			if (client.getId() == clientId) {
				comboBox.setSelectedItem(client);
			}
		}
		return comboBox;

	}
}
