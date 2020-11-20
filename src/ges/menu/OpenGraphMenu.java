package ges.menu;

import ges.editor.Editor;
import ges.graph.Graph;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class OpenGraphMenu {
	@FXML
	public VBox openFileList;
	File wd;
	Stage stage;
	String type;

	public OpenGraphMenu(Stage menu, String type) throws IOException {
		this.stage = menu;
		this.type = type;

		wd = new File(System.getProperty("user.dir"));

		FXMLLoader loader = new FXMLLoader(getClass().getResource("openGraphMenu.fxml"));
		loader.setControllerFactory(c -> this);

		Parent open = loader.load();
		Scene openScene = new Scene(open);

		stage.setTitle("File Browser");
		stage.setScene(openScene);
	}

	public void back(ActionEvent actionEvent) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("editorMenu.fxml"));
		loader.setControllerFactory(c -> new EditorMenu(stage));

		Parent menuEditorP = loader.load();
		MyScene editorMenu = new MyScene(menuEditorP, "Editor Menu");
		stage.setTitle(editorMenu.GetTitle());
		stage.setScene(editorMenu);
	}

	class DataBoxHandler implements EventHandler {
		File input;

		DataBoxHandler(File from) {
			input = from;
		}

		@Override
		public void handle(Event event) {
			stage.close();
			try {
				FileInputStream is = new FileInputStream(input);
				ObjectInputStream in = new ObjectInputStream(is);
				Graph loaded = (Graph) in.readObject();
				in.close();

				if (type.equals("Editor"))
					new Editor(loaded);
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	@FXML
	public void initialize() throws IOException {
		File saves = new File(wd, "saves");
		if (saves.isDirectory()) {
			File[] datas = Objects.requireNonNull(saves.listFiles(new FileFilter() {
				@Override
				public boolean accept(File file) {
					return file.getName().toLowerCase().endsWith(".dat");
				}
			}));

			DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;


			for (var file : datas) {
				BorderPane dataBox = new BorderPane();
				Path path = Paths.get(file.toURI());
				BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
				dataBox.setPrefSize(200, 100);
				dataBox.getStyleClass().add("DataBox");

				LocalDateTime localDateTime = attr.lastModifiedTime()
						.toInstant()
						.atZone(ZoneId.systemDefault())
						.toLocalDateTime();

				Label title = new Label(file.getName());
				title.getStyleClass().add("Title");

				dataBox.setTop(title);
				dataBox.setBottom(new Label("Last Modified:\n" + localDateTime.format(dateFormatter)));

				dataBox.addEventHandler(MouseEvent.MOUSE_CLICKED, new DataBoxHandler(file));

				openFileList.getChildren().add(dataBox);
			}

		}
	}
}
