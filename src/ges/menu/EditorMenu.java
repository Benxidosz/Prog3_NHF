package ges.menu;

import ges.editor.Editor;
import ges.graph.Graph;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class EditorMenu {
	Stage stage;

	EditorMenu(Stage s) {
		stage = s;
	}

	public void makeNew(ActionEvent actionEvent) throws IOException {
		new Editor(new Graph(30));
		stage.close();
	}

	public void back(ActionEvent actionEvent) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("mainMenu.fxml"));
		loader.setControllerFactory(c -> new MainMenu(stage));

		Parent main = loader.load();
		Scene mainScene = new Scene(main);

		stage.setScene(mainScene);
		stage.setTitle("Main Menu");
	}
}
