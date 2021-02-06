package ges.graph.edges.skins;

import ges.graph.edges.Edge;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class DoneEdgeSkin extends BaseEdgeSkin {

	public DoneEdgeSkin(Edge myEdge) {
		super(myEdge);
	}

	@Override
	public void setColor(GraphicsContext gc) {
		gc.setStroke(Color.GREEN);
		gc.setLineWidth(gc.getLineWidth() * 5);
	}
}
