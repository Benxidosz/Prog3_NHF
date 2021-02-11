package ges.simulator.algorithms;

import ges.graph.Graph;
import ges.graph.edges.Edge;
import ges.graph.edges.skins.DoneEdgeSkin;
import ges.graph.node.Node;
import ges.graph.node.skins.BFS.BFSDoneNodeSkin;
import ges.graph.node.skins.BFS.BFSOnProgressNodeSkin;
import ges.simulator.diary.Step;
import ges.simulator.diary.actions.*;
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

	private static class BFSStep {
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
	public Step step() {
		Step step = new Step();

		if (processQueue.isEmpty()) {
			step.addAction(new StateChangeAction(this));
			state = AlgoState.done;
		} else {
			step.addAction(new QueuePollAction<>(processQueue));
			Node underProcess = processQueue.poll();
			assert underProcess != null;
			Node underInVisual = visualGraph.getNode(underProcess.getId());

			for (Node nei : underProcess.getNeighbours()) {
				if (!(processed.contains(nei) || processQueue.contains(nei))) {
					try {
						processQueue.put(nei);
						step.addAction(new QueuePutAction<>(processQueue, nei));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					BFSStep tmpNei = new BFSStep(distances.get(underProcess.getId()).distance + 1, index, underProcess.getId());

					distances.put(nei.getId(), tmpNei);
					step.addAction(new HashMapPutAction<>(distances, nei.getId(), tmpNei));

					index++;

					Node switchNode = visualGraph.getNode(nei.getId());

					step.addAction(new SkinSetAction(switchNode));
					switchNode.setMySkin(new BFSOnProgressNodeSkin(switchNode, tmpNei.distance, tmpNei.index, tmpNei.prev));

					Edge switchEdge = visualGraph.getEdge(switchNode, underInVisual);
					if (switchEdge != null) {
						step.addAction(new SkinMakeDoneAction(switchEdge.getMySkin()));
						switchEdge.getMySkin().makeDone();
					}
				}
			}

			try {
				processed.put(underProcess);
				step.addAction(new QueuePutAction<>(processed, underProcess));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			Node switchNode = visualGraph.getNode(underProcess.getId());
			BFSStep underProcessBFS = distances.get(underProcess.getId());

			step.addAction(new SkinSetAction(switchNode));
			switchNode.setMySkin(new BFSDoneNodeSkin(switchNode, underProcessBFS.distance, underProcessBFS.index, underProcessBFS.prev));

			visualGraph.refresh(canvas);
		}

		return step;
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
			BFSStep tmpNei = new BFSStep(0, index, "");
			distances.put(start.getId(), tmpNei);
			index++;
			Node switchNode = visualGraph.getNode(start.getId());

			switchNode.setMySkin(new BFSOnProgressNodeSkin(switchNode, tmpNei.distance, tmpNei.index, tmpNei.prev));

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
