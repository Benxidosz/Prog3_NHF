package ges.graph.nodes;

import javafx.scene.canvas.GraphicsContext;

public class DFSOnProgressNode extends OnProgressNode {
	final int d;
	final String m;

	public DFSOnProgressNode(Node from, int d, String m) {
		super(from);
		this.d = d;
		this.m = m;
	}

	@Override
	protected void drawText(GraphicsContext gc, double x, double y) {
		gc.strokeText(getId(), x, y, myGraph.nodeRadius * 2);
		gc.strokeText("d: " + d +
				"\nm: " + m, x + (myGraph.nodeRadius), y, myGraph.nodeRadius * 2);
	}
}
