package frc5190.auto;

public class AutoPoint {

	public double x;
	public double y;

	public double shootTurn = 0;
	public double armSpeed;

	public AutoPoint() {

	}

	public AutoPoint(AutoPoint point) {
		x = point.x;
		y = point.y;
	}

	public AutoPoint(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getShootTurn() {
		return shootTurn;
	}

	public void setShootTurn(double shootTurn) {
		this.shootTurn = shootTurn;
	}

	public double getArmSpeed() {
		return armSpeed;
	}

	public void setArmSpeed(double armSpeed) {
		this.armSpeed = armSpeed;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public AutoPoint clone() {
		return new AutoPoint(this);
	}

	public double distance(AutoPoint point) {
		double xa = point.x - x;
		double ya = point.y - y;
		xa = xa * xa;
		ya = ya * ya;
		return Math.sqrt(xa + ya);
	}

	public AutoPoint average(AutoPoint point) {
		AutoPoint avg = new AutoPoint();
		avg.x = (point.x + x) / 2d;
		avg.y = (point.y + y) / 2d;
		return avg;
	}

}
