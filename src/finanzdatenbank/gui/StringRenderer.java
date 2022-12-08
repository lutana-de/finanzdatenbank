package finanzdatenbank.gui;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Matthias Mohr
 */
public class StringRenderer extends DefaultTableCellRenderer {
	
	public StringRenderer() {
		super();
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		c.setFont(BibFont.getBold());
		int lastRow = table.getModel().getRowCount() - 1;
		if (row == lastRow) {
			c.setBackground(new Color(244,244,244));
			c.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		else {
			c.setBackground(new Color(255,255,255));
			c.setHorizontalAlignment(SwingConstants.LEFT);
		}
		return c;
	}
	
}
