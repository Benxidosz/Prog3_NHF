package ges.editor.diary.logs;

import ges.graph.Graph;
import ges.graph.nodes.Node;
import ges.graph.Position;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.ScrollEvent;

import java.util.ArrayList;

public class AllMoveLog extends Log {

	private class NodeLog {
		Position old;
		Position pos;
		Node node;

		NodeLog(Node n, Position o, Position p) {
			node = n;
			old = o;
			pos = p;
		}
	}

	private final ArrayList<NodeLog> nodes;

	public AllMoveLog(Graph g) {
		super(g);
		nodes = new ArrayList<NodeLog>();
	}

	public void addNodePos(Node node, Position old, Position pos) {
		nodes.add(new NodeLog(node, old, pos));
	}

	@Override
	public void undo() {
		for (NodeLog nodeLog : nodes)
			nodeLog.node.setPosition(nodeLog.old);
	}

	@Override
	public void redo() {
		for (NodeLog nodeLog : nodes)
			nodeLog.node.setPosition(nodeLog.pos);
	}

	@Override
	public void scroll(Position mouse, ScrollEvent scrollEvent, Canvas canvas) {
		double scroll = (scrollEvent.getDeltaY() / Math.abs(scrollEvent.getDeltaY()));

		for (NodeLog nodeLog : nodes) {
			double x = nodeLog.old.x;
			double y = nodeLog.old.y;

			nodeLog.old = new Position(x - scroll * (mouse.x - x) / graph.nodeRadius, y - scroll * (mouse.y - y) / graph.nodeRadius);

			x = nodeLog.pos.x;
			y = nodeLog.pos.y;

			nodeLog.pos = new Position(x - scroll * (mouse.x - x) / graph.nodeRadius, y - scroll * (mouse.y - y) / graph.nodeRadius);
		}
	}
}
