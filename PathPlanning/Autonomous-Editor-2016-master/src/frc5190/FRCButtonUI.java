package frc5190;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicButtonUI;

public class FRCButtonUI extends BasicButtonUI {

	public static ComponentUI createUI(JComponent paramJComponent) {
		return new FRCButtonUI();
	}

	public void paint(Graphics g, JComponent c) {
		AbstractButton b = (AbstractButton) c;
		ButtonModel model = b.getModel();
		int w = c.getWidth();
		int h = c.getHeight();
		if (b.getIcon() != null) {
			paintIcon(g, c, new Rectangle(0, 0, w, h));
		} else {
			g.setColor(Color.WHITE);
			if (model.isArmed()) {
				g.setColor(Color.LIGHT_GRAY);
			}
			g.fillRect(0, 0, w, h);
			g.setColor(Color.BLACK);
			g.drawRect(0, 0, w - 1, h - 1);
			g.setFont(b.getFont());
			FontMetrics metrics = g.getFontMetrics(g.getFont());
			String text = b.getText();
			if (text != null) {
				int x = (w - metrics.stringWidth(b.getText())) / 2;
				int y = ((h - metrics.getHeight()) / 2) - metrics.getAscent();
				paintText(g, b, new Rectangle(x, y + h / 2, w, h), b.getText());
			}
		}
	}

	protected void paintIcon(Graphics paramGraphics, JComponent paramJComponent, Rectangle paramRectangle) {
		AbstractButton localAbstractButton = (AbstractButton) paramJComponent;
		ButtonModel localButtonModel = localAbstractButton.getModel();
		Object localObject1 = localAbstractButton.getIcon();
		Object localObject2 = null;
		if (localObject1 == null) {
			return;
		}
		Icon localIcon = null;
		if (localButtonModel.isSelected()) {
			localIcon = localAbstractButton.getSelectedIcon();
			if (localIcon != null) {
				localObject1 = localIcon;
			}
		}
		if (!localButtonModel.isEnabled()) {
			if (localButtonModel.isSelected()) {
				localObject2 = localAbstractButton.getDisabledSelectedIcon();
				if (localObject2 == null) {
					localObject2 = localIcon;
				}
			}
			if (localObject2 == null) {
				localObject2 = localAbstractButton.getDisabledIcon();
			}
		} else if ((localButtonModel.isPressed()) && (localButtonModel.isArmed())) {
			localObject2 = localAbstractButton.getPressedIcon();
			if (localObject2 != null) {
				clearTextShiftOffset();
			}
		} else if ((localAbstractButton.isRolloverEnabled()) && (localButtonModel.isRollover())) {
			if (localButtonModel.isSelected()) {
				localObject2 = localAbstractButton.getRolloverSelectedIcon();
				if (localObject2 == null) {
					localObject2 = localIcon;
				}
			}
			if (localObject2 == null) {
				localObject2 = localAbstractButton.getRolloverIcon();
			}
		}
		if (localObject2 != null) {
			localObject1 = localObject2;
		}
		if ((localButtonModel.isPressed()) && (localButtonModel.isArmed())) {
			((Icon) localObject1).paintIcon(paramJComponent, paramGraphics, 0 + getTextShiftOffset(),
					0 + getTextShiftOffset());
		} else {
			((Icon) localObject1).paintIcon(paramJComponent, paramGraphics, 0, 0);
		}
	}

}
