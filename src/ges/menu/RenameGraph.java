package ges.menu;

import ges.editor.Editor;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class RenameGraph {
	public TextField inputText;
	Stage stage;
	Editor editor;

	public RenameGraph(Editor editor) throws IOException {
		this.editor = editor;

		stage = new Stage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource(String.valueOf(new File("inputString.fxml"))));

		loader.setControllerFactory(c -> this);

		Parent main = loader.load();
		Scene mainScene = new Scene(main);
		stage.setScene(mainScene);
		stage.setTitle("Input String");
		stage.show();
	}

	public void submit(ActionEvent actionEvent) {
		String editable = inputText.getText();
		stage.close();
		if (editable != null)
			editor.refreshTitle(editable);
	}
}
