package ges.graph.nodes.skins.BFS;

import ges.graph.nodes.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class BFSDoneNodeSkin extends BFSBaseNodeSkin {

	public BFSDoneNodeSkin(Node node, int distance, int index, String previousNodeId) {
		super(node, distance, index, previousNodeId);
	}

	@Override
	public void setColor(GraphicsContext gc) {
		gc.setFill(Color.GREEN);
	}
}
