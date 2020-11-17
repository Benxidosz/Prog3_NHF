package sample;

import java.util.LinkedHashSet;

public class Graph {
	int nodeRadius;
	private final StringBuilder tmpId;

	private final LinkedHashSet<Node> nodes;
	private final LinkedHashSet<Edge> edges;

	Graph(int nodeRadius) {
		this.nodeRadius = nodeRadius;
		tmpId = new StringBuilder("A");

		nodes = new LinkedHashSet<>();
		edges = new LinkedHashSet<>();
	}

	boolean addNode(Position pos) {
		boolean ret = nodes.add(new Node(this, tmpId.toString(), pos));

		for(int i = tmpId.length() - 1; i >= 0; --i) {
			tmpId.setCharAt(i, (char) (tmpId.charAt(i) + 1));

			if(tmpId.charAt(i) <= 'Z')
				break;

			tmpId.setCharAt(i, 'A');
			if(i == 0){
				tmpId.append('A');
			}
		}

		return ret;
	}
}
