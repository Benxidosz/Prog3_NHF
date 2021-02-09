package ges.graph.node.skins.DFS;

import ges.graph.node.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class DFSDoneNodeSkin extends DFSBaseNodeSkin {
	public DFSDoneNodeSkin(Node node, int d, String m, int f) {
		super(node, d, m, f);
		switchable = false;
	}

	@Override
	public void setColor(GraphicsContext gc) {
		gc.setFill(Color.GREEN);
	}
}
