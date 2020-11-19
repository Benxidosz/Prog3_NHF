package ges.graph;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.io.Serializable;

public abstract class Scheme implements Serializable {
	protected Position pos;
	protected Position tmpPos;

	Scheme(Position pos) {
		this.pos = pos;
	}

	public void setPosition(Position pos) {
		this.pos = pos;
	}

	public void setTmpPosition(Position pos) {
		this.tmpPos = pos;
	}

	public void verifyPos() {
		if (tmpPos != null) {
			pos = tmpPos;
			tmpPos = null;
		}
	}

	public Position getPosition() {
		return pos;
	}

	public abstract void draw(Canvas canvas);

	public abstract void hoover(Canvas canvas);

	public abstract String export(double x, double y);
}
