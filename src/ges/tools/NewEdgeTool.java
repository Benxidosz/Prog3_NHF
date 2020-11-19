package ges.tools;

import ges.graph.Edge;
import ges.graph.Graph;
import ges.graph.Node;
import ges.graph.Position;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

public class NewEdgeTool extends Tool {

	private Node selectedNode;

	public NewEdgeTool(Graph g) {
		super(g);
		selectedNode = null;
	}

	@Override
	public void click(MouseEvent mouseEvent, Canvas canvas) {
		Node select = graph.getNode(new Position(mouseEvent));
		if (select != null) {
			if (selectedNode == null) {
				selectedNode = select;
				selectedNode.selected(true);
			} else {
				if (selectedNode == select) {
					selectedNode.selected(false);
					selectedNode = null;
				} else {
					graph.addEdge(selectedNode, select, canvas);
					selectedNode.selected(false);
					selectedNode = null;
				}
			}
			graph.refresh(canvas);
		}
	}

	@Override
	public void move(MouseEvent mouseEvent, Canvas canvas) {
		if (selectedNode != null) {
			Edge tmpEdge = new Edge(selectedNode, new Position(mouseEvent));
			graph.addEdge(tmpEdge);
			graph.refresh(canvas);
			Node hoover = graph.getNode(new Position(mouseEvent));
			if (hoover != null) {
				hoover.hoover(canvas);
			}
			graph.rmEdge(tmpEdge);
		}
	}

	@Override
	public void pushed(MouseEvent mouseEvent, Canvas canvas) {

	}

	@Override
	public void released(MouseEvent mouseEvent, Canvas canvas) {

	}

	@Override
	public void drag(MouseEvent mouseEvent, Canvas canvas) {

	}

	@Override
	public void scroll(ScrollEvent mouseEvent, Canvas canvas) {

	}
}
