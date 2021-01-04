package ges.simulator.algorithms;

import ges.graph.Graph;
import ges.graph.nodes.DoneNode;
import ges.graph.nodes.FocusedNode;
import ges.graph.nodes.Node;
import ges.graph.nodes.OnProgressNode;
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
		Node switchNode = visualGraph.getNode(next.getId());
		visualGraph.switchNode(switchNode, new OnProgressNode(switchNode));
		if (previous != null)
			visualGraph.getNode(previous.getId()).reset();
		for (Node nei : next.getNeighbours()) {
			DFSStep tmpStep = table.get(nei);
			if (!tmpStep.d) {
				tmpStep.d = true;
				tmpStep.m = next;
				previous = next;
				switchNode = visualGraph.getNode(next.getId());
				visualGraph.switchNode(switchNode, new FocusedNode(switchNode));
				next = nei;
				hasNext = true;
				break;
			}
		}
		if (!hasNext) {
			DFSStep tmpStep = table.get(next);
			switchNode = visualGraph.getNode(next.getId());
			visualGraph.switchNode(switchNode, new DoneNode(switchNode));

			switchNode = visualGraph.getNode(next.getId());
			visualGraph.switchNode(switchNode, new FocusedNode(switchNode));
			if (tmpStep.m != null) {
				previous = next;
				next = tmpStep.m;
			} else {
				boolean noNext = true;
				for (Node node : graph.getNodes())
					if (!table.get(node).d) {
						previous = next;
						next = node;
						table.get(node).d = true;
						noNext = false;
						break;
					}

				if (noNext) {
					state = AlgoState.done;
				}
			}
		}
		visualGraph.refresh(canvas);
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
