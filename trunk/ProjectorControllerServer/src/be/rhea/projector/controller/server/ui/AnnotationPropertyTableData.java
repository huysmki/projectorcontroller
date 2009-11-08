package be.rhea.projector.controller.server.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import be.rhea.projector.controller.server.annotation.EditableProperty;
import be.rhea.projector.controller.server.annotation.EditableProperty.Type;

public class AnnotationPropertyTableData extends AbstractTableModel  {
	private static final long serialVersionUID = 1L;
	private final Object bean;
	private final JTable table;
	private ArrayList<PropertyData> editableProperties;

	public AnnotationPropertyTableData(Object bean, JTable table) {
		this.bean = bean;
		this.table = table;
		editableProperties = new ArrayList<PropertyData>(); 

		Class<? extends Object> clazz = bean.getClass();
		addPropertyDataToList(clazz);
	
	}

	private void addPropertyDataToList(Class<? extends Object> clazz) {
		Class<?> superclass = clazz.getSuperclass();
		if (!superclass.equals(Object.class)) {
			addPropertyDataToList(superclass);
		}
		Field[] fields = clazz.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			if (field.isAnnotationPresent(EditableProperty.class)) {
				EditableProperty annotation = field.getAnnotation(EditableProperty.class);
				PropertyData data = new PropertyData();
				data.setName(annotation.name());
				data.setType(annotation.type());
				data.setField(field);
				editableProperties.add(data);
			}
		}
	}

	public void setProperty(String name, Object value) {
//		for (int k = 0; k < m_numProps; k++)
//			if (name.equals(m_properties[k][0])) {
//				m_properties[k][1] = objToString(value);
//				table.tableChanged(new TableModelEvent(this, k));
//				table.repaint();
//				break;
//			}
	}

	public int getRowCount() {
		return editableProperties.size();
	}

	public int getColumnCount() {
		return 2;
	}

	public String getColumnName(int nCol) {
		return nCol == 0 ? "Property" : "Value";
	}

	public boolean isCellEditable(int nRow, int nCol) {
		return (nCol == 1);
	}

	public Object getValueAt(int nRow, int nCol) {
		if (nRow < 0 || nRow >= getRowCount()) {
			return "";
		}
		PropertyData data = editableProperties.get(nRow);
		switch (nCol) {
		case 0:
			return data.getName();
		case 1:
			Field field = data.getField();
			field.setAccessible(true);
			try {
				return objToString(field.get(bean));
			} catch (IllegalArgumentException e) {
				//TODO error handling
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				//TODO error handling
				e.printStackTrace();
			}
		}
		return "";
	}

	public void setValueAt(Object value, int nRow, int nCol) {
		if (nRow < 0 || nRow >= getRowCount())
			return;
		String str = value.toString();
		PropertyData data = editableProperties.get(nRow);
		Field field = data.getField();
		field.setAccessible(true);
		try {
			field.set(bean, stringToObj(str, field.getType()));
		} catch (IllegalArgumentException e) {
			//TODO error handling
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			//TODO error handling
			e.printStackTrace();
		}
		
	}

	public String objToString(Object value) {
		if (value == null)
			return "null";
		if (value instanceof Dimension) {
			Dimension dim = (Dimension) value;
			return "" + dim.width + "," + dim.height;
		} else if (value instanceof Insets) {
			Insets ins = (Insets) value;
			return "" + ins.left + "," + ins.top + "," + ins.right + ","
					+ ins.bottom;
		} else if (value instanceof Rectangle) {
			Rectangle rc = (Rectangle) value;
			return "" + rc.x + "," + rc.y + "," + rc.width + ","
					+ rc.height;
		} else if (value instanceof Color) {
			Color col = (Color) value;
			return "" + col.getRed() + "," + col.getGreen() + ","
					+ col.getBlue();
		}
		return value.toString();
	}

	public Object stringToObj(String str, Class<?> cls) {
		try {
			if (str == null)
				return null;
			String name = cls.getName();
			if (name.equals("java.lang.String"))
				return str;
			else if (name.equals("int"))
				return new Integer(str);
			else if (name.equals("long"))
				return new Long(str);
			else if (name.equals("float"))
				return new Float(str);
			else if (name.equals("double"))
				return new Double(str);
			else if (name.equals("boolean"))
				return new Boolean(str);
			else if (name.equals("java.awt.Dimension")) {
				int[] i = strToInts(str);
				return new Dimension(i[0], i[1]);
			} else if (name.equals("java.awt.Insets")) {
				int[] i = strToInts(str);
				return new Insets(i[0], i[1], i[2], i[3]);
			} else if (name.equals("java.awt.Rectangle")) {
				int[] i = strToInts(str);
				return new Rectangle(i[0], i[1], i[2], i[3]);
			} else if (name.equals("java.awt.Color")) {
				int[] i = strToInts(str);
				return new Color(i[0], i[1], i[2]);
			}
			return null; // not supported
		} catch (Exception ex) {
			return null;
		}
	}

	public int[] strToInts(String str) throws Exception {
		int[] i = new int[4];
		StringTokenizer tokenizer = new StringTokenizer(str, ",");
		for (int k = 0; k < i.length && tokenizer.hasMoreTokens(); k++)
			i[k] = Integer.parseInt(tokenizer.nextToken());
		return i;
	}

	public TableCellRenderer getCellRenderer(int row, int column) {
		if (column == 1) {
			PropertyData propertyData = editableProperties.get(row);
			if (propertyData.getType() == Type.COLOR) {
				DefaultTableCellRenderer defaultTableCellRenderer = new DefaultTableCellRenderer();
				defaultTableCellRenderer.setBackground((Color) stringToObj((String) getValueAt(row, column), Color.class));
				return defaultTableCellRenderer;
			}
		}
		return null;
	}

	public TableCellEditor getCellEditor(int row, int column) {
		if (column == 1) {
			PropertyData propertyData = editableProperties.get(row);
			if (propertyData.getType() == Type.COLOR) {
				return (TableCellEditor) new ButtonTableCellEditor((Color) stringToObj((String) getValueAt(row, column), Color.class));
			}
		}
		return null;
	}

}
