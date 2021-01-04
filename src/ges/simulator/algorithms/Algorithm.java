package ges.simulator.algorithms;

import ges.graph.Graph;
import ges.graph.nodes.Node;
import javafx.scene.canvas.Canvas;

/**
 * The super class of the algorithms.
 */
public abstract class Algorithm {
	/**
	 * The graph where the algo run.
	 */
	protected final Graph graph;
	protected Graph visualGraph;

	/**
	 * The algoState with represent the progress of the algo.
	 */
	protected AlgoState state;

	/**
	 * The canvas where the algorithm's animation played.
	 */
	protected Canvas canvas;

	Algorithm(Graph g) {
		graph = g;
		visualGraph = new Graph(g);
		state = AlgoState.notStarted;
	}

	/**
	 * A step.
	 */
	public abstract void step();

	/**
	 * First step.
	 *
	 * @param canvas The canvas.
	 * @param start  The starting Node.
	 */
	public abstract void start(Canvas canvas, Node start);

	/**
	 * @return the maximum cycle of the algo.
	 */
	public abstract int getCycle();

	/**
	 * Reset the algo. And all the nodes.
	 */
	public void reset() {
		state = AlgoState.notStarted;
		visualGraph = new Graph(graph);
		graph.refresh(canvas);
	}

	public AlgoState getState() {
		return state;
	}

	public void graphRefresh() {
		visualGraph = new Graph(graph);
	}
}
