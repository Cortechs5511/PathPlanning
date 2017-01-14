package frc5190.auto.cmds;

import frc5190.auto.AutoCommand;

public class ShootCommand extends AutoCommand {

	public ShootCommand() {
		super();
	}

	@Override
	public double[] toList() {
		return new double[] { 5, 0, 0, 0 };
	}

}
