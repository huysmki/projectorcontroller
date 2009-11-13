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
	
//	I think there are two cases.  In one, the JTable itself has the focus (I think this happens when the 
//	use tabs to the JTable or editing is complete).  A simple addition of a focus listener to the 
//	JTable can tell you when focus lost occurs & editing is happening - 
//	add the focusLost listener & check the event for editingColumn & editingRow != -1.
//
//	When you double-click on the JTable, the editor Component comes up.  This component doesn't 
//	have an event listener associated with it.  I think you'll need to extend the default CellEditor and 
//	add a listener to it, and use this as the editor for you JTable rather than the default.	
	
	@Override
	public TableCellEditor getCellEditor(int i, int j) {
		TableCellEditor cellEditor = data.getCellEditor(i, j);
		if (cellEditor != null) {
			return cellEditor;
		}
		TableCellEditor defaultEditor = super.getCellEditor(i, j);
		return defaultEditor;
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
