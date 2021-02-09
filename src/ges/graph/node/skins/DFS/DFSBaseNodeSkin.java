package ges.graph.node.skins.DFS;

import ges.graph.Graph;
import ges.graph.node.Node;
import ges.graph.node.skins.BaseNodeSkin;
import javafx.scene.canvas.GraphicsContext;

public class DFSBaseNodeSkin extends BaseNodeSkin {
	final int d;
	final String m;
	final int f;

	public DFSBaseNodeSkin(Node node, int d, String m, int f) {
		super(node);
		this.d = d;
		this.m = m;
		this.f = f;
	}

	@Override
	public void drawText(GraphicsContext gc, double x, double y) {
		Graph myGraph = myNode.getGraph();
		gc.strokeText(myNode.getId(), x, y, myGraph.nodeRadius * 2);
		gc.strokeText("d: " + d +
				"\nf: " + f +
				"\nm: " + m, x + (myGraph.nodeRadius), y, myGraph.nodeRadius * 2);
	}
}
