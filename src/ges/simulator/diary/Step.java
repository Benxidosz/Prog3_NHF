package ges.simulator.diary;

import ges.simulator.diary.actions.Action;

import java.util.LinkedList;

public class Step {
	LinkedList<Action> actions;

	public Step() {
		actions = new LinkedList<>();
	}

	public void addAction(Action act) {
		actions.addFirst(act);
	}

	public void rollBack() {
		actions.forEach(Action::revert);
	}
}
