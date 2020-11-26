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
		new MainMenu(stage);
	}

	public void load(ActionEvent actionEvent) throws IOException {
		OpenGraphTable gt = new OpenGraphTable();
		Graph load = gt.getData();
		if (load != null) {
			new Editor(load);
			stage.close();
		}
	}
}
