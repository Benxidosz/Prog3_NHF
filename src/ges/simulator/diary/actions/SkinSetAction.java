package ges.simulator.diary.actions;

import ges.graph.Scheme;
import ges.graph.Skin;

public class SkinSetAction implements Action {
	private final Scheme scheme;
	private final Skin oldSkin;

	public SkinSetAction(Scheme scheme) {
		this.scheme = scheme;
		this.oldSkin = scheme.getMySkin();
	}

	@Override
	public void revert() {
		scheme.setMySkin(oldSkin);
	}
}
