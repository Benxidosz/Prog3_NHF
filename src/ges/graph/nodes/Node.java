package ges.graph.nodes;

import ges.graph.Graph;
import ges.graph.Position;
import ges.graph.Scheme;
import ges.graph.Skin;
import ges.graph.nodes.skins.BaseNodeSkin;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.LinkedHashSet;

/**
 * A subclass of the Scheme. It is represent an Node.
 */
public class Node extends Scheme {

	protected Skin prevSkin;

	/**
	 * A list of the neighbour Nodes.
	 */
	protected final LinkedHashSet<Node> neighbours;

	/**
	 * The id of the Node.
	 */
	private final String id;

	/**
	 * The node's graph.
	 */
	protected final Graph myGraph;

	/**
	 * A state, it show it is selected by an algorithm/tool.
	 */
	private boolean selected;

	/**
	 * It is show what data must appear in the middle of the Node.
	 */
	private String chooser;

	/**
	 * Constructor.
	 *
	 * @param graph   The graph what contains the Node.
	 * @param id      The value of the id.
	 * @param pos     The position of the Node.
	 * @param chooser The data switcher of the middle of the Node.
	 */
	public Node(Graph graph, String id, Position pos, String chooser) {
		super(pos);
		mySkin = new BaseNodeSkin(this);
		prevSkin = null;

		this.pos = pos;
		this.id = id;
		this.myGraph = graph;
		selected = false;
		this.chooser = chooser;

		neighbours = new LinkedHashSet<>();
	}

	/**
	 * Copy constructor.
	 *
	 * @param graph Where the copy will belong.
	 * @param node  The Node, which from the deepCopy made.
	 */
	public Node(Graph graph, Node node) {
		this(graph, node.id, new Position(node.pos), node.chooser);
	}

	/**
	 * Add a Node to the neighbours.
	 *
	 * @param neighbour The neighbour.
	 * @return If it is a success.
	 */
	public boolean push(Node neighbour) {
		if (this.equals(neighbour))
			return false;
		return neighbours.add(neighbour);
	}

	/**
	 * Remove a Node from the neighbours.
	 *
	 * @param neighbour The neighbour.
	 * @return If it is a success.
	 */
	public boolean pop(Node neighbour) {
		return neighbours.remove(neighbour);
	}

	/**
	 * The neighbour list getter.
	 *
	 * @return The list.
	 */
	public LinkedHashSet<Node> getNeighbours() {
		return neighbours;
	}

	/**
	 * The id getter.
	 *
	 * @return The value of id.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Set the chooser.
	 *
	 * @param choose The new value.
	 */
	public void setChooser(String choose) {
		chooser = choose;
	}

	/**
	 * Get the value of the dim.
	 *
	 * @return The value of the dim.
	 */
	public int getDim() {
		return neighbours.size();
	}

	/**
	 * Draw the stroke of the Node.
	 *
	 * @param x  The center x of the Node.
	 * @param y  The center y of the Node.
	 * @param gc The GraphicsContext, where the stroke will be drawn.
	 */
	private void drawStroke(double x, double y, GraphicsContext gc) {
		gc.setFill(Color.BLACK);
		gc.strokeOval(x - myGraph.nodeRadius, y - myGraph.nodeRadius, myGraph.nodeRadius * 2, myGraph.nodeRadius * 2);
	}

	protected void setColor(GraphicsContext gc) {
		if (selected)
			gc.setFill(Color.RED);
		else
			gc.setFill(Color.WHITE);
	}

	protected void drawText(GraphicsContext gc, double x, double y) {
		if (chooser.equals("Id"))
			gc.strokeText(id, x, y, myGraph.nodeRadius * 2);
		else
			gc.strokeText(String.valueOf(getDim()), x, y, myGraph.nodeRadius * 2);
	}

	/**
	 * The hoover animation.
	 *
	 * @param canvas The canvas, where the Scheme will be drawn.
	 */
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

	/**
	 * Generate code of svg.
	 *
	 * @param w The svg file width.
	 * @param h The svg file height.
	 * @return The code.
	 */
	@Override
	public String export(double w, double h) {
		double x1 = (pos.x - w + myGraph.nodeRadius) + 5;
		double y1 = (pos.y - h + myGraph.nodeRadius) + 5;
		return "\t<circle cx=\"" + x1 + "\" cy=\"" + y1 + "\" r=\"" + myGraph.nodeRadius + "\" stroke=\"black\" stroke-width=\"3\" fill=\"white\" />\n" +
				"\t<text x=\"" + x1 + "\" y=\"" + y1 + "\" fill=\"black\">" + id + "</text>";
	}

	/**
	 * Return the selected state.
	 *
	 * @param select The value.
	 */
	public void selected(boolean select) {
		this.selected = select;
	}

	/**
	 * Graph getter.
	 *
	 * @return Value of the graph.
	 */
	public Graph getGraph() {
		return myGraph;
	}

	public void reset() {
		if (prevSkin != null) {
			mySkin = prevSkin;
			prevSkin = null;
		}
	}

	public String getChooser() {
		return chooser;
	}

	public boolean isSelected() {
		return selected;
	}

	@Override
	public void setMySkin(Skin mySkin) {
		this.mySkin = mySkin;
		prevSkin = null;
	}

	public void switchSkin(Skin skin) {
		if (mySkin.isSwitchable() || skin.isGod()) {
			prevSkin = mySkin;
			mySkin = skin;
		}
	}
}
