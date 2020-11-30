package ges.graph;

import ges.simulator.algorithms.AlgoState;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.LinkedHashSet;

public class Node extends Scheme {
	private final LinkedHashSet<Node> neighbours;
	private final String id;
	private final Graph myGraph;
	private boolean selected;
	private String chooser;
	private AlgoState drawState;

	Node(Graph graph, String id, Position pos, String chooser) {
		super(pos);

		this.pos = pos;
		this.id = id;
		this.myGraph = graph;
		selected = false;
		this.chooser = chooser;
		drawState = AlgoState.notStarted;

		neighbours = new LinkedHashSet<Node>();
	}

	Node(Graph graph, Node node) {
		this(graph, node.id, new Position(node.pos), node.chooser);
		drawState = AlgoState.notStarted;
	}

	public boolean push(Node neighbour) {
		if (this.equals(neighbour))
			return false;
		return neighbours.add(neighbour);
	}

	public boolean pop(Node neighbour) {
		return neighbours.remove(neighbour);
	}

	public LinkedHashSet<Node> getNeighbours() {
		return neighbours;
	}

	public String getId() {
		return id;
	}

	public void setChooser(String choose) {
		chooser = choose;
	}

	public int getDim() {
		return neighbours.size();
	}

	private void drawStroke(double x, double y, GraphicsContext gc) {
		gc.setFill(Color.BLACK);
		gc.strokeOval(x - myGraph.nodeRadius, y - myGraph.nodeRadius, myGraph.nodeRadius * 2, myGraph.nodeRadius * 2);
	}

	@Override
	public void draw(Canvas canvas) {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		double x;
		double y;
		if (tmpPos != null) {
			x = tmpPos.x;
			y = tmpPos.y;
		} else {
			x = pos.x;
			y = pos.y;
		}

		if (drawState.equals(AlgoState.notStarted))
			if (selected)
				gc.setFill(Color.RED);
			else
				gc.setFill(Color.WHITE);
		else if (drawState.equals(AlgoState.onProgress))
			gc.setFill(Color.LIGHTYELLOW);
		else if (drawState.equals(AlgoState.done))
			gc.setFill(Color.GREEN);


		gc.fillOval(x - myGraph.nodeRadius, y - myGraph.nodeRadius, myGraph.nodeRadius * 2, myGraph.nodeRadius * 2);

		drawStroke(x, y, gc);

		if (chooser.equals("Id"))
			gc.strokeText(id, x, y, myGraph.nodeRadius * 2);
		else
			gc.strokeText(String.valueOf(getDim()), x, y, myGraph.nodeRadius * 2);
	}

	@Override
	public void hoover(Canvas canvas) {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		if (selected)
			gc.setFill(Color.DARKRED);
		else
			gc.setFill(Color.LIGHTYELLOW);
		gc.fillOval(pos.x - myGraph.nodeRadius, pos.y - myGraph.nodeRadius, myGraph.nodeRadius * 2, myGraph.nodeRadius * 2);

		drawStroke(pos.x, pos.y, gc);

		if (chooser.equals("Id"))
			gc.strokeText(id, pos.x, pos.y, myGraph.nodeRadius * 2);
		else
			gc.strokeText(String.valueOf(getDim()), pos.x, pos.y, myGraph.nodeRadius * 2);
	}

	@Override
	public String export(double x, double y) {
		double x1 = (pos.x - x + myGraph.nodeRadius) + 5;
		double y1 = (pos.y - y + myGraph.nodeRadius) + 5;
		return "\t<circle cx=\"" + x1 + "\" cy=\"" + y1 + "\" r=\"" + myGraph.nodeRadius + "\" stroke=\"black\" stroke-width=\"3\" fill=\"white\" />\n" +
				"\t<text x=\"" + x1 + "\" y=\"" + y1 + "\" fill=\"black\">" + id + "</text>";
	}

	public void selected(boolean select) {
		this.selected = select;
	}

	public Graph getGraph() {
		return myGraph;
	}

	public void setDrawState(AlgoState drawState) {
		this.drawState = drawState;
	}
}
