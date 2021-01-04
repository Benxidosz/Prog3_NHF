package ges.editor.tools;

import ges.editor.diary.Diary;
import ges.editor.diary.logs.AllMoveLog;
import ges.editor.diary.logs.MoveLog;
import ges.graph.Graph;
import ges.graph.nodes.Node;
import ges.graph.Position;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

/**
 * A subclass of Tool. If this tool is selected, you can move a single Node or all of the Nodes.
 */
public class MovingTool extends Tool {

	/**
	 * The selected node, which you can move.
	 * Can be null.
	 */
	private Node selected;

	/**
	 * The x position, where a button was press down.
	 */
	private double sx;

	/**
	 * The y position, where a button was press down.
	 */
	private double sy;

	/**
	 * The x position of the mouse cursor.
	 * It refresh every time, when the position of mouse change.
	 */
	private double mx;

	/**
	 * The y position of the mouse cursor.
	 * It refresh every time, when the position of mouse change.
	 */
	private double my;

	/**
	 * The constructor of the MovingTool.
	 * It set the selected to null.
	 *
	 * @param g       The value of the graph field.
	 * @param tracker The value of the tracker field.
	 */
	public MovingTool(Graph g, Diary tracker) {
		super(g, tracker);
		selected = null;
	}

	/**
	 * It set the mx and my.
	 * It's called, when a mouse moved on a Canvas.
	 *
	 * @param mouseEvent The Event data.
	 * @param canvas     The Canvas, on which the move happened.
	 */
	@Override
	public void move(MouseEvent mouseEvent, Canvas canvas) {
		mx = mouseEvent.getX();
		my = mouseEvent.getY();
	}

	/**
	 * It's request a Node from the graph by position (graph.getNode(Position)) and set it to the value of the selected.
	 * It's called, when a mouseButton pushed down on a Canvas.
	 *
	 * @param mouseEvent The Event data.
	 */
	@Override
	public void pushed(MouseEvent mouseEvent) {
		selected = graph.getNode(new Position(mouseEvent));
		sx = mouseEvent.getX();
		sy = mouseEvent.getY();
	}

	//TODO update docs

	/**
	 * If selected is null: Verify all of the node's position.
	 * And every called, it set selected to null.
	 * It call the tracker's, addStep, method. So this is tracked by the tracker (it can be reverted).
	 * It's called, when a mouseButton released on a Canvas.
	 */
	@Override
	public void released() {
		if (selected != null) {
			diary.addLog(new MoveLog(graph, selected, selected.getPosition(), selected.getTmpPos()));
			selected.verifyPos();
		} else {
			AllMoveLog log = new AllMoveLog(graph);

			for (Node node : graph.getNodes()) {
				log.addNodePos(node, node.getPosition(), node.getTmpPos());
				node.verifyPos();
			}

			diary.addLog(log);
		}
		selected = null;
	}

	/**
	 * If selected isn't null: Set the selected Node position to the position of the mouse.
	 * Otherwise: Subtract a vector, which start with the first position, where the mouseButton pushed down (sx, sy) and
	 * end with the current position of the mouseCursor (mx, my), from all of the nodes position.
	 * It's called, when a mouse drag (mouseButtonPush tha mouseMove) happens on a Canvas.
	 *
	 * @param mouseEvent The Event data.
	 * @param canvas     Thr Canvas, on which the drag happens.
	 */
	@Override
	public void drag(MouseEvent mouseEvent, Canvas canvas) {
		if (selected != null) {
			selected.setTmpPosition(new Position(mouseEvent));
			graph.refresh(canvas);
		} else {
			for (Node node : graph.getNodes()) {
				double x = node.getPosition().x;
				double y = node.getPosition().y;
				double mx = mouseEvent.getX();
				double my = mouseEvent.getY();
				node.setTmpPosition(new Position(x - (sx - mx), y - (sy - my)));
			}
			graph.refresh(canvas);
		}
	}

	/**
	 * If the CTRL is down and the value of the scroll is not 0, rise nodeRadios (field of graph) by the signature value
	 * of the scroll value (-1 or 1).
	 * Than apply a pull/push effect to all Nodes. This effect pull all Nodes to the current position of
	 * the mouseCursor, if the signature value of the scroll is 1, otherwise it push these from the mouseCursor.
	 * It's called, when a scroll happens on a Canvas.
	 *
	 * @param scrollEvent The Event data.
	 * @param canvas      The Canvas, on which the scroll happens.
	 */
	@Override
	public void scroll(ScrollEvent scrollEvent, Canvas canvas) {
		if (scrollEvent.isControlDown()) {
			if (scrollEvent.getDeltaY() != 0 && (graph.nodeRadius > 15 || scrollEvent.getDeltaY() > 0)) {
				double scroll = (scrollEvent.getDeltaY() / Math.abs(scrollEvent.getDeltaY()));
				graph.nodeRadius += scroll;
				for (Node node : graph.getNodes()) {
					double x = node.getPosition().x;
					double y = node.getPosition().y;

					node.setPosition(new Position(x - scroll * (mx - x) / graph.nodeRadius, y - scroll * (my - y) / graph.nodeRadius));
				}
				diary.scroll(new Position(mx, my), scrollEvent, canvas);
				graph.refresh(canvas);
			}
		}
	}
}
