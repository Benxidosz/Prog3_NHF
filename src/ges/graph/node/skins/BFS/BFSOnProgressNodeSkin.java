package ges.graph.node.skins.BFS;

import ges.graph.node.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class BFSOnProgressNodeSkin extends BFSBaseNodeSkin {

	public BFSOnProgressNodeSkin(Node node, int distance, int index, String previousNodeId) {
		super(node, distance, index, previousNodeId);
	}

	@Override
	public void setColor(GraphicsContext gc) {
		gc.setFill(Color.LIGHTYELLOW);
	}
}
