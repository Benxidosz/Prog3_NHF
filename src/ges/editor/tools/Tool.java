package ges.editor.tools;

import ges.editor.diary.EditorDiary;
import ges.graph.Graph;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

/**
 * The super class of all tool.
 */
public abstract class Tool {
	/**
	 * The graph that is being modified by the tool.
	 */
	protected Graph graph;

	/**
	 * The StepTracker that is controlling the step history.
	 * It is need because the tool will add step to StepTracker.
	 */
	protected final EditorDiary diary;

	/**
	 * Constructor of the Tool.
	 *
	 * @param g     The value of the graph field.
	 * @param diary The value of the tracker field.
	 */
	public Tool(Graph g, EditorDiary diary) {
		graph = g;
		this.diary = diary;
	}

	/**
	 * The graph field's setter.
	 *
	 * @param graph The new value of the graph field.
	 */
	public void setGraph(Graph graph) {
		this.graph = graph;
	}

	/**
	 * The mouseClickEvent of the tool.
	 * It's called, when a mouseClick happens on a Canvas.
	 *
	 * @param mouseEvent The Event data.
	 * @param canvas     The Canvas, on which the click happened.
	 */
	public void click(MouseEvent mouseEvent, Canvas canvas) {
	}

	/**
	 * The mouseMoveEvent of the tool.
	 * It's called, when the mouse moved on a Canvas.
	 *
	 * @param mouseEvent The Event data.
	 * @param canvas     The Canvas, on which the move happened.
	 */
	public void move(MouseEvent mouseEvent, Canvas canvas) {
	}

	/**
	 * The mouseButtonPushEvent of the tool.
	 * It's called, when a mouseButton pushed down on a Canvas.
	 *
	 * @param mouseEvent The Event data.
	 */
	public void pushed(MouseEvent mouseEvent) {
	}

	/**
	 * The mouseButtonReleaseEvent of the tool.
	 * It's called, when a mouseButton released on a Canvas.
	 */
	public void released() {
	}

	/**
	 * The mouseDragEvent of the tool.
	 * It's called, when a mouse drag (mouseButtonPush tha mouseMove) happens on a Canvas.
	 *
	 * @param mouseEvent The Event data.
	 * @param canvas     Thr Canvas, on which the drag happens.
	 */
	public void drag(MouseEvent mouseEvent, Canvas canvas) {
	}

	/**
	 * The scrollEvent of the tool.
	 * It's called, when a scroll happens on a Canvas.
	 *
	 * @param scrollEvent The Event data.
	 * @param canvas      The Canvas, on which the scroll happens.
	 */
	public void scroll(ScrollEvent scrollEvent, Canvas canvas) {
	}

	/**
	 * If have selected node, it deselect it.
	 */
	public void deselectNode() {
	}
}
