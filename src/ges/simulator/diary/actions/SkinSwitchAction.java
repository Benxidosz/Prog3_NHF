package ges.simulator.diary.actions;

import ges.graph.node.Node;

public class SkinSwitchAction implements Action {
	private final Node node;

	public SkinSwitchAction(Node node) {
		this.node = node;
	}

	@Override
	public void revert() {
		node.reset();
	}
}
