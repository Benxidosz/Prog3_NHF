package ges.simulator.algorithms;

import ges.graph.Graph;
import ges.graph.Node;
import javafx.scene.canvas.Canvas;

import java.util.HashMap;

/**
 * The Depth First Algorithm
 */
public class DFS extends Algorithm {
	public DFS(Graph g) {
		super(g);
	}

	/**
	 * The next node, which should be process,
	 */
	Node next;

	/**
	 * the previous what was processed.
	 */
	Node previous;

	/**
	 * The previous Node's state.
	 */
	AlgoState prevState;

	class DFSStep {
		boolean d;
		Node m;

		DFSStep() {
			d = false;
			m = null;
		}
	}

	HashMap<Node, DFSStep> table;

	/**
	 * A dfs step.
	 */
	@Override
	public void step() {
		boolean hasNext = false;
		next.setDrawState(AlgoState.onProgress);
		if (previous != null)
			previous.setDrawState(prevState);
		for (Node nei : next.getNeighbours()) {
			DFSStep tmpStep = table.get(nei);
			if (!tmpStep.d) {
				tmpStep.d = true;
				tmpStep.m = next;
				prevState = next.getDrawState();
				previous = next;
				next.setDrawState(AlgoState.focused);
				next = nei;
				hasNext = true;
				break;
			}
		}
		if (!hasNext) {
			DFSStep tmpStep = table.get(next);
			next.setDrawState(AlgoState.done);
			if (previous != null)
				previous.setDrawState(prevState);

			if (tmpStep.m != null) {
				previous = next;
				prevState = next.getDrawState();
				next.setDrawState(AlgoState.focused);
				next = tmpStep.m;
			} else {
				boolean noNext = true;
				for (Node node : graph.getNodes())
					if (!table.get(node).d) {
						previous = next;
						prevState = next.getDrawState();
						next = node;
						table.get(node).d = true;
						noNext = false;
						break;
					}

				if (noNext)
					state = AlgoState.done;
			}
		}
		graph.refresh(canvas);
	}

	@Override
	public void start(Canvas canvas, Node start) {
		table = new HashMap<>();
		for (Node node : graph.getNodes())
			table.put(node, new DFSStep());

		next = start;
		previous = null;
		table.get(start).d = true;
		this.canvas = canvas;
		state = AlgoState.onProgress;
		step();
	}

	@Override
	public int getCycle() {
		return (graph.getNodes().size() * 2) + 1;
	}
}
