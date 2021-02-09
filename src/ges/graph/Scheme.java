package ges.graph;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.io.Serializable;

/**
 * A super call of the Schemes, which can be drown and serialized.
 */
public abstract class Scheme implements Serializable {

	protected Skin mySkin;

	/**
	 * The position of the Scheme.
	 */
	protected Position pos;

	/**
	 * A temporary position of the Scheme. It used, when all the nodes be moved together.
	 */
	protected Position tmpPos;

	/**
	 * Scheme's constructor.
	 *
	 * @param pos The value of the position.
	 */
	public Scheme(Position pos) {
		this.pos = pos;
	}

	/**
	 * Set the scheme position.
	 *
	 * @param pos The new value.
	 */
	public void setPosition(Position pos) {
		this.pos = pos;
	}

	/**
	 * Set the scheme tmpPosition.
	 *
	 * @param pos The new value.
	 */
	public void setTmpPosition(Position pos) {
		this.tmpPos = pos;
	}

	/**
	 * Verify the the position, so set the position to the tmpPosition.
	 */
	public void verifyPos() {
		if (tmpPos != null) {
			pos = tmpPos;
			tmpPos = null;
		}
	}

	/**
	 * Get the position.
	 *
	 * @return Value of the position.
	 */
	public Position getPosition() {
		return pos;
	}

	protected abstract void setColor(GraphicsContext gc);

	/**
	 * The draw method.
	 *
	 * @param canvas The canvas, where the Scheme will be drawn.
	 */
	public void draw(Canvas canvas) {
		mySkin.draw(canvas);
	}

	/**
	 * The hoover method.
	 *
	 * @param canvas The canvas, where the Scheme will be drawn.
	 */
	public void hoover(Canvas canvas) {

	}

	public Position getTmpPos() {
		return tmpPos;
	}

	/**
	 * The export method.
	 *
	 * @param w The svg file width.
	 * @param h The svg file height.
	 * @return The svg code of the Scheme.
	 */
	public abstract String export(double w, double h);

	public void setMySkin(Skin mySkin) {
		this.mySkin = mySkin;
	}

	public Skin getMySkin() {
		return mySkin;
	}
}
