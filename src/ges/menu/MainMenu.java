package ges.menu;

import ges.graph.Graph;
import ges.simulator.Simulator;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Open the MainMenu window and control it.
 */
public class MainMenu {
	/**
	 * The stage of the window.
	 */
	final Stage stage;

	/**
	 * The constructor of the MainMenu. It load the mainMenu.fxml and initialize a window with it.
	 *
	 * @throws IOException It can throw it.
	 */
	public MainMenu() throws IOException {
		stage = new Stage();
		// load fxml
		FXMLLoader loader = new FXMLLoader(getClass().getResource("mainMenu.fxml"));

		loader.setControllerFactory(c -> this);

		Parent main;
		main = loader.load();
		Scene mainScene = new Scene(main);

		//set up the stage
		stage.setResizable(false);
		stage.setScene(mainScene);
		stage.setTitle("Main Menu");
		stage.show();
	}

	/**
	 * MainMenu constructor, with an existing stage. It will load the fxml, but not initialize a new window, it will
	 * use an existing.
	 *
	 * @param s The given stage.
	 * @throws IOException It can throw.
	 */
	public MainMenu(Stage s) throws IOException {
		stage = s;
		//load fxml
		FXMLLoader loader = new FXMLLoader(getClass().getResource("mainMenu.fxml"));

		loader.setControllerFactory(c -> this);

		Parent main;
		main = loader.load();
		Scene mainScene = new Scene(main);

		//set the stage
		stage.setScene(mainScene);
		stage.show();
	}

	/**
	 * Close the stage.
	 */
	public void quit() {
		stage.close();
	}

	/**
	 * Open the editorMenu. It will load the editorMenu's fxml.
	 *
	 * @throws IOException It can throw it.
	 */
	public void toEditorMenu() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("editorMenu.fxml"));

		loader.setControllerFactory(c -> new EditorMenu(stage));

		Parent menuEditorP = loader.load();
		Scene editorMenu = new Scene(menuEditorP);
		stage.setTitle("Editor Menu");
		stage.setScene(editorMenu);
	}

	/**
	 * Open a pop up window, which from the user can choose an existing graph to open in simulator.
	 *
	 * @throws IOException It can throw it.
	 */
	public void toSimulator() throws IOException {
		OpenGraphTable gt = new OpenGraphTable();
		Graph load = gt.getData();
		if (load != null) {
			new Simulator(load);
			stage.close();
		}
	}
}
