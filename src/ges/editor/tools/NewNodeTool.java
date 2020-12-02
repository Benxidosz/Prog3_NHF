package ges.editor.tools;

import ges.editor.StepTracker;
import ges.graph.Graph;
import ges.graph.Position;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;

/**
 * A subclass of Tool. If this tool is selected, you can create new Node to the graph.
 */
public class NewNodeTool extends Tool {

	/**
	 * The reference of the combobox.
	 */
	final ComboBox chooser;

	/**
	 * The constructor of the NewNodeTool.
	 * It set the selected to null.
	 *
	 * @param g           The value of the graph field.
	 * @param chooser     The value of the chooser field.
	 * @param stepTracker The value of the stepTracker field.
	 */
	public NewNodeTool(Graph g, ComboBox chooser, StepTracker stepTracker) {
		super(g, stepTracker);
		this.chooser = chooser;
	}

	/**
	 * Create a newNode to the position of the mouseCursor and add to the graph.
	 *
	 * @param mouseEvent The Event data.
	 * @param canvas     The Canvas, on which the click happened.
	 */
	@Override
	public void click(MouseEvent mouseEvent, Canvas canvas) {
		if (graph.addNode(new Position(mouseEvent), (String) chooser.getValue())) {
			graph.refresh(canvas);
			tracker.addStep(graph);
		}
	}
}
