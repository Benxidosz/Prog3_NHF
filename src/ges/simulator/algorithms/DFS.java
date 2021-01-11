package ges.simulator.algorithms;

import ges.graph.Graph;
import ges.graph.edges.DoneEdge;
import ges.graph.edges.Edge;
import ges.graph.nodes.*;
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
		int d;
		int f;
		String m;

		DFSStep() {
			d = -1;
			f = -1;
			m = "";
		}
	}

	HashMap<String, DFSStep> table;

	int D;
	int F;

	/**
	 * A dfs step.
	 */
	@Override
	public void step() {
		if (state == AlgoState.waitForReset) {
			visualGraph.getNode(next.getId()).reset();
			state = AlgoState.done;
		} else {
			boolean hasNext = false;
			Node switchNode = visualGraph.getNode(next.getId());

			DFSStep progress = table.get(switchNode.getId());
			Node newNode = new DFSOnProgressNode(switchNode, progress.d, progress.m);
			visualGraph.switchNode(switchNode, newNode);

			if (previous != null) {
				Edge switchEdge = visualGraph.getEdge(newNode, visualGraph.getNode(previous.getId()));
				if (switchEdge != null)
					visualGraph.switchEdge(switchEdge, new DoneEdge(newNode, visualGraph.getNode(previous.getId())));
			}

			if (previous != null)
				visualGraph.getNode(previous.getId()).reset();
			for (Node nei : next.getNeighbours()) {
				DFSStep tmpStep = table.get(nei.getId());
				if (tmpStep.d == -1) {
					tmpStep.d = D++;
					tmpStep.m = next.getId();
					previous = next;
					switchNode = visualGraph.getNode(next.getId());
					visualGraph.switchNode(switchNode, new FocusedNode(switchNode));
					next = nei;
					hasNext = true;
					break;
				}
			}
			if (!hasNext) {
				DFSStep tmpStep = table.get(next.getId());

				table.get(next.getId()).f = F++;
				switchNode = visualGraph.getNode(next.getId());
				visualGraph.switchNode(switchNode, new DFSDoneNode(switchNode, tmpStep.d, tmpStep.m, tmpStep.f));

				switchNode = visualGraph.getNode(next.getId());
				visualGraph.switchNode(switchNode, new FocusedNode(switchNode));

				if (!"".equals(tmpStep.m) && tmpStep.m != null) {
					previous = next;
					next = graph.getNode(tmpStep.m);
				} else {
					boolean noNext = true;
					for (Node node : graph.getNodes())
						if (table.get(node.getId()).d == -1) {
							previous = next;
							next = node;
							table.get(node.getId()).d = D++;
							noNext = false;
							break;
						}

					if (noNext) {
						state = AlgoState.waitForReset;
					}
				}
			}
		}
		visualGraph.refresh(canvas);
	}

	@Override
	public void start(Canvas canvas, Node start) {
		D = 1;
		F = 1;
		table = new HashMap<>();
		for (Node node : graph.getNodes())
			table.put(node.getId(), new DFSStep());

		next = start;
		previous = null;
		table.get(start.getId()).d = D++;
		this.canvas = canvas;
		state = AlgoState.onProgress;
		step();
	}

	@Override
	public int getCycle() {
		return (graph.getNodes().size() * 2) + 1;
	}
}
