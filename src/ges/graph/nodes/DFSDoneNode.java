package ges.graph.nodes;

import javafx.scene.canvas.GraphicsContext;

public class DFSDoneNode extends DoneNode {
	final int d;
	final String m;
	final int f;

	public DFSDoneNode(Node from, int d, String m, int f) {
		super(from);
		this.d = d;
		this.m = m;
		this.f = f;
	}

	@Override
	protected void drawText(GraphicsContext gc, double x, double y) {
		gc.strokeText(getId(), x, y, myGraph.nodeRadius * 2);
		gc.strokeText("d: " + d +
				"\nf: " + f +
				"\nm: " + m, x + (myGraph.nodeRadius), y, myGraph.nodeRadius * 2);
	}
}
