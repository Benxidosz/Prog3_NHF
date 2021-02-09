package ges.graph.node.skins.BFS;

import ges.graph.node.Node;
import ges.graph.node.skins.BaseNodeSkin;
import javafx.scene.canvas.GraphicsContext;

public class BFSBaseNodeSkin extends BaseNodeSkin {
	private final int distance;
	private final int index;
	private final String previousNodeId;

	public BFSBaseNodeSkin(Node node, int distance, int index, String previousNodeId) {
		super(node);
		this.distance = distance;
		this.index = index;
		this.previousNodeId = previousNodeId;
	}

	@Override
	public void drawText(GraphicsContext gc, double x, double y) {
		gc.strokeText(myNode.getId(), x, y, myNode.getGraph().nodeRadius * 2);
		gc.strokeText("b: " + index +
				"\nt: " + distance +
				"\nm: " + previousNodeId, x + (myNode.getGraph().nodeRadius), y, myNode.getGraph().nodeRadius * 2);
	}
}
