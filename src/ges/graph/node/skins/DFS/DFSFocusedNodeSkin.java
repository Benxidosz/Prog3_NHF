package ges.graph.node.skins.DFS;

import ges.graph.node.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class DFSFocusedNodeSkin extends DFSBaseNodeSkin {

	public DFSFocusedNodeSkin(Node node, int d, String m, int f) {
		super(node, d, m, f);
		switchable = false;
		god = true;
	}

	@Override
	public void setColor(GraphicsContext gc) {
		gc.setFill(Color.YELLOW);
	}
}
