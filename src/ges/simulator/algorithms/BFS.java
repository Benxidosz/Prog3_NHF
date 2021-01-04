package ges.simulator.algorithms;

import ges.graph.Graph;
import ges.graph.edges.Edge;
import ges.graph.edges.FocusedEdge;
import ges.graph.nodes.DoneNode;
import ges.graph.nodes.Node;
import ges.graph.nodes.OnProgressNode;
import javafx.scene.canvas.Canvas;

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

					Node switchNode = visualGraph.getNode(nei.getId());
					Edge switchEdge = visualGraph.getEdge(switchNode, underInVisual);
					if (switchEdge != null)
						visualGraph.switchEdge(switchEdge, new FocusedEdge(switchNode, underInVisual));
					visualGraph.switchNode(switchNode, new OnProgressNode(switchNode));
				}
			}

			try {
				processed.put(underProcess);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			Node switchNode = visualGraph.getNode(underProcess.getId());
			visualGraph.switchNode(switchNode, new DoneNode(switchNode));

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
		try {
			processQueue.put(start);
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
