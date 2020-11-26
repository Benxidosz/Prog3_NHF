package ges.simulator.algorithms;

import ges.graph.Node;

import java.util.*;

public class NodeQueue {
	private final LinkedList<Node> queue;

	public NodeQueue() {
		this.queue = new LinkedList<Node>();
	}

	public boolean isEmpty() {
		return queue.isEmpty();
	}

	public void push(Node node) {
		queue.addLast(node);
	}

	public Node front() {
		Node first = queue.getFirst();
		queue.removeFirst();
		return first;
	}

	public boolean contains(Node node) {
		return queue.contains(node);
	}
}
