package ges.menu;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class MainMenu {
	Stage stage;

	public MainMenu() {
		stage = new Stage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("mainMenu.fxml"));

		loader.setControllerFactory(c -> this);

		Parent main = null;
		try {
			main = loader.load();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
		Scene mainScene = new Scene(main);

		stage.setResizable(false);
		stage.setScene(mainScene);
		stage.setTitle("Main Menu");
		stage.show();
	}

	public MainMenu(Stage s) {
		stage = s;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("mainMenu.fxml"));

		loader.setControllerFactory(c -> this);

		Parent main = null;
		try {
			main = loader.load();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
		Scene mainScene = new Scene(main);

		stage.setScene(mainScene);
		stage.show();
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
