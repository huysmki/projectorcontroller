package be.rhea.projector.controller.server.ui;

import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public class PropertyTable extends JTable {
	private static final long serialVersionUID = 1L;
	private AnnotationPropertyTableData data;

	public void setPropertyTableData(AnnotationPropertyTableData data) {
		this.data = data;
		this.setModel(data);
	}
	
	@Override
	public TableCellEditor getCellEditor(int i, int j) {
		TableCellEditor cellEditor = data.getCellEditor(i, j);
		if (cellEditor != null) {
			return cellEditor;
		}
		return super.getCellEditor(i, j);
	}
	
	@Override
	public TableCellRenderer getCellRenderer(int i, int j) {
		TableCellRenderer cellRenderer = data.getCellRenderer(i, j);
		if (cellRenderer != null) {
			return cellRenderer;
		}
		return super.getCellRenderer(i, j);
	}
}
