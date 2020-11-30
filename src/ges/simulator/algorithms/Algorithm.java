package ges.simulator.algorithms;

import ges.graph.Graph;
import ges.graph.Node;
import javafx.scene.canvas.Canvas;


public abstract class Algorithm {
	protected Graph graph;

	protected AlgoState state;
	protected Canvas canvas;

	Algorithm(Graph g) {
		graph = g;
		state = AlgoState.notStarted;
	}

	public abstract void step();

	public abstract void start(Canvas canvas, Node start);

	public abstract int getCycle();

	public void reset() {
		state = AlgoState.notStarted;
		for (Node node : graph.getNodes())
			node.setDrawState(AlgoState.notStarted);
		graph.refresh(canvas);
	}

	public AlgoState getState() {
		return state;
	}
}
