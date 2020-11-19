package ges.graph;

import javafx.scene.input.MouseEvent;

import java.io.Serializable;

public class Position implements Serializable {
	public double x;
	public double y;

	public Position(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Position(MouseEvent event) {
		this.x = event.getX();
		this.y = event.getY();
	}
}
