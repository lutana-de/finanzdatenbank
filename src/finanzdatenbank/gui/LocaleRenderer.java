package finanzdatenbank.gui;

import java.awt.Color;
import java.awt.Component;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Matthias Mohr
 */
public class LocaleRenderer extends DefaultTableCellRenderer {
	
	public LocaleRenderer() {
		super();
		setHorizontalAlignment(SwingConstants.RIGHT);
	}

	@Override
	protected void setValue(Object value) {
		if (value != null) {
			Integer i = (Integer) value;
			Float f = i / 100.0f;
			NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.GERMANY);
			super.setValue(nf.format(f));
		}
		else {
			super.setValue("");
		}
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		if (row == table.getModel().getRowCount()-1 || column == BibTableModel.SUM_COL) {
			c.setFont(BibFont.getBold());
			c.setBackground(new Color(244,244,244));
		}
		else {
			c.setFont(BibFont.getRegular());
			c.setBackground(new Color(255,255,255));
		}
		return c;
	}
	
}
