package ges.editor;

import ges.graph.Graph;

import java.util.LinkedList;
import java.util.ListIterator;

public class StepTracker {

	/**
	 * A list of the previous states of the graph.
	 */
	final LinkedList<Graph> steps;

	/**
	 * An iterator, which reference the current element of the list, which is the current state of graph.
	 */
	ListIterator<Graph> tmpStep;

	/**
	 * The max steps, which can be reverted.
	 */
	final int maxStep;

	/**
	 * An exception, what the undo or redo can throw, when there is no other steps, which can be reverted.
	 */
	public static class NoStepException extends RuntimeException {
		NoStepException(String msg) {
			super(msg);
		}
	}

	/**
	 * The Constructor of the stepTracker.
	 *
	 * @param main The graph what is tracked.
	 * @param max  The max steps, which can be reverted.
	 */
	StepTracker(Graph main, int max) {
		steps = new LinkedList<Graph>();
		steps.addFirst(new Graph(main));
		tmpStep = steps.listIterator(0);
		maxStep = max;
	}

	/**
	 * Add a step to the list, it happen by deepCopying the graph current state.
	 * It remove the list element, which is before teh tmpStep.
	 *
	 * @param graph The graph which need to be copied.
	 */
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

	/**
	 * Undo a step.
	 *
	 * @return The graph state, what is now the current.
	 */
	public Graph undoStep() {
		if ((tmpStep.nextIndex() + 1) >= steps.size())
			throw new NoStepException("No back step!");

		Graph ret = steps.get(tmpStep.nextIndex() + 1);
		tmpStep.next();
		return new Graph(ret);
	}

	/**
	 * Redo a step.
	 *
	 * @return The graph state, what is now the current.
	 */
	public Graph redoStep() {
		if (!tmpStep.hasPrevious())
			throw new NoStepException("No Front Step!");

		return new Graph(tmpStep.previous());
	}
}
