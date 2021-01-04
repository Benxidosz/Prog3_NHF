package ges.graph.nodes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class FocusedNode extends Node {
	private final Node original;

	public FocusedNode(Node from) {
		super(from.getGraph(), from);
		original = from;
	}

	@Override
	protected void setColor(GraphicsContext gc) {
		gc.setFill(Color.YELLOW);
	}

	@Override
	public void reset() {
		myGraph.switchNode(this, original);
	}
}
