package ges.graph;

import javafx.scene.input.MouseEvent;

import java.io.Serializable;

/**
 * Represents a position. It can be serialized.
 */
public class Position implements Serializable {

	/**
	 * x coordinate.
	 */
	public final double x;

	/**
	 * y coordinate.
	 */
	public final double y;

	/**
	 * Constructor from a (x, y) coordinate.
	 *
	 * @param x Given x.
	 * @param y Given y.
	 */
	public Position(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Constructor from MouseEvent.
	 *
	 * @param event Given event.
	 */
	public Position(MouseEvent event) {
		this.x = event.getX();
		this.y = event.getY();
	}

	/**
	 * Copy constructor.
	 *
	 * @param pos Given pos.
	 */
	public Position(Position pos) {
		this(pos.x, pos.y);
	}
}
