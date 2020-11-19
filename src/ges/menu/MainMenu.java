package ges.menu;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class MainMenu {
	Stage stage;

	public MainMenu(Stage s) {
		stage = s;
	}

	public void quit(ActionEvent actionEvent) {
		stage.close();
	}

	public void toEditorMenu(ActionEvent actionEvent) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("editorMenu.fxml"));
		loader.setControllerFactory(c -> new EditorMenu(stage));

		Parent menuEditorP = loader.load();
		MyScene editorMenu = new MyScene(menuEditorP, "Editor Menu");
		stage.setTitle(editorMenu.GetTitle());
		stage.setScene(editorMenu);
	}
}
