package ges.simulator.algorithms;

import ges.graph.Graph;
import ges.graph.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;

import java.util.Queue;

import static java.lang.Thread.sleep;

public class BFS extends Algorithm {
	public BFS(Graph g) {
		super(g);
	}

	@Override
	public void process(Canvas canvas, Node start, Double time) {
		long ms = (long) (time * 1000);
		startTime = System.currentTimeMillis();
		first = true;

		NodeQueue queue = new NodeQueue();
		NodeQueue processed = new NodeQueue();
		queue.push(start);
		while (!queue.isEmpty()) {
			if (first || System.currentTimeMillis() - startTime > ms) {
				first = false;
				Node active = queue.front();
				processed.push(active);
				active.drawProcessed(canvas);
				for (Node neighbour : active.getNeighbours()) {
					if (!processed.contains(neighbour) && !queue.contains(neighbour)) {
						queue.push(neighbour);
						neighbour.drawUnderProcess(canvas);
					}
				}
				startTime = System.currentTimeMillis();
			}
		}
		first = true;
	}
}
