package ges.graph;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.io.Serializable;

public abstract class Skin implements Serializable {
	protected boolean switchable = true;
	protected boolean god = false;
	protected boolean completed = true;

	public abstract void setColor(GraphicsContext gc);

	public abstract void draw(Canvas canvas);

	public abstract String export(double w, double h);

	public boolean isSwitchable() {
		return switchable;
	}

	public boolean isGod() {
		return god;
	}

	public void makeDone() {

	}

	public void unDone() {

	}

	public void toComplete() {
		completed = true;
	}
}
