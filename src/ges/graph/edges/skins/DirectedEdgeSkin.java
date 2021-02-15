package ges.graph.edges.skins;

import ges.graph.Skin;
import ges.graph.edges.Edge;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;

public class DirectedEdgeSkin extends BaseEdgeSkin {
	private Skin sideSkin;

	public DirectedEdgeSkin(Edge myEdge) {
		super(myEdge);
		sideSkin = null;
		completed = false;
	}

	@Override
	public void draw(Canvas canvas) {
		GraphicsContext gc = canvas.getGraphicsContext2D();

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

		int ARR_SIZE = 8;

		int nodeRadius = myEdge.getNode(0).getGraph().nodeRadius;

		double len = Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
		double angle = 0;

		final double asin = Math.asin(Math.abs(x1 - x2) / len);
		if (x2 <= x1 && y2 >= y1)
			angle = asin;
		else if (x2 >= x1 && y2 >= y1)
			angle = -asin;
		else if (x2 <= x1 && y2 <= y1)
			angle = -asin + Math.PI;
		else if (x2 >= x1 && y2 <= y1)
			angle = asin + Math.PI;

		double[] baseXes = new double[]{x2, x2 - ARR_SIZE, x2 + ARR_SIZE, x2};
		double[] baseYes;
		if (completed) {
			baseYes = new double[]{y2 - nodeRadius, y2 - ARR_SIZE - nodeRadius, y2 - ARR_SIZE - nodeRadius, y2 - nodeRadius};
		} else {
			baseYes = new double[]{y2, y2 - ARR_SIZE, y2 - ARR_SIZE, y2};
		}

		double s = Math.sin(angle);
		double c = Math.cos(angle);

		for (int i = 0; i < 4; ++i) {
			double x = baseXes[i];
			double y = baseYes[i];

			x -= x2;
			y -= y2;

			// rotate point
			double xnew = x * c - y * s;
			double ynew = x * s + y * c;

			// translate point back:
			baseXes[i] = xnew + x2;
			baseYes[i] = ynew + y2;
		}

		double originalWidth = gc.getLineWidth();

		if (sideSkin == null)
			setColor(gc);
		else
			sideSkin.setColor(gc);

		gc.strokeLine(x1, y1, x2, y2);

		gc.setLineWidth(originalWidth);

		gc.fillPolygon(baseXes, baseYes,
				4);

		gc.setStroke(Color.BLACK);
		gc.setFill(Color.BLACK);
	}

	@Override
	public String export(double w, double h) {
		StringBuilder ret = new StringBuilder(super.export(w, h) + "\t<polygon points=\"");

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

		int ARR_SIZE = 8;

		int nodeRadius = myEdge.getNode(0).getGraph().nodeRadius;

		double len = Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
		double angle = 0;

		final double asin = Math.asin(Math.abs(x1 - x2) / len);
		if (x2 <= x1 && y2 >= y1)
			angle = asin;
		else if (x2 >= x1 && y2 >= y1)
			angle = -asin;
		else if (x2 <= x1 && y2 <= y1)
			angle = -asin + Math.PI;
		else if (x2 >= x1 && y2 <= y1)
			angle = asin + Math.PI;

		double[] baseXes = new double[]{x2, x2 - ARR_SIZE, x2 + ARR_SIZE, x2};
		double[] baseYes;

		baseYes = new double[]{y2 - nodeRadius, y2 - ARR_SIZE - nodeRadius, y2 - ARR_SIZE - nodeRadius, y2 - nodeRadius};


		double s = Math.sin(angle);
		double c = Math.cos(angle);

		for (int i = 0; i < 4; ++i) {
			double x = baseXes[i];
			double y = baseYes[i];

			x -= x2;
			y -= y2;

			// rotate point
			double xnew = x * c - y * s;
			double ynew = x * s + y * c;

			// translate point back:
			baseXes[i] = xnew + x2;
			baseYes[i] = ynew + y2;
		}

		for (int i = 0; i < 4; ++i) {
			ret.append(baseXes[i] - w + nodeRadius + 5).append(",").append(baseYes[i] - h + nodeRadius + 5);
			if (i < 3)
				ret.append(" ");
		}

		ret.append("\" style=\"fill:black;stroke:black;stroke-width:1\" />");

		return ret.toString();
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
