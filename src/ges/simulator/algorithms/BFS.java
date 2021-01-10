package ges.simulator.algorithms;

import ges.graph.Graph;
import ges.graph.edges.DoneEdge;
import ges.graph.edges.Edge;
import ges.graph.nodes.BFSDoneNode;
import ges.graph.nodes.Node;
import ges.graph.nodes.OnProgressNode;
import javafx.scene.canvas.Canvas;

import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The Breath First Algorithm.
 */
public class BFS extends Algorithm {
	/**
	 * Nodes, which have already processed.
	 */
	LinkedBlockingQueue<Node> processed;
	/**
	 * Nodes, which is under process.
	 */
	LinkedBlockingQueue<Node> processQueue;

	class BFSStep {
		final int distance;
		final int index;
		final String prev;

		BFSStep(int distance, int index, String prev) {
			this.distance = distance;
			this.index = index;
			this.prev = prev;
		}
	}

	HashMap<String, BFSStep> distances;

	int index;

	public BFS(Graph g) {
		super(g);
	}

	/**
	 * Do a step of BFS.
	 */
	@Override
	public void step() {
		if (processQueue.isEmpty()) {
			state = AlgoState.done;
		} else {
			Node underProcess = processQueue.poll();
			Node underInVisual = visualGraph.getNode(underProcess.getId());

			for (Node nei : underProcess.getNeighbours()) {
				if (!(processed.contains(nei) || processQueue.contains(nei))) {
					try {
						processQueue.put(nei);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					distances.put(nei.getId(), new BFSStep(distances.get(underProcess.getId()).distance + 1, index, underProcess.getId()));
					index++;

					Node switchNode = visualGraph.getNode(nei.getId());
					Node newNode = new OnProgressNode(switchNode);
					visualGraph.switchNode(switchNode, newNode);

					Edge switchEdge = visualGraph.getEdge(newNode, underInVisual);
					if (switchEdge != null)
						visualGraph.switchEdge(switchEdge, new DoneEdge(newNode, underInVisual));
				}
			}

			try {
				processed.put(underProcess);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			Node switchNode = visualGraph.getNode(underProcess.getId());
			BFSStep underProcessBFS = distances.get(underProcess.getId());
			visualGraph.switchNode(switchNode, new BFSDoneNode(switchNode, underProcessBFS.distance, underProcessBFS.index, underProcessBFS.prev));

			visualGraph.refresh(canvas);
		}
	}

	/**
	 * Start the algo,
	 *
	 * @param canvas The canvas.
	 * @param start  The starting Node.
	 */
	@Override
	public void start(Canvas canvas, Node start) {
		this.canvas = canvas;
		state = AlgoState.onProgress;
		processed = new LinkedBlockingQueue<>();
		processQueue = new LinkedBlockingQueue<>();
		distances = new HashMap<>();
		index = 0;
		try {
			processQueue.put(start);
			distances.put(start.getId(), new BFSStep(0, index, ""));
			index++;
			Node switchNode = visualGraph.getNode(start.getId());
			visualGraph.switchNode(switchNode, new OnProgressNode(switchNode));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		visualGraph.refresh(canvas);
	}

	@Override
	public int getCycle() {
		return (graph.getNodes().size() + 2);
	}
}
