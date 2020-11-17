package sample;

import java.util.LinkedHashSet;

public class Node extends Scheme {
	private final LinkedHashSet<Node> neighbours;
	private final String id;
	private final Graph myGraph;

	Node(Graph graph, String id, Position pos) {
		super(pos);

		this.pos = pos;
		this.id = id;
		this.myGraph = graph;

		neighbours = new LinkedHashSet<Node>();
	}

	public boolean push(Node neighbour){
		return neighbours.add(neighbour);
	}

	public LinkedHashSet getNeighbours() { return neighbours; }

	public String getId() { return id; }

	public int getDim() { return neighbours.size(); }

	@Override
	public void draw() {

	}
}
