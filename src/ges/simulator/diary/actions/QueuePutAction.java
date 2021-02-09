package ges.simulator.diary.actions;

import java.util.concurrent.LinkedBlockingQueue;

public class QueuePutAction<T> implements Action {
	private final LinkedBlockingQueue<T> queue;
	private final T element;

	public QueuePutAction(LinkedBlockingQueue<T> queue, T element) {
		this.queue = queue;
		this.element = element;
	}

	@Override
	public void revert() {
		queue.remove(element);
	}
}
