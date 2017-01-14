package frc5190.auto.cmds;

import frc5190.auto.AutoCommand;

public class StraightCommand extends AutoCommand {

	private int distance = 0;
	private double speed = 0;

	public StraightCommand(int distance, double speed) {
		super();
		this.distance = distance;
		this.speed = speed;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	@Override
	public double[] toList() {
		return new double[] { 1, distance, speed, 0 };
	}

}
