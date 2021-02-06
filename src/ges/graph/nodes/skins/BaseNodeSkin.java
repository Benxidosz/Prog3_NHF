package ges.graph.nodes.skins;

import ges.graph.Graph;
import ges.graph.Skin;
import ges.graph.nodes.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class BaseNodeSkin extends Skin {
	protected final Node myNode;

	public BaseNodeSkin(Node node) {
		myNode = node;
	}

	private void drawStroke(double x, double y, GraphicsContext gc) {
		gc.setFill(Color.BLACK);
		gc.strokeOval(x - myNode.getGraph().nodeRadius, y - myNode.getGraph().nodeRadius, myNode.getGraph().nodeRadius * 2, myNode.getGraph().nodeRadius * 2);
	}

	@Override
	public void setColor(GraphicsContext gc) {
		if (myNode.isSelected())
			gc.setFill(Color.RED);
		else
			gc.setFill(Color.WHITE);
	}

	public void drawText(GraphicsContext gc, double x, double y) {
		if (myNode.getChooser().equals("Id"))
			gc.strokeText(myNode.getId(), x, y, myNode.getGraph().nodeRadius * 2);
		else
			gc.strokeText(String.valueOf(myNode.getDim()), x, y, myNode.getGraph().nodeRadius * 2);
	}

	@Override
	public void draw(Canvas canvas) {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		double x;
		double y;
		if (myNode.getTmpPos() != null) {
			x = myNode.getTmpPos().x;
			y = myNode.getTmpPos().y;
		} else {
			x = myNode.getPosition().x;
			y = myNode.getPosition().y;
		}

		Graph myGraph = myNode.getGraph();

		setColor(gc);

		gc.fillOval(x - myGraph.nodeRadius, y - myGraph.nodeRadius, myGraph.nodeRadius * 2, myGraph.nodeRadius * 2);

		drawStroke(x, y, gc);

		drawText(gc, x, y);
	}
}
