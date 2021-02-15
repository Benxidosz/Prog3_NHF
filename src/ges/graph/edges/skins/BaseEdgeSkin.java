package ges.graph.edges.skins;

import ges.graph.Skin;
import ges.graph.edges.Edge;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.Serializable;

public class BaseEdgeSkin extends Skin {
	protected final Edge myEdge;

	private Skin sideSkin;

	public BaseEdgeSkin(Edge myEdge) {
		this.myEdge = myEdge;
		sideSkin = null;
	}

	@Override
	public void setColor(GraphicsContext gc) {
		gc.setStroke(Color.BLACK);
	}

	/**
	 * Draw a line between the two nodes center (position).
	 *
	 * @param canvas The canvas, where the Scheme will be drawn.
	 */
	@Override
	public void draw(Canvas canvas) {
		double x1;
		double y1;
		double x2;
		double y2;
		var nodes = myEdge.getNodes();

		if (nodes[0].getTmpPos() != null) {
			x1 = nodes[0].getTmpPos().x;
			y1 = nodes[0].getTmpPos().y;
		} else {
			x1 = nodes[0].getPosition().x;
			y1 = nodes[0].getPosition().y;
		}
		if (nodes[1].getTmpPos() != null) {
			x2 = nodes[1].getTmpPos().x;
			y2 = nodes[1].getTmpPos().y;
		} else {
			x2 = nodes[1].getPosition().x;
			y2 = nodes[1].getPosition().y;
		}

		GraphicsContext gc = canvas.getGraphicsContext2D();
		double originalWidth = gc.getLineWidth();

		if (sideSkin == null)
			setColor(gc);
		else
			sideSkin.setColor(gc);

		gc.strokeLine(x1, y1, x2, y2);

		gc.setStroke(Color.BLACK);
		gc.setLineWidth(originalWidth);
		gc.setFill(Color.BLACK);
	}

	@Override
	public String export(double w, double h) {
		var nodes = myEdge.getNodes();

		int nodeRadius = nodes[0].getGraph().nodeRadius;
		double x1 = nodes[0].getPosition().x - w + nodeRadius + 5;
		double x2 = nodes[1].getPosition().x - w + nodeRadius + 5;
		double y1 = nodes[0].getPosition().y - h + nodeRadius + 5;
		double y2 = nodes[1].getPosition().y - h + nodeRadius + 5;
		return "\t<line x1=\"" + x1 + "\" y1=\"" + y1 + "\" x2=\"" + x2 + "\" y2=\"" + y2 + "\" style=\"stroke:rgb(0,0,0);stroke-width:2\" />";
	}

	@Override
	public void makeDone() {
		sideSkin = new DoneEdgeSkin(myEdge);
	}

	@Override
	public void unDone() {
		sideSkin = null;
	}
}
