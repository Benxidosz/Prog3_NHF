package ges.editor;

import ges.graph.Graph;
import ges.graph.Node;
import ges.graph.Position;
import ges.menu.OpenGraphTable;
import ges.menu.RenameGraph;
import ges.menu.MainMenu;
import ges.editor.tools.*;
import ges.simulator.Simulator;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.*;
import java.util.LinkedHashSet;

public class Editor {

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
	@FXML
	public Pane canvasPane;


	Stage stage;
	Graph graph;
	StepTracker stepTracker;

	Button selectedButton;
	Tool activeTool;

	File wd;

	public Editor(Graph g) throws IOException {
		graph = g;
		stepTracker = new StepTracker(graph, 5);

		FXMLLoader loader = new FXMLLoader(getClass().getResource("editor.fxml"));
		loader.setControllerFactory(c -> this);

		Parent main = loader.load();
		Scene mainScene = new Scene(main);

		stage = new Stage();
		stage.setScene(mainScene);
		stage.setResizable(false);
		stage.setTitle("Editor - " + graph.title);
		stage.show();

		selectedButton = null;
		wd = new File(System.getProperty("user.dir"));

		if (graph.title.equals("Untitled")) {
			RenameGraph myRe = new RenameGraph();
			graph.title = myRe.getData();
			refreshTitle();
		}
		graph.refresh(myCanvas);
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
			activeTool = new NewNodeTool(graph, editorNameSwitcher, stepTracker);
		} else if (selectedButton == editorRmNode) {
			activeTool = new RmNodeTool(graph, stepTracker);
		} else if (selectedButton == editorMove) {
			activeTool = new MovingTool(graph, stepTracker);
		} else if (selectedButton == editorNewEdge) {
			activeTool = new NewEdgeTool(graph, stepTracker);
		} else if (selectedButton == editorRmEdge) {
			activeTool = new RmEdgeTool(graph, stepTracker);
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
	public void nameSwitched() {
		graph.setChooser((String) editorNameSwitcher.getValue());
		graph.refresh(myCanvas);
	}

	@FXML
	public void sort() {
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
		stepTracker.addStep(graph);
	}

	@FXML
	public void canvasScroll(ScrollEvent scrollEvent) {
		if (activeTool != null)
			activeTool.scroll(scrollEvent, myCanvas);
	}

	@FXML
	public void quit() throws IOException {
		stage.close();
		new MainMenu();
	}

	@FXML
	public void export() throws IOException {
		File export = new File(wd, "final");
		if (!export.isDirectory())
			export.mkdir();

		graph.export(new File(export, graph.title + ".svg"));
	}

	@FXML
	public void rename() throws IOException {
		RenameGraph myRe = new RenameGraph();
		graph.title = myRe.getData();
		System.out.println(graph.title);
		refreshTitle();
	}

	public void refreshTitle() {
		stage.setTitle("Editor - " + graph.title);
	}

	@FXML
	public void save() throws IOException {
		File saves = new File(wd, "saves");
		if (!saves.isDirectory())
			saves.mkdir();

		FileOutputStream fout = new FileOutputStream(new File(saves, graph.title + ".ges"));
		ObjectOutputStream oos = new ObjectOutputStream(fout);
		oos.writeObject(graph);
		oos.close();
	}

	@FXML
	public void makeNew() throws IOException {
		new Editor(new Graph(30));
		stage.close();
	}

	@FXML
	public void open() throws IOException {
		OpenGraphTable gt = new OpenGraphTable();
		Graph load = gt.getData();
		if (load != null) {
			new Editor(load);
			stage.close();
		}
	}

	@FXML
	public void goSimulator() throws IOException {
		save();
		stage.close();
		new Simulator(graph);
	}

	@FXML
	public void editorUndo() {
		try {
			graph = stepTracker.undoStep();
		} catch (StepTracker.NoStepException noStepException) {
			System.err.println(noStepException.getMessage());
		}
		graph.refresh(myCanvas);
		if (activeTool != null)
			activeTool.setGraph(graph);
	}

	@FXML
	public void editorRedo() {
		try {
			graph = stepTracker.redoStep();
		} catch (StepTracker.NoStepException noStepException) {
			System.err.println(noStepException.getMessage());
		}
		graph.refresh(myCanvas);
		if (activeTool != null)
			activeTool.setGraph(graph);
	}

	public void editorKeyPressed(KeyEvent keyEvent) {
		if (keyEvent.isControlDown()) {
			if (keyEvent.getCode().equals(KeyCode.Z)) {
				if (keyEvent.isShiftDown())
					editorRedo();
				else
					editorUndo();
			}
		}
	}
}
