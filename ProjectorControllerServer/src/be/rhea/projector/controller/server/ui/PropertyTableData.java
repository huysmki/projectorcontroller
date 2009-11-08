package be.rhea.projector.controller.server.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;
import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;

public class PropertyTableData extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	protected String[][] m_properties;
	protected int m_numProps = 0;
	protected Vector m_v;
	private final Object bean;
	private final JTable table;

	public PropertyTableData(Object bean, JTable table) {
		this.bean = bean;
		this.table = table;
		try {
			BeanInfo info = Introspector.getBeanInfo(bean.getClass());
			BeanDescriptor descr = info.getBeanDescriptor();
			PropertyDescriptor[] props = info.getPropertyDescriptors();
			m_numProps = props.length;
			m_v = new Vector(m_numProps);
			for (int k = 0; k < m_numProps; k++) {
				String name = props[k].getDisplayName();
				boolean added = false;
				for (int i = 0; i < m_v.size(); i++) {
					String str = ((PropertyDescriptor) m_v.elementAt(i))
							.getDisplayName();
					if (name.compareToIgnoreCase(str) < 0) {
						m_v.insertElementAt(props[k], i);
						added = true;
						break;
					}
				}
				if (!added)
					m_v.addElement(props[k]);
			}
			m_properties = new String[m_numProps][2];
			for (int k = 0; k < m_numProps; k++) {
				PropertyDescriptor prop = (PropertyDescriptor) m_v
						.elementAt(k);
				m_properties[k][0] = prop.getDisplayName();
				Method mRead = prop.getReadMethod();
				if (mRead != null && mRead.getParameterTypes().length == 0) {
					Object value = mRead.invoke(bean, null);
					m_properties[k][1] = objToString(value);
				} else
					m_properties[k][1] = "error";
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane
					.showMessageDialog(null, "Error: "
							+ ex.toString(), "Warning",
							JOptionPane.WARNING_MESSAGE);
		}
	}

	public void setProperty(String name, Object value) {
		for (int k = 0; k < m_numProps; k++)
			if (name.equals(m_properties[k][0])) {
				m_properties[k][1] = objToString(value);
				table.tableChanged(new TableModelEvent(this, k));
				table.repaint();
				break;
			}
	}

	public int getRowCount() {
		return m_numProps;
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
		if (nRow < 0 || nRow >= getRowCount())
			return "";
		switch (nCol) {
		case 0:
			return m_properties[nRow][0];
		case 1:
			return m_properties[nRow][1];
		}
		return "";
	}

	public void setValueAt(Object value, int nRow, int nCol) {
		if (nRow < 0 || nRow >= getRowCount())
			return;
		String str = value.toString();
		PropertyDescriptor prop = (PropertyDescriptor) m_v.elementAt(nRow);
		Class cls = prop.getPropertyType();
		Object obj = stringToObj(str, cls);
		if (obj == null)
			return; // can't process
		Method mWrite = prop.getWriteMethod();
		if (mWrite == null || mWrite.getParameterTypes().length != 1)
			return;
		try {
			mWrite.invoke(bean, new Object[] { obj });
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane
					.showMessageDialog(null, "Error: "
							+ ex.toString(), "Warning",
							JOptionPane.WARNING_MESSAGE);
		}
		m_properties[nRow][1] = str;
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

	public Object stringToObj(String str, Class cls) {
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
}
