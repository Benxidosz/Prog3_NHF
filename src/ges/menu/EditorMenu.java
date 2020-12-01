package ges.menu;

import ges.editor.Editor;
import ges.graph.Graph;
import javafx.stage.Stage;

import java.io.IOException;

public class EditorMenu {
	final Stage stage;

	EditorMenu(Stage s) {
		stage = s;
	}

	public void makeNew() throws IOException {
		new Editor(new Graph(30));
		stage.close();
	}

	public void back() throws IOException {
		new MainMenu(stage);
	}

	public void load() throws IOException {
		OpenGraphTable gt = new OpenGraphTable();
		Graph load = gt.getData();
		if (load != null) {
			new Editor(load);
			stage.close();
		}
	}
}
