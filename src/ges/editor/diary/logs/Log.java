package ges.editor.diary.logs;

import ges.graph.Graph;
import ges.graph.Position;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.ScrollEvent;

public abstract class Log {
	protected Graph graph;

	protected Log(Graph g) {
		graph = g;
	}

	public abstract void undo();

	public abstract void redo();

	public void scroll(Position mouse, ScrollEvent scrollEvent, Canvas canvas) {

	}

}
