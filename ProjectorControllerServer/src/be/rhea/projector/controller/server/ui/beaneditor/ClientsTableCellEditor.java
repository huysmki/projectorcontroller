package be.rhea.projector.controller.server.ui.beaneditor;

import java.awt.Component;
import java.awt.Font;
import java.util.List;

import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import be.rhea.projector.controller.server.scenario.Client;
import be.rhea.projector.controller.server.scenario.ClientType;

public class ClientsTableCellEditor extends AbstractCellEditor implements
		TableCellEditor {
	private static final long serialVersionUID = 1L;
	private final int clientId;
	private final List<Client> clients;
	private JComboBox comboBox;
	private final ClientType allowedClientType;

	public ClientsTableCellEditor(int clientId, List<Client> clients, ClientType allowedClientType) {
		this.clientId = clientId;
		this.clients = clients;
		this.allowedClientType = allowedClientType;
	}

	public Component getTableCellEditorComponent(JTable jtable, Object obj,
			boolean flag, int i, int j) {
		comboBox = new JComboBox();
		Font font = comboBox.getFont();
		comboBox.setFont(font.deriveFont(Font.PLAIN));
		
		for (Client client : clients) {
			if (client.getType() == allowedClientType) {
				comboBox.addItem(client);
				if (client.getId() == clientId) {
					comboBox.setSelectedItem(client);
				}
			}
		}
		return comboBox;
	}

	public Object getCellEditorValue() {
		Client selectedClient = (Client)comboBox.getSelectedItem();
		if (selectedClient == null) {
			return 0;
		} else {
			return selectedClient.getId();
		}
	}
}
