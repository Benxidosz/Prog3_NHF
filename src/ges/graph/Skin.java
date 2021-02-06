package ges.graph;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.io.Serializable;

public abstract class Skin implements Serializable {
	protected boolean switchable = true;
	protected boolean god = false;

	protected abstract void setColor(GraphicsContext gc);

	protected abstract void draw(Canvas canvas);

	public boolean isSwitchable() {
		return switchable;
	}

	public boolean isGod() {
		return god;
	}
}
