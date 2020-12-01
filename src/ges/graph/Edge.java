package ges.graph;

import javafx.scene.canvas.Canvas;

public class Edge extends Scheme {
	final Node[] nodes;

	Edge(Node n1, Node n2) {
		super(new Position(Math.sqrt(Math.pow(n1.getPosition().x - n2.getPosition().x, 2)), Math.sqrt(Math.pow(n1.getPosition().y - n2.getPosition().y, 2))));
		nodes = new Node[2];
		nodes[0] = n1;
		nodes[1] = n2;
	}

	public Edge(Node n1, Position pos) {
		super(pos);
		nodes = new Node[2];
		nodes[0] = n1;
		nodes[1] = new Node(null, "", pos, null);
	}

	@Override
	public void draw(Canvas canvas) {
		double x1;
		double y1;
		double x2;
		double y2;
		if (nodes[0].tmpPos != null) {
			x1 = nodes[0].tmpPos.x;
			y1 = nodes[0].tmpPos.y;
		} else {
			x1 = nodes[0].pos.x;
			y1 = nodes[0].pos.y;
		}
		if (nodes[1].tmpPos != null) {
			x2 = nodes[1].tmpPos.x;
			y2 = nodes[1].tmpPos.y;
		} else {
			x2 = nodes[1].pos.x;
			y2 = nodes[1].pos.y;
		}
		canvas.getGraphicsContext2D().strokeLine(x1, y1, x2, y2);

	}

	@Override
	public void hoover(Canvas canvas) {

	}

	@Override
	public String export(double x, double y) {
		int nodeRadius = nodes[0].getGraph().nodeRadius;
		double x1 = nodes[0].pos.x - x + nodeRadius + 5;
		double x2 = nodes[1].pos.x - x + nodeRadius + 5;
		double y1 = nodes[0].pos.y - y + nodeRadius + 5;
		double y2 = nodes[1].pos.y - y + nodeRadius + 5;
		return "\t<line x1=\"" + x1 + "\" y1=\"" + y1 + "\" x2=\"" + x2 + "\" y2=\"" + y2 + "\" style=\"stroke:rgb(0,0,0);stroke-width:2\" />";
	}

	public boolean nodeInEdge(Node node) {
		return nodes[0] == node || nodes[1] == node;
	}

}
