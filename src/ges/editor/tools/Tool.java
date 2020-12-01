package ges.editor.tools;

import ges.editor.StepTracker;
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
	protected final StepTracker tracker;

	/**
	 * Constructor of the Tool.
	 *
	 * @param g       The value of the graph field.
	 * @param tracker The value of the tracker field.
	 */
	public Tool(Graph g, StepTracker tracker) {
		graph = g;
		this.tracker = tracker;
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
	public abstract void click(MouseEvent mouseEvent, Canvas canvas);

	/**
	 * The mouseMoveEvent of the tool.
	 * It's called, when the mouse moved on a Canvas.
	 *
	 * @param mouseEvent The Event data.
	 * @param canvas     The Canvas, on which the move happened.
	 */
	public abstract void move(MouseEvent mouseEvent, Canvas canvas);

	/**
	 * The mouseButtonPushEvent of the tool.
	 * It's called, when a mouseButton pushed down on a Canvas.
	 *
	 * @param mouseEvent The Event data.
	 */
	public abstract void pushed(MouseEvent mouseEvent);

	/**
	 * The mouseButtonReleaseEvent of the tool.
	 * It's called, when a mouseButton released on a Canvas.
	 */
	public abstract void released();

	/**
	 * The mouseDragEvent of the tool.
	 * It's called, when a mouse drag (mouseButtonPush tha mouseMove) happens on a Canvas.
	 *
	 * @param mouseEvent The Event data.
	 * @param canvas     Thr Canvas, on which the drag happens.
	 */
	public abstract void drag(MouseEvent mouseEvent, Canvas canvas);

	/**
	 * The scrollEvent of the tool.
	 * It's called, when a scroll happens on a Canvas.
	 *
	 * @param scrollEvent The Event data.
	 * @param canvas      The Canvas, on which the scroll happens.
	 */
	public abstract void scroll(ScrollEvent scrollEvent, Canvas canvas);
}
