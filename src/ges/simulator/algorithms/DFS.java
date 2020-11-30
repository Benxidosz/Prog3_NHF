package ges.simulator.algorithms;

import ges.graph.Graph;
import ges.graph.Node;
import javafx.scene.canvas.Canvas;

import java.util.HashMap;

public class DFS extends Algorithm {
	public DFS(Graph g) {
		super(g);
	}

	Node next;

	class DFSStep {
		boolean d;
		Node m;

		DFSStep() {
			d = false;
			m = null;
		}
	}

	HashMap<Node, DFSStep> table;

	@Override
	public void step() {
		boolean hasNext = false;
		next.setDrawState(AlgoState.onProgress);
		for (Node nei : next.getNeighbours()) {
			DFSStep tmpStep = table.get(nei);
			if (!tmpStep.d) {
				tmpStep.d = true;
				tmpStep.m = next;
				next = nei;
				hasNext = true;
				break;
			}
		}
		if (!hasNext) {
			DFSStep tmpStep = table.get(next);
			next.setDrawState(AlgoState.done);
			if (tmpStep.m != null)
				next = tmpStep.m;
			else {
				boolean noNext = true;
				for (Node node : graph.getNodes())
					if (!table.get(node).d) {
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
