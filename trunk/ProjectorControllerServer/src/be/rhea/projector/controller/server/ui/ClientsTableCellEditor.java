package be.rhea.projector.controller.server.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

import be.rhea.projector.controller.server.scenario.Client;

public class ClientsTableCellEditor extends AbstractCellEditor implements
		TableCellEditor {
	private static final long serialVersionUID = 1L;
	private final int clientId;
	private final List<Client> clients;
	private JComboBox comboBox;

	public ClientsTableCellEditor(int clientId, List<Client> clients) {
		this.clientId = clientId;
		this.clients = clients;
	}

	@Override
	public Component getTableCellEditorComponent(JTable jtable, Object obj,
			boolean flag, int i, int j) {
		comboBox = new JComboBox();
		for (Client client : clients) {
			comboBox.addItem(client);
			if (client.getId() == clientId) {
				comboBox.setSelectedItem(client);
			}
		}
		return comboBox;
	}

	@Override
	public Object getCellEditorValue() {
		return ((Client)comboBox.getSelectedItem()).getId();
	}
}
