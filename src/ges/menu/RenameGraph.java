package ges.menu;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class RenameGraph {
	public TextField inputText;
	final Stage stage;
	String data;

	public RenameGraph() throws IOException {
		this.data = "";

		stage = new Stage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource(String.valueOf(new File("inputString.fxml"))));

		loader.setControllerFactory(c -> this);

		Parent main = loader.load();
		Scene mainScene = new Scene(main);
		stage.setScene(mainScene);
		stage.setTitle("Input String");
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.showAndWait();
	}

	public void submit() {
		data = inputText.getText();
		if (data != null && !data.equals(""))
			stage.close();
	}

	public String getData() {
		return data;
	}
}
