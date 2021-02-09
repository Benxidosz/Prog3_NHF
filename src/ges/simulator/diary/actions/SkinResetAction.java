package ges.simulator.diary.actions;

import ges.graph.Skin;
import ges.graph.node.Node;

public class SkinResetAction implements Action {
	private final Node node;
	private final Skin skin;
	private final Skin prevSkin;

	public SkinResetAction(Node node) {
		this.node = node;
		this.skin = node.getMySkin();
		this.prevSkin = node.getPrevSkin();
	}

	@Override
	public void revert() {
		node.setMySkin(skin);
		node.setPrevSkin(prevSkin);
	}
}
