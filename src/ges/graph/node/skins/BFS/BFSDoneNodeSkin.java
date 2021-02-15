package ges.graph.node.skins.BFS;

import ges.graph.node.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class BFSDoneNodeSkin extends BFSBaseNodeSkin {

	public BFSDoneNodeSkin(Node node, int distance, int index, String previousNodeId) {
		super(node, distance, index, previousNodeId);
	}

	@Override
	public void drawText(GraphicsContext gc, double x, double y) {
		gc.strokeText(myNode.getId(), x, y, myNode.getGraph().nodeRadius * 2);
		gc.strokeText("b: " + (index + 1) +
				"\nt: " + distance +
				"\nm: " + previousNodeId, x + (myNode.getGraph().nodeRadius), y, myNode.getGraph().nodeRadius * 2);
	}

	@Override
	public void setColor(GraphicsContext gc) {
		gc.setFill(Color.GREEN);
	}
}
