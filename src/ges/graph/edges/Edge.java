package ges.graph.edges;

import ges.graph.Position;
import ges.graph.Scheme;
import ges.graph.nodes.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


/**
 * A subclass of the Scheme. It is represent an Edge.
 */
public class Edge extends Scheme {

	/**
	 * An array of the node, where to the Edge belongs. (Always 2).
	 */
	private final Node[] nodes;

	/**
	 * Edge's constructor from two node.
	 *
	 * @param n1 First node.
	 * @param n2 Second node.
	 */
	public Edge(Node n1, Node n2) {
		super(new Position(Math.sqrt(Math.pow(n1.getPosition().x - n2.getPosition().x, 2)), Math.sqrt(Math.pow(n1.getPosition().y - n2.getPosition().y, 2))));
		nodes = new Node[2];
		nodes[0] = n1;
		nodes[1] = n2;
	}

	/**
	 * Edge's constructor from a node and from a position.
	 *
	 * @param n1  First node.
	 * @param pos The position.
	 */
	public Edge(Node n1, Position pos) {
		super(pos);
		nodes = new Node[2];
		nodes[0] = n1;
		nodes[1] = new Node(null, "", pos, null);
	}

	@Override
	protected void setColor(GraphicsContext gc) {
		gc.setStroke(Color.BLACK);
	}

	/**
	 * Draw a line between the two nodes center (position).
	 *
	 * @param canvas The canvas, where the Scheme will be drawn.
	 */
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

		setColor(gc);

		gc.strokeLine(x1, y1, x2, y2);

	}

	/**
	 * It will generate a svg code, which represents a line, relative to the positions of the Edge's nodes and the svg
	 * file's width and height.
	 *
	 * @param w The svg file width.
	 * @param h The svg file height.
	 * @return The code.
	 */
	@Override
	public String export(double w, double h) {
		int nodeRadius = nodes[0].getGraph().nodeRadius;
		double x1 = nodes[0].getPosition().x - w + nodeRadius + 5;
		double x2 = nodes[1].getPosition().x - w + nodeRadius + 5;
		double y1 = nodes[0].getPosition().y - h + nodeRadius + 5;
		double y2 = nodes[1].getPosition().y - h + nodeRadius + 5;
		return "\t<line x1=\"" + x1 + "\" y1=\"" + y1 + "\" x2=\"" + x2 + "\" y2=\"" + y2 + "\" style=\"stroke:rgb(0,0,0);stroke-width:2\" />";
	}

	/**
	 * Check if a node is in the edge. (one of the two nodes).
	 *
	 * @param node The node, which need to be checked.
	 * @return True if yes, false if not.
	 */
	public boolean nodeInEdge(Node node) {
		return nodes[0] == node || nodes[1] == node;
	}

	public Node getNode(int i) {
		return nodes[i];
	}
}
