package be.rhea.projector.controller.server.ui;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class BeanEditor extends JPanel {
	private static final long serialVersionUID = -3986474952940035967L;
	protected PropertyTable table;
	protected AnnotationPropertyTableData data;

	public BeanEditor() {
		this.setLayout(new BorderLayout());
		this.setBounds(10,10, 300, 300);
		table = new PropertyTable();
		JScrollPane ps = new JScrollPane();
		ps.getViewport().add(table);
		this.add(ps, BorderLayout.CENTER);
		setVisible(true);
	}
	
	public void setObject(Object bean) {
		data = new AnnotationPropertyTableData(bean, table);
		table.setPropertyTableData(data);
	}

	public void propertyChange(PropertyChangeEvent evt) {
		data.setProperty(evt.getPropertyName(), evt.getNewValue());
	}

	
}

