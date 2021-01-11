package ges.graph.nodes;

import javafx.scene.canvas.GraphicsContext;

public class BFSDoneNode extends DoneNode {
	private final int distance;
	private final int index;
	private final String previousNodeId;

	public BFSDoneNode(Node from, int distance, int index, String previousNodeId) {
		super(from);
		this.distance = distance;
		this.index = index;
		this.previousNodeId = previousNodeId;
	}

	@Override
	protected void drawText(GraphicsContext gc, double x, double y) {
		gc.strokeText(getId(), x, y, myGraph.nodeRadius * 2);
		gc.strokeText("b: " + index +
				"\nt: " + distance +
				"\nm: " + previousNodeId, x + (myGraph.nodeRadius), y, myGraph.nodeRadius * 2);
	}
}
