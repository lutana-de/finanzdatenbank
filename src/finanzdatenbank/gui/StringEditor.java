/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package finanzdatenbank.gui;

import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class StringEditor extends DefaultCellEditor {

	public StringEditor(JTextField field) {
		super(field);
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		Component c = super.getTableCellEditorComponent(table, value, isSelected, row, column);
		JTextField textField = (JTextField) c;
		textField.setHorizontalAlignment(SwingConstants.LEFT);
		textField.setFont(BibFont.getBold());
		return c;
	}
}
