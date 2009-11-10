package be.rhea.projector.controller.server.ui;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import be.rhea.projector.controller.server.scenario.Client;

public class BeanEditor extends JPanel {
	private static final long serialVersionUID = -3986474952940035967L;
	protected PropertyTable table;
	protected AnnotationPropertyTableData data;

	public BeanEditor() {
		this.setLayout(new BorderLayout());
		this.setBounds(10, 10, 300, 300);
		table = new PropertyTable();
		JScrollPane ps = new JScrollPane();
		ps.getViewport().add(table);
		this.add(ps, BorderLayout.CENTER);
		setVisible(true);
	}

	public void setObject(Object bean, ArrayList<Client> clients) {
		data = new AnnotationPropertyTableData(bean, table, clients);
		table.setPropertyTableData(data);
	}

	public void propertyChange(PropertyChangeEvent evt) {
		data.setProperty(evt.getPropertyName(), evt.getNewValue());
	}
}
