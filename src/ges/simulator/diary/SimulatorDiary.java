package ges.simulator.diary;

import java.util.Stack;

public class SimulatorDiary {
	private final Stack<Step> steps;

	public SimulatorDiary() {
		steps = new Stack<>();
	}

	public void addStep(Step step) {
		steps.push(step);
	}

	public void back() {
		steps.pop().rollBack();
	}

	public boolean isEmpty() {
		return steps.isEmpty();
	}
}
