package ges.simulator.diary.actions;

import java.util.concurrent.LinkedBlockingQueue;

public class QueuePollAction<T> implements Action {
	private final LinkedBlockingQueue<T> queue;
	private final T element;

	public QueuePollAction(LinkedBlockingQueue<T> queue) {
		this.queue = queue;
		this.element = queue.peek();
	}

	@Override
	public void revert() {
		LinkedBlockingQueue<T> sideQueue = new LinkedBlockingQueue<>();
		try {
			sideQueue.put(element);
			while (!queue.isEmpty())
				sideQueue.put(queue.poll());

			while (!sideQueue.isEmpty())
				queue.put(sideQueue.poll());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
