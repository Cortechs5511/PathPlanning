package frc5190.auto.cmds;

import frc5190.auto.AutoCommand;

public class TurnCommand extends AutoCommand {

	private double angle = 0;
	private double speed = 0;

	public TurnCommand(double angle, double speed) {
		super();
		this.angle = angle;
		this.speed = speed;
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	@Override
	public double[] toList() {
		return new double[] { 2, angle, speed, 0 };
	}

}
