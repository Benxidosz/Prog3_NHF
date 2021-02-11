package ges.simulator.diary.actions;

import ges.graph.Skin;

public class SkinMakeDoneAction implements Action {

	private final Skin skin;

	public SkinMakeDoneAction(Skin skin) {
		this.skin = skin;
	}

	@Override
	public void revert() {
		skin.unDone();
	}
}
