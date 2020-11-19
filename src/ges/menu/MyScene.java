package ges.menu;

import javafx.scene.Parent;
import javafx.scene.Scene;

public class MyScene extends Scene {
	private final String title;

	public MyScene(Parent parent, String title) {
		super(parent);
		this.title = title;
		getStylesheets().add("basicStyle.css");
	}

	public String GetTitle() {
		return title;
	};
}
