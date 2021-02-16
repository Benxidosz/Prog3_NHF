package ges.editor.diary.logs;

import ges.graph.Graph;
import ges.graph.node.Node;

public class RmDirectedEdgeLog extends Log {
	private final Node n1;

	private final Node n2;

	public RmDirectedEdgeLog(Graph g, Node n1, Node n2) {
		super(g);
		this.n1 = n1;
		this.n2 = n2;
	}

	@Override
	public void undo() {
		graph.addDirectedEdge(n1, n2);
	}

	@Override
	public void redo() {
		graph.rmDirectedEdge(n1, n2);
	}
}
