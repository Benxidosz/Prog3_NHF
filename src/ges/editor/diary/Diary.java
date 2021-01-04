package ges.editor.diary;

import ges.editor.diary.logs.Log;
import ges.graph.Graph;
import ges.graph.Position;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.ScrollEvent;

import java.util.LinkedList;
import java.util.ListIterator;

public class Diary {

	//TODO update docs
	/**
	 * A list of the previous states of the graph.
	 */
	final LinkedList<Log> logs;

	//TODO update docs
	/**
	 * An iterator, which reference the current element of the list, which is the current state of graph.
	 */
	ListIterator<Log> tmpLog;

	/**
	 * An exception, what the undo or redo can throw, when there is no other steps, which can be reverted.
	 */
	public static class NoStepException extends RuntimeException {
		NoStepException(String msg) {
			super(msg);
		}
	}

	//TODO update docs

	/**
	 * The Constructor of the stepTracker.
	 */
	public Diary() {
		logs = new LinkedList<Log>();
		tmpLog = null;
	}

	//TODO update docs

	/**
	 * Add a step to the list, it happen by deepCopying the graph current state.
	 * It remove the list element, which is before teh tmpStep.
	 *
	 * @param log The graph which need to be copied.
	 */
	public void addLog(Log log) {
		if (tmpLog != null) {
			for (ListIterator<Log> iter = tmpLog; iter.hasPrevious(); ) {
				iter.previous();
				iter.remove();
			}
		}
		logs.addFirst(log);

		tmpLog = logs.listIterator(0);
	}

	//TODO update docs

	/**
	 * Undo a step.
	 *
	 * @return The graph state, what is now the current.
	 */
	public void undoStep() {
		if ((tmpLog.nextIndex()) >= logs.size())
			throw new NoStepException("No back step!");

		tmpLog.next().undo();
	}

	//TODO update docs

	/**
	 * Redo a step.
	 *
	 * @return The graph state, what is now the current.
	 */
	public void redoStep() {
		if (!tmpLog.hasPrevious())
			throw new NoStepException("No Front Step!");

		tmpLog.previous().redo();
	}

	public void scroll(Position mouse, ScrollEvent scrollEvent, Canvas canvas) {
		for (Log log : logs)
			log.scroll(mouse, scrollEvent, canvas);
	}
}
