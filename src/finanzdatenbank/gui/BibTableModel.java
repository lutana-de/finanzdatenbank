package finanzdatenbank.gui;

import java.util.Vector;
import javax.swing.table.DefaultTableModel;

public class BibTableModel extends DefaultTableModel {
	
	private BibGUI2 gui;
	
	public static final int TITLE_COL = 0;
	public static final int FIRST_MONTH_COL = 1;
	public static final int SUM_COL = 13;
	public static final int OTHER_COL = 14;
	public static final int ID_COL = 15;
	
	public BibTableModel(BibGUI2 gui) {
		this.gui = gui;
		addColumn("Rechnung");
		addColumn("Januar");
		addColumn("Februar");
		addColumn("März");
		addColumn("April");
		addColumn("Mai");
		addColumn("Juni");
		addColumn("Juli");
		addColumn("August");
		addColumn("September");
		addColumn("Oktober");
		addColumn("November");
		addColumn("Dezember");
		addColumn("Summe");
		addColumn("Nachzahlung/Gutschrift");
		addColumn("ID");
	}

	public Class getColumnClass(int columnIndex) {
		switch(columnIndex) {
			case TITLE_COL:
				return String.class;
			case ID_COL:
				return String.class;
			default: 
				return Integer.class;
		}
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return !this.gui.isReadOnly() && columnIndex != ID_COL && columnIndex != SUM_COL && rowIndex != this.getRowCount()-1;
	}
	
	public int getColumnPreferredWidth(int columnIndex) {
		switch(columnIndex) {
			case TITLE_COL:
				return 100;
			case ID_COL:
				return 0;
			default: 
				return 20;
		}
	}
	
	public int getColumnMinWidth(int columnIndex) {
		switch(columnIndex) {
			case ID_COL:
				return 0;
			default:
				return 10;
		}
	}
	
	public int getColumnMaxWidth(int columnIndex) {
		switch(columnIndex) {
			case ID_COL:
				return 0;
			case TITLE_COL:
				return 500;
			default: 
				return 200;
		}
	}
	
	@Override
	public Object getValueAt(int row, int col) {
		if (col == SUM_COL) {
			return this.sumRow(row);
		}
		else if (row == this.getRowCount()-1) {
			if (col == TITLE_COL) {
				return "Summe";
			}
			else if (col == ID_COL) {
				return null;
			}
			return this.sumCol(col);
		}
		else {
			return super.getValueAt(row, col);
		}
	}
	
	public int sumRow(int row) {
		int sum = 0;
		for(int i = 0; i < 12; i++) {
			Integer value = (Integer) this.getValueAt(row, BibTableModel.FIRST_MONTH_COL + i);
			if (value != null) {
				sum += value;
			}
		}
		return sum;
	}
	
	
	public int sumCol(int col) {
		int sum = 0;
		for(int i = 0; i < this.getRowCount()-1; i++) {
			Integer value = (Integer) this.getValueAt(i, col);
			if (value != null) {
				sum += value;
			}
		}
		return sum;
	}
	
}
