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
	String edit;

	public RenameGraph(Editor editor, String edit) throws IOException {
		this.editor = editor;
		this.edit = edit;

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
		edit = inputText.getText();
		stage.close();
		if (edit != null && !edit.equals(""))
			editor.refreshTitle(edit);
	}
}
