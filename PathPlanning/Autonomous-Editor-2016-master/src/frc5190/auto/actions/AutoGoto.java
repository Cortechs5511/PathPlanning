package frc5190.auto.actions;

import frc5190.auto.AutoPoint;

public class AutoGoto extends AutoAction {

	private AutoPoint point = new AutoPoint();

	public double speed = 0;
	public double turnSpeed = 0;

	public AutoGoto(AutoPoint point) {
		super();
		this.point = point;
	}

	public AutoGoto() {
	}

	public AutoGoto(AutoGoto ag) {
		point = new AutoPoint(ag.getPoint());
		speed = ag.speed;
		turnSpeed = ag.turnSpeed;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public double getTurnSpeed() {
		return turnSpeed;
	}

	public void setTurnSpeed(double turnSpeed) {
		this.turnSpeed = turnSpeed;
	}

	public AutoPoint getPoint() {
		return point;
	}

	public void setPoint(AutoPoint point) {
		this.point = point;
	}

}
