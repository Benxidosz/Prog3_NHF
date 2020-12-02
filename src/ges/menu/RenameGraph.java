package ges.menu;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

/**
 * The rename pop up window's controller.
 */
public class RenameGraph {
	/**
	 * The input text field
	 */
	@FXML
	public TextField inputText;

	/**
	 * The stage.
	 */
	final Stage stage;

	/**
	 * The title.
	 */
	String data;

	/**
	 * Constructor, it load the fxml, then initialize a pop up window.
	 *
	 * @throws IOException Can be thrown.
	 */
	public RenameGraph() throws IOException {
		this.data = "Untitled";

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

	/**
	 * Submit the typed title.
	 */
	public void submit() {
		data = inputText.getText();
		if (data != null && !data.equals(""))
			stage.close();
	}

	public String getData() {
		return data;
	}
}
