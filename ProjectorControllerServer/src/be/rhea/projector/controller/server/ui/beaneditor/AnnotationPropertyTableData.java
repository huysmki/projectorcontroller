package be.rhea.projector.controller.server.ui.beaneditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import be.rhea.projector.controller.server.ProjectorControllerServer;
import be.rhea.projector.controller.server.annotation.EditableProperty;
import be.rhea.projector.controller.server.annotation.EditableProperty.Type;
import be.rhea.projector.controller.server.scenario.Client;
import be.rhea.projector.controller.server.scenario.ClientType;

public class AnnotationPropertyTableData extends AbstractTableModel  {
	private static final long serialVersionUID = 1L;
	private final Object bean;
	private ArrayList<PropertyData> editableProperties;
	private final ArrayList<Client> clients;

	public AnnotationPropertyTableData(Object bean, ArrayList<Client> clients) {
		this.bean = bean;
		this.clients = clients;
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
				data.setAllowedClientType(annotation.allowedClientType());
				data.setField(field);
				
				editableProperties.add(data);
			}
		}
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
				ProjectorControllerServer.showError(e);
			} catch (IllegalAccessException e) {
				ProjectorControllerServer.showError(e);
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
			ProjectorControllerServer.showError(e);
		} catch (IllegalAccessException e) {
			ProjectorControllerServer.showError(e);
		}
		
	}

	@SuppressWarnings("unchecked")
	public String objToString(Object value) {
		if (value == null)
			return "Unknown";
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
		} else if (value instanceof ArrayList) {
			ArrayList<Integer> arrayList = (ArrayList<Integer>) value;
			StringBuilder builder = new StringBuilder();
			boolean first = true;
			for (Integer integer : arrayList) {
				if (!first) {
					builder.append(",");
				}
				builder.append(integer);
				first = false;
			}
			return builder.toString();
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
			} else if (name.equals("java.util.List")) {
				int[] i = strToInts(str);
				ArrayList<Integer> arrayList = new ArrayList<Integer>();
				for (int j = 0; j < i.length; j++) {
					int value = i[j];
					arrayList.add(value);
				}
				return arrayList;
			} else if (name.equals("be.rhea.projector.controller.server.scenario.ClientType")) {
				return ClientType.valueOf(str);
			}
			return null; // not supported
		} catch (Exception ex) {
			return null;
		}
	}

	public int[] strToInts(String str) throws Exception {
		StringTokenizer tokenizer = new StringTokenizer(str, ",");
		int[] i = new int[tokenizer.countTokens()];
		
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
			} else if (propertyData.getType() == Type.CLIENTS) {
				ClientsTableCellRenderer clientTableCellRenderer = new ClientsTableCellRenderer(Integer.parseInt((String) getValueAt(row, column)), clients);
				return clientTableCellRenderer;
			} else if (propertyData.getType() == Type.BOOLEAN) {
				BooleanTableCellRenderer booleanTableCellRenderer = new BooleanTableCellRenderer(Boolean.parseBoolean((String) getValueAt(row, column)));
				return booleanTableCellRenderer;
			} else if (propertyData.getType() == Type.CLIENT_TYPE) {
				ClientTypeTableCellRenderer booleanTableCellRenderer = new ClientTypeTableCellRenderer((String) getValueAt(row, column));
				return booleanTableCellRenderer;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public TableCellEditor getCellEditor(int row, int column) {
		if (column == 1) {
			PropertyData propertyData = editableProperties.get(row);
			String valueAt = (String) getValueAt(row, column);
			if (propertyData.getType() == Type.COLOR) {
				return (TableCellEditor) new ColorTableCellEditor((Color) stringToObj(valueAt, Color.class));
			} else if (propertyData.getType() == Type.FILE) {
				return (TableCellEditor) new FileTableCellEditor(valueAt);
			} else if (propertyData.getType() == Type.CLIENTS) {
				return (TableCellEditor) new ClientsTableCellEditor(Integer.parseInt(valueAt), clients, propertyData.getAllowedClientType());
			} else if (propertyData.getType() == Type.IP) {
				return (TableCellEditor) new IPTableCellEditor(valueAt);
			} else if (propertyData.getType() == Type.ARTNET) {
				return (TableCellEditor) new ArtNetTableCellEditor(((ArrayList<Integer>) stringToObj(valueAt, List.class)));
			} else if (propertyData.getType() == Type.BOOLEAN) {
				return (TableCellEditor) new BooleanTableCellEditor(Boolean.parseBoolean((String) getValueAt(row, column)));
			} else if (propertyData.getType() == Type.CLIENT_TYPE) {
				return (TableCellEditor) new ClientTypeTableCellEditor((String) getValueAt(row, column));
			}
		}
		return null;
	}

}
