package ges.graph.nodes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class DoneNode extends Node {
	private final Node original;

	public DoneNode(Node from) {
		super(from.getGraph(), from);
		original = from;
	}

	@Override
	protected void setColor(GraphicsContext gc) {
		gc.setFill(Color.GREEN);
	}

	@Override
	public void reset() {
		myGraph.switchNode(this, original);
	}
}
