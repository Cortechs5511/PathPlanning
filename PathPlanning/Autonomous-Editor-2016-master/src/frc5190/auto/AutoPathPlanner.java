package frc5190.auto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import frc5190.AutoEditorWindow;
import frc5190.auto.actions.AutoAction;
import frc5190.auto.actions.AutoGoto;
import frc5190.auto.actions.AutoLowerArm;
import frc5190.auto.actions.AutoRaiseArm;
import frc5190.auto.actions.AutoShoot;
import frc5190.auto.actions.AutoTurn;
import frc5190.auto.cmds.ArmDownCommand;
import frc5190.auto.cmds.ArmUpCommand;
import frc5190.auto.cmds.ShootCommand;
import frc5190.auto.cmds.StraightCommand;
import frc5190.auto.cmds.TurnCommand;
import frc5190.auto.optionpanel.GotoOptions;

public class AutoPathPlanner {

	private AutoGoto robotStartPos = new AutoGoto();
	private double robotStartRotation = 0;
	private ArrayList<AutoAction> autoActions = new ArrayList<AutoAction>();

	private AutoEditorWindow window;

	public AutoPathPlanner(AutoEditorWindow window) {
		this.window = window;
	}

	public AutoGoto getRobotStartPos() {
		return robotStartPos;
	}

	public void setRobotStartPos(AutoGoto robotStartPos) {
		this.robotStartPos = robotStartPos;
	}

	public ArrayList<AutoAction> getAutoActions() {
		return autoActions;
	}

	public void setAutoActions(ArrayList<AutoAction> autoActions) {
		this.autoActions = autoActions;
	}

	public AutoEditorWindow getWindow() {
		return window;
	}

	public void setWindow(AutoEditorWindow window) {
		this.window = window;
	}

	public ArrayList<AutoCommand> getCommands() {
		ArrayList<AutoCommand> commands = new ArrayList<AutoCommand>();
		double currentRotation = robotStartRotation;
		while (currentRotation >= 180) {
			currentRotation -= 360;
		}
		while (currentRotation <= -180) {
			currentRotation += 360;
		}
		AutoPoint lastPoint = robotStartPos.getPoint();
		double currentX = lastPoint.x;
		double currentY = lastPoint.y;
		for (AutoAction action : autoActions) {
			if (action instanceof AutoGoto) {
				AutoGoto auto = (AutoGoto) action;
				AutoPoint point = auto.getPoint();
				double angle = getAngle(new AutoPoint(currentX, currentY), point) - currentRotation;
				while (angle >= 180) {
					angle -= 360;
				}
				while (angle <= -180) {
					angle += 360;
				}
				double distance = point.distance(new AutoPoint(currentX, currentY));
				commands = addCommands(lastPoint, commands);
				if (angle != 0) {
					commands.add(new TurnCommand(angle,auto.getTurnSpeed()));
				}
				commands.add(new StraightCommand((int) distance,auto.getSpeed()));

				currentRotation += angle;
				while (currentRotation >= 180) {
					currentRotation -= 360;
				}
				while (currentRotation <= -180) {
					currentRotation += 360;
				}
				currentX += Math.cos(Math.toRadians(currentRotation)) * distance;
				currentY += Math.sin(Math.toRadians(currentRotation)) * distance;
				lastPoint = point;
			} else if (action instanceof AutoShoot) {
				commands.add(new ShootCommand());
			} else if (action instanceof AutoLowerArm) {
				commands.add(new ArmDownCommand(((AutoLowerArm) action).getSpeed()));
			} else if (action instanceof AutoRaiseArm) {
				commands.add(new ArmUpCommand(((AutoRaiseArm) action).getSpeed()));
			}
		}
		commands = addCommands(lastPoint, commands);
		return commands;
	}

	private ArrayList<AutoCommand> addCommands(AutoPoint lastPoint, ArrayList<AutoCommand> commands) {
		return commands;
	}

	private double getAngle(AutoPoint current, AutoPoint target) {
		float angle = (float) Math.toDegrees(Math.atan2(target.y - current.y, target.x - current.x));

		return angle;
	}

	public double getRobotStartRotation() {
		return robotStartRotation;
	}

	public void setRobotStartRotation(double robotStartRotation) {
		this.robotStartRotation = robotStartRotation;
	}

	public ArrayList<AutoPoint> getAutoPoints() {
		ArrayList<AutoPoint> points = new ArrayList<AutoPoint>();
		for (AutoAction action : autoActions) {
			if (action instanceof AutoGoto) {
				AutoPoint point = ((AutoGoto) action).getPoint();
				points.add(point);
			}
		}
		return points;
	}

