package ges.tools;

import ges.graph.Graph;
import ges.graph.Node;
import ges.graph.Position;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

public class RmNodeTool extends Tool {
	public RmNodeTool(Graph g) {
		super(g);
	}

	@Override
	public void click(MouseEvent mouseEvent, Canvas canvas) {
		Node hoover = graph.getNode(new Position(mouseEvent));
		if (hoover != null) {
			if (graph.rmNode(hoover, canvas))
				graph.refresh(canvas);
		}
	}

	@Override
	public void move(MouseEvent mouseEvent, Canvas canvas) {

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
