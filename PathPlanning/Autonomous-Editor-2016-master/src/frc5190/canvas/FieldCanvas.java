package frc5190.canvas;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import frc5190.auto.AutoPathPlanner;
import frc5190.auto.AutoPoint;

public class FieldCanvas extends JPanel {

	private BufferedImage image;

	public int mouseX = 0;
	public int mouseY = 0;

	public static final int FIELD_PIXEL_X = 93;
	public static final int FIELD_PIXEL_Y = 50;
	public static final int FIELD_PIXEL_W = 1262;
	public static final int FIELD_PIXEL_H = 624;

	public static final int FIELD_WIDTH = 650;
	public static final int FIELD_HEIGHT = 320;

	public static final int ROBOT_SIZE = 20;

	public static final int POINT_SIZE = 3;

	public boolean movingRobot = false;
	public AutoPoint robot = new AutoPoint();
	public int robotRotation;
	private AutoPathPlanner planner;

	public boolean addingPoint = false;
	public AutoPoint point = new AutoPoint();

	public AutoPoint selectedPoint = new AutoPoint();
	public boolean selectedLine = false;

	private double fieldX = 0;
	private double fieldY = 0;
	private double fieldW = 0;
	private double fieldH = 0;

	public int PIXEL_WIDTH = 0;
	public int PIXEL_HEIGHT = 0;
	
