package frc5190.auto.optionpanel;

import java.awt.Color;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import frc5190.AutoEditorWindow;
import frc5190.auto.AutoPoint;
import frc5190.auto.actions.AutoGoto;
import net.miginfocom.swing.MigLayout;

public class TurnOptions extends JPanel {

	private JSpinner speedSpinner;

	private AutoGoto point;

	public TurnOptions(AutoEditorWindow window, AutoGoto point) {
		setBackground(Color.LIGHT_GRAY);
		this.point = point;
		setLayout(new MigLayout("", "[fill][grow,fill]", "[]"));

		JLabel lblTurnSpeed = new JLabel("Turn Speed");
		add(lblTurnSpeed, "cell 0 0");
		
				JSpinner turnSpeedSpinner = new JSpinner();
				turnSpeedSpinner.setModel(new SpinnerNumberModel(new Double(0), null, null, new Double(1)));
				turnSpeedSpinner.addChangeListener(new ChangeListener() {
					public void stateChanged(ChangeEvent e) {
						point.setTurnSpeed((double) turnSpeedSpinner.getValue());
						window.fireUpdate();
					}
				});
				add(turnSpeedSpinner, "cell 1 0");
				turnSpeedSpinner.setValue(point.getTurnSpeed());
	}

}
