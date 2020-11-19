package ges.editor;

import ges.graph.Graph;
import ges.graph.Node;
import ges.graph.Position;
import ges.menu.RenameGraph;
import ges.menu.MainMenu;
import ges.tools.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.*;
import java.util.LinkedHashSet;

public class Editor {
	@FXML
	public Pane canvasPane;
	@FXML
	public Button editorNewNode;
	@FXML
	public Button editorRmNode;
	@FXML
	public Button editorMove;
	@FXML
	public Button editorNewEdge;
	@FXML
	public Button editorRmEdge;
	@FXML
	public ComboBox editorNameSwitcher;
	@FXML
	public Canvas myCanvas;


	Stage stage;
	Graph graph;

	Button selectedButton;
	Tool activeTool;

	File wd;

	public Editor(Graph g) throws IOException {
		graph = g;

		FXMLLoader loader = new FXMLLoader(getClass().getResource("editor.fxml"));
		loader.setControllerFactory(c -> this);

		Parent main = loader.load();
		Scene mainScene = new Scene(main);

		stage = new Stage();
		stage.setScene(mainScene);
		stage.setTitle("Editor - " + graph.title);
		selectedButton = null;
		wd = new File(System.getProperty("user.dir"));
		stage.show();
		if (graph.title.equals("Untitled"))
			new RenameGraph(this);
	}

	@FXML
	public void initialize() {


		ObservableList<String> options =
				FXCollections.observableArrayList(
						"Id",
						"Div"
				);
		editorNameSwitcher.setItems(options);
		editorNameSwitcher.setValue("Id");
	}

	@FXML
	public void toolSelect(ActionEvent actionEvent) {
		if (selectedButton != null)
			selectedButton.setDisable(false);

		selectedButton = (Button) actionEvent.getSource();

		if (selectedButton == editorNewNode) {
			activeTool = new NewNodeTool(graph, editorNameSwitcher);
		} else if (selectedButton == editorRmNode) {
			activeTool = new RmNodeTool(graph);
		} else if (selectedButton == editorMove) {
			activeTool = new MovingTool(graph);
		} else if (selectedButton == editorNewEdge) {
			activeTool = new NewEdgeTool(graph);
		} else if (selectedButton == editorRmEdge) {
			activeTool = new RmEdgeTool(graph);
		} else {
			activeTool = null;
		}

		selectedButton.setDisable(true);
	}

	@FXML
	public void canvasMouseClick(MouseEvent mouseEvent) {
		if (activeTool != null)
			activeTool.click(mouseEvent, myCanvas);
	}

	@FXML
	public void canvasMouseMove(MouseEvent mouseEvent) {
		Node hoover = graph.getNode(new Position(mouseEvent));
		if (hoover != null) {
			hoover.hoover(myCanvas);
		} else {
			graph.refresh(myCanvas);
		}
		if (activeTool != null)
			activeTool.move(mouseEvent, myCanvas);
	}

	@FXML
	public void canvasMousePress(MouseEvent mouseEvent) {
		if (activeTool != null)
			activeTool.pushed(mouseEvent, myCanvas);
	}

	@FXML
	public void canvasMouseRelease(MouseEvent mouseEvent) {
		if (activeTool != null)
			activeTool.released(mouseEvent, myCanvas);
	}

	@FXML
	public void canvasMouseDrag(MouseEvent mouseEvent) {
		if (activeTool != null)
			activeTool.drag(mouseEvent, myCanvas);
	}

	@FXML
	public void nameSwitched(ActionEvent actionEvent) {
		graph.setChooser((String) editorNameSwitcher.getValue());
		graph.refresh(myCanvas);
	}

	@FXML
	public void sort(ActionEvent actionEvent) {
		double r = 300;
		double cx = myCanvas.getWidth() / 2;
		double cy = myCanvas.getHeight() / 2;
		double i = 0;
		LinkedHashSet<Node> nodes = graph.getNodes();
		double alfa = Math.toRadians(((double) 360 / (double) nodes.size()));

		for (Node node : nodes) {
			double s = Math.sin(alfa * i - Math.PI / 2) * r;
			double c = Math.cos(alfa * i - Math.PI / 2) * r;

			node.setPosition(new Position(cx, cy - r));

			double xnew = c + cx;
			double ynew = s + cy;

			node.setPosition(new Position(xnew, ynew));
			++i;
		}

		graph.refresh(myCanvas);
	}

	@FXML
	public void canvasScroll(ScrollEvent scrollEvent) {
		if (activeTool != null)
			activeTool.scroll(scrollEvent, myCanvas);
	}

	@FXML
	public void quit(ActionEvent actionEvent) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(String.valueOf(new File("../menu", "mainMenu.fxml"))));

		loader.setControllerFactory(c -> new MainMenu(stage));

		Parent main = loader.load();
		Scene mainScene = new Scene(main);
		stage.setScene(mainScene);
		stage.setTitle("Main Menu");
	}

	@FXML
	public void export(ActionEvent actionEvent) throws IOException {
		File export = new File(wd, "final");
		if (!export.isDirectory())
			export.mkdir();

		graph.export(new File(export, graph.title + ".svg"));
	}

	@FXML
	public void rename(ActionEvent actionEvent) throws IOException {
		new RenameGraph(this);
	}

	public void refreshTitle(String newTitle) {
		graph.title = newTitle;
		stage.setTitle("Editor - " + graph.title);
	}

	@FXML
	public void save(ActionEvent actionEvent) throws IOException {
		File saves = new File(wd, "saves");
		if (!saves.isDirectory())
			saves.mkdir();

		FileOutputStream fout = new FileOutputStream(new File(saves, graph.title + ".dat"));
		ObjectOutputStream oos = new ObjectOutputStream(fout);
		oos.writeObject(graph);
		oos.close();
	}

	@FXML
	public void makeNew(ActionEvent actionEvent) throws IOException {
		new Editor(new Graph(30));
		stage.close();
	}
}
