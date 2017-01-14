package frc5190.auto.cmds;

import frc5190.auto.AutoCommand;

public class ArmUpCommand extends AutoCommand {

	private double speed = 0;

	public ArmUpCommand(double speed) {
		super();
		this.speed = speed;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	@Override
	public double[] toList() {
		return new double[] { 4, speed, 0, 0 };
	}

}
