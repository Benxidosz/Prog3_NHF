package ges.tools;

import ges.graph.Graph;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

public abstract class Tool {
	protected Graph graph;

	public Tool(Graph g) {
		graph = g;
	}

	public abstract void click(MouseEvent mouseEvent, Canvas canvas);

	public abstract void move(MouseEvent mouseEvent, Canvas canvas);

	public abstract void pushed(MouseEvent mouseEvent, Canvas canvas);

	public abstract void released(MouseEvent mouseEvent, Canvas canvas);

	public abstract void drag(MouseEvent mouseEvent, Canvas canvas);

	public abstract void scroll(ScrollEvent mouseEvent, Canvas canvas);
}
