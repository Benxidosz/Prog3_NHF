package ges.menu;

import ges.editor.Editor;
import ges.graph.Graph;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.util.Objects;

public class OpenGraphTable {
	@FXML
	public TableView openTable;

	File wd;
	Stage stage;
	Stage close;
	Scene back;
	Graph data;

	public OpenGraphTable() throws IOException {
		wd = new File(System.getProperty("user.dir"));

		FXMLLoader loader = new FXMLLoader(getClass().getResource("openGraphTable.fxml"));
		loader.setControllerFactory(c -> this);

		Parent open = loader.load();
		Scene openScene = new Scene(open);

		stage = new Stage();
		stage.setResizable(false);

		stage.setScene(openScene);
		stage.setTitle("Open Graph");

		stage.initModality(Modality.APPLICATION_MODAL);
		stage.showAndWait();
	}

	@FXML
	public void initialize() throws IOException {

		TableColumn nameColumn = new TableColumn("Name");
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		nameColumn.setPrefWidth(250);
		nameColumn.setEditable(false);
		nameColumn.setResizable(false);
		nameColumn.getStyleClass().add("centerColumn");

		TableColumn lastColumn = new TableColumn("Last Modified");
		lastColumn.setCellValueFactory(new PropertyValueFactory<>("modified"));
		lastColumn.setPrefWidth(150);
		lastColumn.setEditable(false);
		lastColumn.setResizable(false);
		lastColumn.getStyleClass().add("centerColumn");

		openTable.getColumns().addAll(nameColumn, lastColumn);


		File saves = new File(wd, "saves");
		if (saves.isDirectory()) {
			File[] datas = Objects.requireNonNull(saves.listFiles(new FileFilter() {
				@Override
				public boolean accept(File file) {
					return file.getName().toLowerCase().endsWith(".ges");
				}
			}));
			for (var file : datas)
				openTable.getItems().add(new TableRow(file));
		}
	}

	@FXML
	public void back() {
		data = null;
		stage.close();
	}

	@FXML
	public void load() throws IOException, ClassNotFoundException {
		TableRow selected = (TableRow) openTable.getSelectionModel().getSelectedItem();
		if (selected != null) {
			File loadable = selected.getFile();

			FileInputStream is = new FileInputStream(loadable);
			ObjectInputStream in = new ObjectInputStream(is);

			data = (Graph) in.readObject();
			stage.close();
		}
	}

	public Graph getData() {
		return data;
	}
}
