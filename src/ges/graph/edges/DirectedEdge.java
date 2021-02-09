package ges.graph.edges;

import ges.graph.Graph;
import ges.graph.Position;
import ges.graph.node.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class DirectedEdge extends Edge {
	final Graph myGraph;

	public DirectedEdge(Node n1, Node n2, Graph myGraph) {
		super(n1, n2);
		this.myGraph = myGraph;
	}

	public DirectedEdge(Node n1, Position pos, Graph myGraph) {
		super(n1, pos);
		this.myGraph = myGraph;
	}

	@Override
	public void draw(Canvas canvas) {
		double x1;
		double y1;
		double x2;
		double y2;
		if (nodes[0].getTmpPos() != null) {
			x1 = nodes[0].getTmpPos().x;
			y1 = nodes[0].getTmpPos().y;
		} else {
			x1 = nodes[0].getPosition().x;
			y1 = nodes[0].getPosition().y;
		}
		if (nodes[1].getTmpPos() != null) {
			x2 = nodes[1].getTmpPos().x;
			y2 = nodes[1].getTmpPos().y;
		} else {
			x2 = nodes[1].getPosition().x;
			y2 = nodes[1].getPosition().y;
		}

		GraphicsContext gc = canvas.getGraphicsContext2D();
		double originalWidth = gc.getLineWidth();

		setColor(gc);

		gc.setFill(Color.BLACK);

		double dx = x2 - x1, dy = y2 - y1;
		int len = (int) Math.sqrt(dx * dx + dy * dy);
		int ARR_SIZE = 8;

		gc.strokeLine(x1, y1, x2, y2);
		gc.fillPolygon(new double[]{x2 + len - myGraph.nodeRadius, x2 + len - ARR_SIZE - myGraph.nodeRadius, x2 + len - ARR_SIZE - myGraph.nodeRadius, x2 + len - myGraph.nodeRadius}, new double[]{y2 - myGraph.nodeRadius, y2 - myGraph.nodeRadius - ARR_SIZE, y2 - myGraph.nodeRadius + ARR_SIZE, y2 - myGraph.nodeRadius},
				4);

		gc.setStroke(Color.BLACK);
		gc.setLineWidth(originalWidth);
	}
}
