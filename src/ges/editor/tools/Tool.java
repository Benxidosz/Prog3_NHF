package ges.editor.tools;

import ges.editor.StepTracker;
import ges.graph.Graph;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

public abstract class Tool {
	protected Graph graph;
	protected StepTracker tracker;

	public Tool(Graph g, StepTracker tracker) {
		graph = g;
		this.tracker = tracker;
	}

	public void setGraph(Graph graph) {
		this.graph = graph;
	}

	public abstract void click(MouseEvent mouseEvent, Canvas canvas);

	public abstract void move(MouseEvent mouseEvent, Canvas canvas);

	public abstract void pushed(MouseEvent mouseEvent, Canvas canvas);

	public abstract void released(MouseEvent mouseEvent, Canvas canvas) throws CloneNotSupportedException;

	public abstract void drag(MouseEvent mouseEvent, Canvas canvas) throws CloneNotSupportedException;

	public abstract void scroll(ScrollEvent mouseEvent, Canvas canvas);
}