	public FieldCanvas(AutoPathPlanner planner) {
		this.planner = planner;
		try {
			image = ImageIO.read(FieldCanvas.class.getResourceAsStream("images/gamefield.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void recalc(int w, int h){
		float rateX = (float) w / (float) image.getWidth();
		float rateY = (float) h / (float) image.getHeight();
		double DW = image.getWidth();
		double DH = image.getHeight();
		double WIDTH = 0;
		double HEIGHT = 0;
		if (rateX > rateY) {
			WIDTH = DW * rateY;
			HEIGHT = DH * rateY;
		} else {
			WIDTH = DW * rateX;
			HEIGHT = DH * rateX;
		}
		PIXEL_WIDTH = (int)WIDTH;
		PIXEL_HEIGHT = (int)HEIGHT;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void paint(Graphics gr) {
		Graphics2D g = (Graphics2D) gr;
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		float rateX = (float) getWidth() / (float) image.getWidth();
		float rateY = (float) getHeight() / (float) image.getHeight();
		double x = 0;
		double y = 0;
		double DW = image.getWidth();
		double DH = image.getHeight();
		double WIDTH = 0;
		double HEIGHT = 0;
		if (rateX > rateY) {
			WIDTH = DW * rateY;
			HEIGHT = DH * rateY;
		} else {
			WIDTH = DW * rateX;
			HEIGHT = DH * rateX;
		}
		PIXEL_WIDTH = (int)WIDTH;
		PIXEL_HEIGHT = (int)HEIGHT;
		x = (getWidth() - WIDTH) / 2;
		y = (getHeight() - HEIGHT) / 2;
		g.drawImage(image, (int) x, (int) y, (int) WIDTH, (int) HEIGHT, null);
		fieldX = (double) FIELD_PIXEL_X / DW * WIDTH + x;
		fieldY = (double) FIELD_PIXEL_Y / DH * HEIGHT + y;
		fieldW = (double) (FIELD_PIXEL_W - FIELD_PIXEL_X) / DW * WIDTH;
		fieldH = (double) (FIELD_PIXEL_H - FIELD_PIXEL_Y) / DH * HEIGHT;
		double mouseLocX = (int) ((double) (mouseX - fieldX) / fieldW * FIELD_WIDTH);
		double mouseLocY = (int) ((double) (mouseY - fieldY) / fieldH * FIELD_HEIGHT);
		// g.setColor(Color.BLUE);
		// g.fillRect(fieldX, fieldY, fieldW, fieldH);
		if (movingRobot) {
			AffineTransform trans = (AffineTransform) g.getTransform().clone();
			robot.x = (int) mouseLocX;
			robot.y = (int) mouseLocY;
			robot.x = Math.min(FIELD_WIDTH - ROBOT_SIZE / 2, Math.max(ROBOT_SIZE / 2, robot.x));
			robot.y = Math.min(FIELD_HEIGHT - ROBOT_SIZE / 2, Math.max(ROBOT_SIZE / 2, robot.y));
			double robotLocX = (((double) robot.x - ROBOT_SIZE / 2d) / FIELD_WIDTH * fieldW) + fieldX;
			double robotLocY = (((double) robot.y - ROBOT_SIZE / 2d) / FIELD_HEIGHT * fieldH) + fieldY;
			int robotSize = (int) (((double) ROBOT_SIZE) / FIELD_WIDTH * WIDTH);
			g.setColor(Color.gray);
			g.rotate(Math.toRadians(robotRotation + 180), robotLocX + robotSize / 2d, robotLocY + robotSize / 2d);
			g.fillRect((int) robotLocX, (int) robotLocY, (int) robotSize, (int) robotSize);
			g.setColor(Color.GREEN);
			g.fillRect((int) robotLocX, (int) robotLocY, (int) (robotSize / 4d), (int) robotSize);
			g.setTransform(trans);
		}
		{
			AffineTransform trans = (AffineTransform) g.getTransform().clone();
			AutoPoint robot = planner.getRobotStartPos().getPoint();
			double robotLocX = (((double) robot.x - ROBOT_SIZE / 2d) / FIELD_WIDTH * fieldW) + fieldX;
			double robotLocY = (((double) robot.y - ROBOT_SIZE / 2d) / FIELD_HEIGHT * fieldH) + fieldY;
			int robotSize = (int) (((double) ROBOT_SIZE) / FIELD_WIDTH * WIDTH);
			g.setColor(Color.DARK_GRAY);
			g.rotate(Math.toRadians(planner.getRobotStartRotation() + 180), robotLocX + robotSize / 2d,
					robotLocY + robotSize / 2d);
			g.fillRect((int) robotLocX, (int) robotLocY, (int) robotSize, (int) robotSize);
			g.setColor(Color.GREEN);
			g.fillRect((int) robotLocX, (int) robotLocY, (int) (robotSize / 4d), (int) robotSize);
			g.setTransform(trans);
		}
		{
			ArrayList<AutoPoint> points = (ArrayList<AutoPoint>) planner.getAutoPoints().clone();
			point = new AutoPoint(mouseLocX, mouseLocY);
			if (addingPoint) {
				points.add(point);
				selectedPoint = null;
			}
			AutoPoint lastPoint = planner.getRobotStartPos().getPoint();
			for (int i = 0; i < points.size(); i++) {
				AutoPoint point = points.get(i);
				double pointSize = (((double) POINT_SIZE) / FIELD_WIDTH * WIDTH);
				double lastPointLocX = (((double) lastPoint.x) / FIELD_WIDTH * fieldW) + fieldX;
				double lastPointLocY = (((double) lastPoint.y) / FIELD_HEIGHT * fieldH) + fieldY;
				lastPoint = point;
				double pointLocX = (((double) point.x) / FIELD_WIDTH * fieldW) + fieldX;
				double pointLocY = (((double) point.y) / FIELD_HEIGHT * fieldH) + fieldY;
				if (addingPoint) {
					g.setColor(Color.GRAY);
				} else {
					g.setColor(Color.DARK_GRAY);
				}
				if (point.equals(selectedPoint) && selectedLine) {
					g.setColor(Color.CYAN);
				}
				g.drawLine((int) pointLocX, (int) pointLocY, (int) lastPointLocX, (int) lastPointLocY);
				if (addingPoint) {
					g.setColor(Color.GRAY);
				} else {
					g.setColor(Color.DARK_GRAY);
				}
				if (point.equals(selectedPoint) && !selectedLine) {
					g.setColor(Color.CYAN);
				}
				g.fillRect((int) (pointLocX - pointSize / 2), (int) (pointLocY - pointSize / 2), (int) pointSize,
						(int) pointSize);
			}
		}
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
	}

	public AutoPoint getLine(int pixelX, int pixelY) {
		int errorAllowed = 5;
		double locX = (int) ((double) (pixelX - fieldX) / fieldW * FIELD_WIDTH);
		double locY = (int) ((double) (pixelY - fieldY) / fieldH * FIELD_HEIGHT);
		AutoPoint last = planner.getRobotStartPos().getPoint();
		for (AutoPoint point : planner.getAutoPoints()) {
			AutoPoint pointOnLine = nearestPointOnLine(last, point, new AutoPoint(locX, locY));
			if (pointOnLine.distance(new AutoPoint(locX, locY)) < errorAllowed) {
				return point;
			}
			last = point;
		}
		return null;
	}

	public AutoPoint nearestPointOnLine(AutoPoint start, AutoPoint end, AutoPoint pos) {
		double ax = start.x;
		double ay = start.y;
		double bx = end.x;
		double by = end.y;
		double px = pos.x;
		double py = pos.y;
		double apx = px - ax;
		double apy = py - ay;
		double abx = bx - ax;
		double aby = by - ay;

		double ab2 = abx * abx + aby * aby;
		double ap_ab = apx * abx + apy * aby;
		double t = ap_ab / ab2;
		if (t < 0) {
			t = 0;
		} else if (t > 1) {
			t = 1;
		}
		return new AutoPoint(ax + abx * t, ay + aby * t);
	}

	public AutoPoint getPoint(int pixelX, int pixelY) {
		int errorAllowed = 3;
		double locX = (int) ((double) (pixelX - fieldX) / fieldW * FIELD_WIDTH);
		double locY = (int) ((double) (pixelY - fieldY) / fieldH * FIELD_HEIGHT);
		for (AutoPoint point : planner.getAutoPoints()) {
			if (point.distance(new AutoPoint(locX, locY)) < errorAllowed) {
				return point;
			}
		}
		if (planner.getRobotStartPos().getPoint().distance(new AutoPoint(locX, locY)) < ROBOT_SIZE / 2) {
			return planner.getRobotStartPos().getPoint();
		}
		return null;
	}

}
