package ges.editor.diary.logs;

import ges.graph.Graph;
import ges.graph.Position;
import ges.graph.nodes.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.ScrollEvent;

public class MoveLog extends Log {

	private Position oldPos;

	private Position pos;

	private final Node movedNode;

	public MoveLog(Graph g, Node moved, Position old, Position p) {
		super(g);

		movedNode = moved;
		oldPos = old;
		pos = p;
	}

	@Override
	public void undo() {
		movedNode.setPosition(oldPos);
	}

	@Override
	public void redo() {
		movedNode.setPosition(pos);
	}

	@Override
	public void scroll(Position mouse, ScrollEvent scrollEvent, Canvas canvas) {
		double scroll = (scrollEvent.getDeltaY() / Math.abs(scrollEvent.getDeltaY()));

		double x = oldPos.x;
		double y = oldPos.y;

		oldPos = new Position(x - scroll * (mouse.x - x) / graph.nodeRadius, y - scroll * (mouse.y - y) / graph.nodeRadius);

		x = pos.x;
		y = pos.y;

		pos = new Position(x - scroll * (mouse.x - x) / graph.nodeRadius, y - scroll * (mouse.y - y) / graph.nodeRadius);
	}
}
