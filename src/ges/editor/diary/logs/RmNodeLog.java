package ges.editor.diary.logs;

import ges.graph.Graph;
import ges.graph.node.Node;

public class RmNodeLog extends Log {
	private final Node removedNode;

	public RmNodeLog(Graph g, Node removedNode) {
		super(g);
		this.removedNode = removedNode;
	}

	@Override
	public void undo() {
		graph.addNode(removedNode);
	}

	@Override
	public void redo() {
		graph.rmNode(removedNode);
	}
}
