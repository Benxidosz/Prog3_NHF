package ges.simulator.diary.actions;

import ges.graph.node.Node;
import ges.simulator.algorithms.DFS;

public class DFSValuesChangeAction implements Action {
	private final DFS dfs;
	private final Node next;
	private final Node previous;
	private final int D;
	private final int F;

	public DFSValuesChangeAction(DFS dfs) {
		this.dfs = dfs;
		next = dfs.getNext();
		previous = dfs.getPrevious();
		D = dfs.getD();
		F = dfs.getF();
	}

	@Override
	public void revert() {
		dfs.setNext(next);
		dfs.setPrevious(previous);
		dfs.setD(D);
		dfs.setF(F);
	}
}
