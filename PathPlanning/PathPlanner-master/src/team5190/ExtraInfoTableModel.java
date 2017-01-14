package team5190;

import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

public class ExtraInfoTableModel extends AbstractTableModel {

	private String[] col = new String[] { "Variable", "Value" };

	private ExtraInfo extraInfo;

	private String[][] data;

	public ExtraInfoTableModel(ExtraInfo extraInfo) {
		this.extraInfo = extraInfo;
		data = extraInfo.path.planner.extraData();
	}

	public void addTableModelListener(TableModelListener arg0) {

	}

	public Class<?> getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	public int getColumnCount() {
		return col.length;
	}

	public String getColumnName(int c) {
		return col[c];
	}

	public int getRowCount() {
		data = extraInfo.path.planner.extraData();
		return data.length;
	}

	public Object getValueAt(int row, int col) {
		data = extraInfo.path.planner.extraData();
		return data[row][col];
	}

	public boolean isCellEditable(int row, int col) {
		return false;
	}

	public void removeTableModelListener(TableModelListener arg0) {

	}

	public void setValueAt(Object arg0, int arg1, int arg2) {

	}

}
