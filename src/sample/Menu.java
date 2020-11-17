package sample;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class Menu {
	Stage stage;
	MyScene mainScene;
	Editor myEditor;
	Button createGraph;

	Parent editor;

	private class MenuClick implements EventHandler {
		private final MyScene scene;

		MenuClick(MyScene scene){
			this.scene = scene;
		}

		@Override
		public void handle(Event event) {
			SetScene(scene);
			if (event.getSource() == createGraph) {
				myEditor = new Editor(editor);
			}
		}
	}

	private class QuitHandle implements EventHandler {

		@Override
		public void handle(Event event) {
			stage.close();
		}
	}

	public Menu(Stage stage) throws IOException {
		this.stage = stage;

		Parent main = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
		Parent menuEditor = FXMLLoader.load(getClass().getResource("editorMenu.fxml"));
		editor = FXMLLoader.load(getClass().getResource("editor.fxml"));

		mainScene = new MyScene(main, "Main Menu");

		this.stage.setResizable(false);
		SetScene(mainScene);

		main.lookup("#mainEditor").addEventHandler(MouseEvent.MOUSE_CLICKED, new MenuClick(new MyScene(menuEditor, "Main Menu")));

		menuEditor.lookup("#backEditor").addEventHandler(MouseEvent.MOUSE_CLICKED, new MenuClick(mainScene));

		createGraph = (Button) menuEditor.lookup("#newGraph");
		createGraph.addEventHandler(MouseEvent.MOUSE_CLICKED, new MenuClick(new MyScene(editor, "Editor")));

		main.lookup("#quitMenu").addEventHandler(MouseEvent.MOUSE_CLICKED, new QuitHandle());

		MenuButton file = (MenuButton) editor.lookup("#file");
		MenuItem quit = new MenuItem("Quit");

		quit.setOnAction(e -> {
			stage.setScene(mainScene);
			stage.setTitle(mainScene.GetTitle());
			stage.show();
		});

		file.getItems().add(quit);

	}

	public void SetScene(MyScene scene){
		stage.setScene(scene);
		stage.setTitle(scene.GetTitle());
		stage.show();
	}
}
