package frc5190.table;

import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

import frc5190.auto.AutoPathPlanner;
import frc5190.auto.AutoPoint;

public class AutonomousPointsModel extends AbstractTableModel {

	private AutoPathPlanner planner;

	public AutonomousPointsModel(AutoPathPlanner planner) {
		this.planner = planner;
	}

	public void addTableModelListener(TableModelListener arg0) {

	}

	public Class<?> getColumnClass(int i) {
		return Double.class;
	}

	public int getColumnCount() {
		return 3;
	}

	public String getColumnName(int i) {
		return "";
	}

	public int getRowCount() {
		return planner.getAutoPoints().size();
	}

	public Object getValueAt(int x, int y) {
		AutoPoint point = planner.getAutoPoints().get(x);
		switch (y) {
		case 0:
			return x;
		case 1:
			return point.getX();
		case 2:
			return point.getY();
		}
		return null;
	}

	public boolean isCellEditable(int x, int y) {
		return y != 0;
	}

	public void removeTableModelListener(TableModelListener arg0) {

	}

	public void setValueAt(Object value, int x, int y) {
		double val = (double) value;
		AutoPoint point = planner.getAutoPoints().get(x);
		switch (y) {
		case 1:
			point.setX(val);
			break;
		case 2:
			point.setY(val);
			break;
		}
	}

}
