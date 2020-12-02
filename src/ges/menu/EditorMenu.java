package ges.menu;

import ges.editor.Editor;
import ges.graph.Graph;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The controller of the Editor menu, where the user can decided, if they Edit a whole new graph, or modify a existing
 * one.
 */
public class EditorMenu {

	/**
	 * The current stage.
	 */
	final Stage stage;

	/**
	 * The constructor.
	 *
	 * @param s The stage value.
	 */
	EditorMenu(Stage s) {
		stage = s;
	}

	/**
	 * The make new graph option. It will open an Editor with a new Graph.
	 *
	 * @throws IOException Editor constructor can throw it.
	 */
	public void makeNew() throws IOException {
		new Editor(new Graph(30));
		stage.close();
	}

	/**
	 * Back to the main menu.
	 *
	 * @throws IOException MainMenu constructor can be throw it.
	 */
	public void back() throws IOException {
		new MainMenu(stage);
	}

	/**
	 * Open a pop up window, where from the user can choose an existing graph.
	 *
	 * @throws IOException
	 */
	public void load() throws IOException {
		OpenGraphTable gt = new OpenGraphTable();
		Graph load = gt.getData();
		if (load != null) {
			new Editor(load);
			stage.close();
		}
	}
}