	public void save(File file) {
		System.out.println("Saving to " + file.getAbsolutePath());
		try {
			if (!file.exists()) {
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			FileWriter stream = new FileWriter(file);
			stream.write(window.getName() + "\n\n");
			ArrayList<AutoCommand> commands = getCommands();
			boolean first = true;
			for (AutoCommand command : commands) {
				if (!first) {
					stream.write("\n");
				}
				first = false;
				String data = "";
				double[] list = command.toList();
				for (int i = 0; i < list.length; i++) {
					data += list[i];
					if (i + 1 < list.length) {
						data += "\t";
					}
				}
				stream.write(data);
			}
			stream.flush();
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void open(File file) {
		ArrayList<AutoCommand> commands = new ArrayList<AutoCommand>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			String name = "";
			String extraData = "";
			int id = 0;
			while ((line = reader.readLine()) != null) {
				if (id == 0) {
					name = line;
				} else if (id == 1) {
					extraData = line;
				} else {
					String[] dbStrList = line.split("\t");
					double[] dbList = new double[dbStrList.length];
					for (int i = 0; i < dbStrList.length; i++) {
						dbList[i] = Double.parseDouble(dbStrList[i]);
					}
					double cmd = dbList[0];
					switch ((int) cmd) {
					case 0:
						// TODO
						break;
					case 1:
						commands.add(new StraightCommand((int) dbList[1], dbList[2]));
						break;
					case 2:
						commands.add(new TurnCommand(dbList[1], dbList[2]));
						break;
					case 3:
						commands.add(new ArmDownCommand(dbList[1]));
						break;
					case 4:
						commands.add(new ArmUpCommand(dbList[1]));
						break;
					case 5:
						commands.add(new ShootCommand());
						break;
					}
				}
				id++;
			}
			window.setName(name);
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		setCommands(commands);
	}
	
	public void setCommands(ArrayList<AutoCommand> commands){
		ArrayList<AutoAction> points = new ArrayList<AutoAction>();
		AutoPoint currentLocation = new AutoPoint(robotStartPos.getPoint());
		AutoGoto gotoBuffer = null;
		int commandID = 0;
		double rotation = robotStartRotation;
		boolean gotoA = false;
		for (AutoCommand cmd : commands) {
			if (cmd instanceof StraightCommand) {
				if(gotoBuffer == null){
					gotoBuffer = new AutoGoto(currentLocation);
					points.add(gotoBuffer);
				}
				gotoBuffer.setSpeed(((StraightCommand) cmd).getSpeed());
				double distance = ((StraightCommand) cmd).getDistance();
				double speed = ((StraightCommand) cmd).getSpeed();
				if (speed < 0) {
					speed = Math.abs(speed);
					distance *= -1;
				}
				currentLocation.x += Math.cos(Math.toRadians(rotation)) * distance;
				currentLocation.y += Math.sin(Math.toRadians(rotation)) * distance;
				gotoBuffer.setPoint(new AutoPoint(currentLocation));
				gotoBuffer = null;
			} else if (cmd instanceof TurnCommand) {
				if(commandID == 0){
					robotStartPos.setTurnSpeed(((TurnCommand) cmd).getSpeed());
				}
				if(!gotoA){
					gotoBuffer = new AutoGoto(currentLocation);
					points.add(gotoBuffer);
					gotoBuffer.setTurnSpeed(((TurnCommand) cmd).getSpeed());
				}else{
					gotoBuffer = null;
					AutoTurn turn = new AutoTurn();
					turn.setTurnAngle(((TurnCommand) cmd).getAngle());
					turn.setTurnSpeed(((TurnCommand) cmd).getSpeed());
					points.add(turn);
				}
				rotation += ((TurnCommand) cmd).getAngle();
			} else if (cmd instanceof ShootCommand) {
				gotoBuffer = null;
				points.add(new AutoShoot());
			} else if (cmd instanceof ArmUpCommand) {
				gotoBuffer = null;
				AutoRaiseArm auto = new AutoRaiseArm();
				auto.setSpeed(((ArmUpCommand) cmd).getSpeed());
				points.add(auto);
			} else if (cmd instanceof ArmDownCommand) {
				gotoBuffer = null;
				AutoLowerArm auto = new AutoLowerArm();
				auto.setSpeed(((ArmDownCommand) cmd).getSpeed());
				points.add(auto);
			}
			commandID++;
		}
		autoActions = points;
	}
/*
	public void setCommandsOLD(ArrayList<AutoCommand> commands) {
		ArrayList<AutoAction> points = new ArrayList<AutoAction>();
		AutoGoto currentLocation = new AutoGoto(robotStartPos);
		double rotation = robotStartRotation;
		double rotationBuffer = 0;
		for (AutoCommand cmd : commands) {
			if (cmd instanceof StraightCommand) {
				rotation += rotationBuffer;
				rotationBuffer = 0;
				AutoPoint currentPoint = currentLocation.getPoint();
				AutoGoto action = currentLocation;
				if (currentLocation.equals(robotStartPos)) {
					currentPoint = new AutoPoint(currentPoint);
					currentLocation = new AutoGoto(currentPoint);
					points.add(new AutoGoto(currentPoint));
				}
				double distance = ((StraightCommand) cmd).getDistance();
				double speed = ((StraightCommand) cmd).getSpeed();
				if (speed < 0) {
					speed = Math.abs(speed);
					distance *= -1;
				}
				double x = Math.cos(Math.toRadians(rotation)) * distance;
				double y = Math.sin(Math.toRadians(rotation)) * distance;
				currentPoint.x += x;
				currentPoint.y += y;
				action.setSpeed(speed);
			} else if (cmd instanceof TurnCommand) {
				AutoPoint currentPoint = new AutoPoint(currentLocation);
				currentLocation = currentPoint;
				AutoGoto action = new AutoGoto(currentLocation);
				points.add(action);
				rotation += ((TurnCommand) cmd).getAngle();
				action.setTurnSpeed(((TurnCommand) cmd).getSpeed());
			} else if (cmd instanceof ShootCommand) {
				points.add(new AutoShoot());
			} else if (cmd instanceof ArmUpCommand) {
				AutoPoint currentPoint = currentLocation;
				if (currentLocation.equals(robotStartPos)) {
					currentPoint = new AutoPoint(currentLocation);
					currentLocation = currentPoint;
					points.add(new AutoGoto(currentLocation));
				}
				currentPoint.setRaiseArm(true);
			} else if (cmd instanceof ArmDownCommand) {
				AutoPoint currentPoint = currentLocation;
				if (currentLocation.equals(robotStartPos)) {
					currentPoint = new AutoPoint(currentLocation);
					currentLocation = currentPoint;
					points.add(new AutoGoto(currentLocation));
				}
				currentPoint.setLowerArm(true);
			}
		}

		// TODO
		autoActions = points;
	}
*/
	public void removePoint(AutoPoint selectedPoint) {
		AutoAction remove = null;
		for (AutoAction action : autoActions) {
			if (action instanceof AutoGoto) {
				AutoPoint point = ((AutoGoto) action).getPoint();
				if (point.equals(selectedPoint)) {
					remove = action;
				}
			}
		}
		autoActions.remove(remove);
	}

	public AutoPoint addPoint() {
		AutoGoto auto = new AutoGoto();
		autoActions.add(auto);
		return auto.getPoint();
	}

	public AutoPoint getLineStart(AutoPoint end) {
		return getLineStartAction(end).getPoint();
	}

	public AutoGoto getLineStartAction(AutoPoint end) {
		AutoGoto start = robotStartPos;
		for (AutoAction action : autoActions) {
			if (action instanceof AutoGoto) {
				AutoPoint point = ((AutoGoto) action).getPoint();
				if (point.equals(end)) {
					return start;
				}
				start = (AutoGoto) action;
			}
		}
		return null;
	}

	public AutoGoto getAction(AutoPoint end) {
		for (AutoAction action : autoActions) {
			if (action instanceof AutoGoto) {
				AutoPoint point = ((AutoGoto) action).getPoint();
				if (point.equals(end)) {
					return (AutoGoto) action;
				}
			}
		}
		return robotStartPos;
	}

	public void addPoint(AutoPoint start, AutoPoint average) {
		AutoGoto startAction = getAction(start);
		int index = autoActions.indexOf(startAction);
		autoActions.add(index, new AutoGoto(average));
	}

	public AutoPoint getLocationOf(AutoAction action) {
		return getLocationOf(autoActions.indexOf(action));
	}

	public AutoPoint getLocationOf(int index) {
		for (int i = index; i >= 0; i--) {
			AutoAction action = autoActions.get(i);
			if (action instanceof AutoGoto) {
				return ((AutoGoto) action).getPoint();
			}
		}
		return robotStartPos.getPoint();
	}

	public void addAction(AutoAction action) {
		autoActions.add(action);
	}

}
