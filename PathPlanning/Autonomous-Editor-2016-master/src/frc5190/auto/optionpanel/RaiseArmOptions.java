package frc5190.auto.optionpanel;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import frc5190.AutoEditorWindow;
import frc5190.auto.actions.AutoGoto;
import frc5190.auto.actions.AutoRaiseArm;
import net.miginfocom.swing.MigLayout;

public class RaiseArmOptions extends JPanel {

	private AutoRaiseArm action;

	public RaiseArmOptions(AutoEditorWindow window, AutoRaiseArm action) {
		this.action = action;
		setLayout(new MigLayout("", "[fill][grow,fill]", "[]"));

		JLabel lblArmSpeed = new JLabel("Arm Speed");
		add(lblArmSpeed, "flowx,cell 0 0");

		JSpinner straightSpeedSpinner = new JSpinner();
		straightSpeedSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent paramChangeEvent) {
				action.setSpeed((double) straightSpeedSpinner.getValue());
				window.fireUpdate();
			}
		});
		straightSpeedSpinner.setModel(new SpinnerNumberModel(new Double(0), null, null, new Double(1)));
		add(straightSpeedSpinner, "cell 1 0");
		straightSpeedSpinner.setValue(action.getSpeed());
	}

}
