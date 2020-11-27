package ges.editor.tools;

import ges.editor.StepTracker;
import ges.graph.Graph;
import ges.graph.Node;
import ges.graph.Position;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

public class MovingTool extends Tool {
	private Node selected;
	private double sx;
	private double sy;
	private double mx;
	private double my;

	public MovingTool(Graph g, StepTracker tracker) {
		super(g, tracker);
		selected = null;
	}


	@Override
	public void click(MouseEvent mouseEvent, Canvas canvas) {

	}

	@Override
	public void move(MouseEvent mouseEvent, Canvas canvas) {
		mx = mouseEvent.getX();
		my = mouseEvent.getY();
	}

	@Override
	public void pushed(MouseEvent mouseEvent, Canvas canvas) {
		selected = graph.getNode(new Position(mouseEvent));
		sx = mouseEvent.getX();
		sy = mouseEvent.getY();
	}

	@Override
	public void released(MouseEvent mouseEvent, Canvas canvas) {
		if (selected == null)
			for (Node node : graph.getNodes()) {
				node.verifyPos();
			}
		selected = null;
		tracker.addStep(graph);
	}

	@Override
	public void drag(MouseEvent mouseEvent, Canvas canvas) {
		if (selected != null) {
			selected.setPosition(new Position(mouseEvent));
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
				graph.refresh(canvas);
			}
		}
	}
}
