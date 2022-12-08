package finanzdatenbank.gui;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import finanzdatenbank.BibDatabase;

public class BibTableChangeListener implements TableModelListener {

	public void tableChanged(TableModelEvent e) {
		int row = e.getFirstRow();
		int column = e.getColumn();
		BibTableModel model = (BibTableModel) e.getSource();
		if (row == model.getRowCount()-1) {
			return;
		}
		if (e.getType() == TableModelEvent.UPDATE) {
			Integer id = (Integer) model.getValueAt(row, BibTableModel.ID_COL);

			BibDatabase db = BibDatabase.get();
			if (column == BibTableModel.TITLE_COL) {
				String data = (String) model.getValueAt(row, column);
				db.updateTitle(id, data);
			}
			else if (column != BibTableModel.SUM_COL) {
				try {
					Integer data = (Integer) model.getValueAt(row, column);
					if (column == BibTableModel.OTHER_COL) {
						db.updateOther(id, data);
					}
					else {
						db.updateMonth(id, column, data);
					}
					model.setValueAt(model.sumRow(row), row, BibTableModel.SUM_COL);
					model.setValueAt(model.sumCol(row), model.getRowCount()-1, column);
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}
}
