package be.rhea.projector.controller.server.ui.beaneditor;

import java.awt.Component;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.AbstractCellEditor;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

public class IPTableCellEditor extends AbstractCellEditor implements
		TableCellEditor {
	private static final long serialVersionUID = 1L;
	private String value;
	private JFormattedTextField textField;

	public IPTableCellEditor(String value) {
		this.value = value;
	}

	@Override
	public Component getTableCellEditorComponent(JTable jtable, Object obj,
			boolean flag, int i, int j) {
		textField = new JFormattedTextField(value);
		textField.setInputVerifier(new InputVerifier() {
            Pattern pat = Pattern.compile("\\b(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\."+
                    "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                    "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                    "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b");
            public boolean shouldYieldFocus(JComponent input) {
                boolean inputOK = verify(input);
                if (inputOK) {
                    return true;
                } else {
                    return false;
                }
            }
            public boolean verify(JComponent input) {
                JTextField field = (JTextField) input;
                Matcher m = pat.matcher(field.getText());
                return m.matches();
            }
        });
		return textField;
	}
	
	@Override
	public boolean stopCellEditing() {
		Pattern pat = Pattern.compile("\\b(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\."+
                "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b");
		boolean matches = pat.matcher(textField.getText()).matches();
		if (matches) {
			return super.stopCellEditing();
		}
		return false;
	}
	
	@Override
	public Object getCellEditorValue() {
		return textField.getText();
	}
}
