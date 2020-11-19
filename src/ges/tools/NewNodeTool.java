package ges.tools;

import ges.graph.Graph;
import ges.graph.Position;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

public class NewNodeTool extends Tool {
	ComboBox chooser;

	public NewNodeTool(Graph g, ComboBox chooser) {
		super(g);
		this.chooser = chooser;
	}

	@Override
	public void click(MouseEvent mouseEvent, Canvas canvas) {
		if (graph.addNode(new Position(mouseEvent), canvas, (String) chooser.getValue()))
			graph.refresh(canvas);
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
