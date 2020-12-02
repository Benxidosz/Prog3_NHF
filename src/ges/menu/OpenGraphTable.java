package ges.menu;

import ges.graph.Graph;
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

/**
 * The Controller of the pop up window.
 */
public class OpenGraphTable {
	/**
	 * A table, where it shows up all the existing graph.
	 */
	@FXML
	public TableView openTable;

	/**
	 * root folder.
	 */
	final File wd;

	/**
	 * The pop ups window's stage.
	 */
	Stage stage;

	/**
	 * The data, which can be gotten after the pop up closes.
	 */
	Graph data;

	/**
	 * The constructor, it load the fxml and initialize the window
	 *
	 * @throws IOException
	 */
	public OpenGraphTable() throws IOException {
		wd = new File(System.getProperty("user.dir"));

		//load fxml
		FXMLLoader loader = new FXMLLoader(getClass().getResource("openGraphTable.fxml"));
		loader.setControllerFactory(c -> this);

		Parent open = loader.load();
		Scene openScene = new Scene(open);

		//set up the stage.
		stage = new Stage();
		stage.setResizable(false);

		stage.setScene(openScene);
		stage.setTitle("Open Graph");

		stage.initModality(Modality.APPLICATION_MODAL);
		stage.showAndWait();
	}

	/**
	 * Initialize the table, with the existing graph's datas.
	 */
	@FXML
	public void initialize() {

		//graph's name column
		TableColumn nameColumn = new TableColumn("Name");
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		nameColumn.setPrefWidth(250);
		nameColumn.setEditable(false);
		nameColumn.setResizable(false);
		nameColumn.getStyleClass().add("centerColumn");

		//graph's last modified date's column
		TableColumn lastColumn = new TableColumn("Last Modified");
		lastColumn.setCellValueFactory(new PropertyValueFactory<>("modified"));
		lastColumn.setPrefWidth(150);
		lastColumn.setEditable(false);
		lastColumn.setResizable(false);
		lastColumn.getStyleClass().add("centerColumn");

		//set up the table columns
		openTable.getColumns().addAll(nameColumn, lastColumn);


		//get the saves folder
		File saves = new File(wd, "saves");
		if (saves.isDirectory()) {
			//get all .ges files in the saves
			File[] datas = Objects.requireNonNull(saves.listFiles(new FileFilter() {
				@Override
				public boolean accept(File file) {
					return file.getName().toLowerCase().endsWith(".ges");
				}
			}));
			//add to the table them.
			for (var file : datas)
				openTable.getItems().add(new TableRow(file));
		}
	}

	/**
	 * The back button. It set the data to null.
	 */
	@FXML
	public void back() {
		data = null;
		stage.close();
	}

	/**
	 * Load the chosen graph to the data.
	 *
	 * @throws IOException            Can be thrown.
	 * @throws ClassNotFoundException Can be thrown.
	 */
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
