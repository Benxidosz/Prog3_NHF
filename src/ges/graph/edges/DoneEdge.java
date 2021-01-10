package ges.graph.edges;

import ges.graph.nodes.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class DoneEdge extends Edge {
	public DoneEdge(Node n1, Node n2) {
		super(n1, n2);
	}

	@Override
	protected void setColor(GraphicsContext gc) {
		gc.setStroke(Color.GREEN);
		gc.setLineWidth(gc.getLineWidth() * 5);
	}
}
