package ges.simulator.algorithms;

import ges.graph.Graph;
import ges.graph.Node;
import javafx.scene.canvas.Canvas;


public abstract class Algorithm {
	protected Graph graph;
	protected long startTime;
	protected boolean first;

	Algorithm(Graph g) {
		graph = g;
	}

	public abstract void process(Canvas canvas, Node start, Double time);
}
