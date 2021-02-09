package ges.simulator.diary.actions;

import ges.simulator.algorithms.AlgoState;
import ges.simulator.algorithms.Algorithm;

public class StateChangeAction implements Action {
	private final Algorithm algo;
	private final AlgoState oldState;

	public StateChangeAction(Algorithm algo) {
		this.algo = algo;
		this.oldState = algo.getState();
	}

	@Override
	public void revert() {
		algo.setState(oldState);
	}
}
