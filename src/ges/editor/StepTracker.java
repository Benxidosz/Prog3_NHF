package ges.editor;

import ges.graph.Graph;

import java.util.LinkedList;
import java.util.ListIterator;

public class StepTracker {
	LinkedList<Graph> steps;
	ListIterator<Graph> tmpStep;
	int maxStep;

	public static class NoStepException extends RuntimeException {
		NoStepException(String msg) {
			super(msg);
		}
	}

	StepTracker(Graph main, int max) {
		steps = new LinkedList<Graph>();
		steps.addFirst(new Graph(main));
		tmpStep = steps.listIterator(0);
		maxStep = max;
	}

	public void addStep(Graph graph) {
		for (ListIterator<Graph> iter = tmpStep; iter.hasPrevious(); ) {
			iter.previous();
			iter.remove();
		}
		steps.addFirst(new Graph(graph));
		while (steps.size() > maxStep)
			steps.removeLast();
		tmpStep = steps.listIterator(0);
	}

	public Graph undoStep() {
		if ((tmpStep.nextIndex() + 1) >= steps.size())
			throw new NoStepException("No back step!");

		Graph ret = steps.get(tmpStep.nextIndex() + 1);
		tmpStep.next();
		return new Graph(ret);
	}

	public Graph redoStep() {
		if (!tmpStep.hasPrevious())
			throw new NoStepException("No Front Step!");

		return new Graph(tmpStep.previous());
	}
}
