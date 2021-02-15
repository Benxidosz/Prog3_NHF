package ges.simulator.diary.actions;

import ges.simulator.algorithms.BFS;

public class BFSValuesChangeAction implements Action {
	private final BFS bfs;
	private final int index;

	public BFSValuesChangeAction(BFS bfs) {
		this.bfs = bfs;
		this.index = bfs.getIndex();
	}

	@Override
	public void revert() {
		bfs.setIndex(index);
	}
}
