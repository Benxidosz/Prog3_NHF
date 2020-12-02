package ges.simulator.algorithms;

import ges.graph.Graph;
import ges.graph.Node;
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
			for (Node nei : underProcess.getNeighbours()) {
				if (!(processed.contains(nei) || processQueue.contains(nei))) {
					try {
						processQueue.put(nei);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					nei.setDrawState(AlgoState.onProgress);
				}
			}

			try {
				processed.put(underProcess);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			underProcess.setDrawState(AlgoState.done);

			graph.refresh(canvas);
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
			start.setDrawState(AlgoState.onProgress);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		graph.refresh(canvas);
	}

	@Override
	public int getCycle() {
		return (graph.getNodes().size() + 2);
	}
}
