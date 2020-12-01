package ges.menu;

import ges.graph.Graph;
import ges.simulator.Simulator;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenu {
	final Stage stage;

	public MainMenu() throws IOException {
		stage = new Stage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("mainMenu.fxml"));

		loader.setControllerFactory(c -> this);

		Parent main;
		main = loader.load();
		Scene mainScene = new Scene(main);

		stage.setResizable(false);
		stage.setScene(mainScene);
		stage.setTitle("Main Menu");
		stage.show();
	}

	public MainMenu(Stage s) throws IOException {
		stage = s;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("mainMenu.fxml"));

		loader.setControllerFactory(c -> this);

		Parent main;
		main = loader.load();
		Scene mainScene = new Scene(main);

		stage.setScene(mainScene);
		stage.show();
	}

	public void quit() {
		stage.close();
	}

	public void toEditorMenu() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("editorMenu.fxml"));

		loader.setControllerFactory(c -> new EditorMenu(stage));

		Parent menuEditorP = loader.load();
		Scene editorMenu = new Scene(menuEditorP);
		stage.setTitle("Editor Menu");
		stage.setScene(editorMenu);
	}

	public void toSimulator() throws IOException {
		OpenGraphTable gt = new OpenGraphTable();
		Graph load = gt.getData();
		if (load != null) {
			new Simulator(load);
			stage.close();
		}
	}
}
