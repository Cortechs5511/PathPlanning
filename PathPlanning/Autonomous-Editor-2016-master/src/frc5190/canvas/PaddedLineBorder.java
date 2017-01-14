package frc5190.canvas;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.LineBorder;

public class PaddedLineBorder extends LineBorder {

	private int paddingH = 0;
	private int paddingW = 0;

	public PaddedLineBorder(int paddingH, int paddingW) {
		super(Color.BLACK);
		this.paddingH = paddingH;
		this.paddingW = paddingW;
	}

	public Insets getBorderInsets(Component paramComponent, Insets paramInsets) {
		int insetW = paddingW + thickness;
		int insetH = paddingH + thickness;
		paramInsets.set(insetW, insetH, insetW, insetH);
		return paramInsets;
	}

}
