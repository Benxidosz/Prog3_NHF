package ges.editor.diary.logs;

import ges.graph.Graph;
import ges.graph.nodes.Node;

public class NewNodeLog extends Log {
	private final Node addedNode;

	public NewNodeLog(Graph g, Node n) {
		super(g);
		addedNode = n;
	}

	@Override
	public void undo() {
		graph.rmNode(addedNode);
	}

	@Override
	public void redo() {
		graph.addNode(addedNode);
	}
}
