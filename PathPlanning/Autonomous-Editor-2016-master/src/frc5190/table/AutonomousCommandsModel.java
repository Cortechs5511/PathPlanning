package frc5190.table;

import javax.swing.JComboBox;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

import frc5190.auto.AutoCommand;
import frc5190.auto.AutoPathPlanner;
import frc5190.auto.AutoPoint;

public class AutonomousCommandsModel extends AbstractTableModel {

	private AutoPathPlanner planner;

	public AutonomousCommandsModel(AutoPathPlanner planner) {
		this.planner = planner;
	}

	public void addTableModelListener(TableModelListener arg0) {

	}

	public Class<?> getColumnClass(int i) {
		return Double.class;
	}

	public int getColumnCount() {
		return 4;
	}

	public String getColumnName(int i) {
		return "";
	}

	public int getRowCount() {
		return planner.getCommands().size();
	}

	public Object getValueAt(int x, int y) {
		return planner.getCommands().get(x).toList()[y];
	}

	public boolean isCellEditable(int arg0, int arg1) {
		return true;
	}

	public void removeTableModelListener(TableModelListener arg0) {

	}

	public void setValueAt(Object value, int x, int y) {
		double val = (double) value;
		/*AutoPoint point = planner.getCurrentPoints().get(x);
		if (point == null) {
			point = new AutoPoint();
			planner.getCurrentPoints().add(point);
		}
		if (y == 1) {
			point.setX(val);
		}
		if (y == 2) {
			point.setY(val);
		}*/
	}

}
