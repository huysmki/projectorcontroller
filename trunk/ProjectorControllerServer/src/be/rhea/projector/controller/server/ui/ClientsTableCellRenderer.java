package be.rhea.projector.controller.server.ui;

import java.awt.Component;
import java.util.List;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellRenderer;

import be.rhea.projector.controller.server.scenario.Client;

public class ClientsTableCellRenderer extends AbstractCellEditor implements
		TableCellRenderer{
	private static final long serialVersionUID = 1L;
	private final int clientId;
	private final List<Client> clients;
	private JTextField textField;

	public ClientsTableCellRenderer(int clientId, List<Client> clients) {
		this.clientId = clientId;
		this.clients = clients;
	}

	@Override
	public Object getCellEditorValue() {
		return clientId;
	}

	@Override
	public Component getTableCellRendererComponent(JTable jtable, Object obj,
			boolean flag, boolean flag1, int i, int j) {
		textField = new JTextField();
		textField.setEditable(false);
		for (Client client : clients) {
			if (client.getId() == clientId) {
				textField.setText(client.toString());
			}
		}
		return textField;

	}
}
