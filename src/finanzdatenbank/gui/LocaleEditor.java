package finanzdatenbank.gui;

import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class LocaleEditor extends DefaultCellEditor {

	private Object value;

	public LocaleEditor(JTextField field) {
		super(field);
	}

	@Override
	public Object getCellEditorValue() {
		return value;
	}

	@Override
	public boolean stopCellEditing() {
		try {
			value = null;
			String editingValue = (String) super.getCellEditorValue();
			if (editingValue != null) {
				editingValue = editingValue.trim().replace(',', '.');
				if (editingValue.length() > 0) {
					Float f = Float.valueOf(editingValue) * 100;
					value = f.intValue();
				}
			}
		} catch (NumberFormatException exception) {
			return false;
		}

		return super.stopCellEditing();
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		Component c = super.getTableCellEditorComponent(table, value, isSelected, row, column);

		JTextField textField = (JTextField) c;
		textField.setHorizontalAlignment(SwingConstants.RIGHT);
		if (value != null) {
			Integer cent = (Integer) value;
			Integer euro = cent / 100;
			textField.setText(String.valueOf(euro).replace('.', ','));
		}
		else {
			textField.setText("");
		}

		return c;
	}
}
