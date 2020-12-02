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

/**
 * The controller of the editor.fxml file. It manage all the things, which need to be managed to make possible
 * to the user to edit the graph.
 */
public class Editor {

	/**
	 * A toolButton.
	 * It is a reference to the button of the fxml, which has editorNewNode id.
	 * If it pressed, a NewNodeTool will be set as active tool.
	 */
	@FXML
	public Button editorNewNode;
	/**
	 * A toolButton.
	 * It is a reference to the button of the fxml, which has editorRmNode id.
	 * If it pressed, a RmNodeTool will be set as active tool.
	 */
	@FXML
	public Button editorRmNode;
	/**
	 * A toolButton.
	 * It is a reference to the button of the fxml, which has editorMove id.
	 * If it pressed, a MovingTool will be set as active tool.
	 */
	@FXML
	public Button editorMove;
	/**
	 * A toolButton.
	 * It is a reference to the button of the fxml, which has editorNewEdge id.
	 * If it pressed, a NewEdgeTool will be set as active tool.
	 */
	@FXML
	public Button editorNewEdge;
	/**
	 * A toolButton.
	 * It is a reference to the button of the fxml, which has editorRmEdge id.
	 * If it pressed, a RmNodeTool will be set as active tool.
	 */
	@FXML
	public Button editorRmEdge;

	/**
	 * It is a reference to the ComboBox of the fxml, which has editorNameSwitcher id.
	 * The user can switch if he want to show the id of the Nodes or the division of the Nodes, with it.
	 */
	@FXML
	public ComboBox editorNameSwitcher;

	/**
	 * It is a reference to the Canvas of the fxml, which has myCanvas id.
	 * The graph wil appear on that and the user can edit his graph by make actions on it.
	 */
	@FXML
	public Canvas myCanvas;

	/**
	 * The stage, where the things appear.
	 */
	Stage stage;

	/**
	 * the user's graph.
	 */
	Graph graph;

	/**
	 * The stepTracker, which track the changes.
	 */
	final StepTracker stepTracker;

	/**
	 * Reference to the last clicked toolButton (move, newNode, rmNode, newEdge, rmEdge)
	 */
	Button selectedButton;

	/**
	 * The tool, which active.
	 */
	Tool activeTool;

	/**
	 * The main folder.
	 */
	File wd;

	/**
	 * The Editor constructor.
	 * It load the editor.fxml and make the stage, which show the editor.fxml file content.
	 *
	 * @param g The editable graph.
	 * @throws IOException The FXMLLoader can throw this.
	 */
	public Editor(Graph g) throws IOException {
		//initialize the graph and stepTracker.
		graph = g;
		stepTracker = new StepTracker(graph, 5);

		//Load the fxml.
		FXMLLoader loader = new FXMLLoader(getClass().getResource("editor.fxml"));
		loader.setControllerFactory(c -> this);

		Parent main = loader.load();
		Scene mainScene = new Scene(main);

		//make the stage.
		stage = new Stage();
		stage.setScene(mainScene);
		stage.setResizable(false);
		stage.setTitle("Editor - " + graph.title);
		stage.show();

		//initialize the selectedButton and the wd.
		selectedButton = null;
		wd = new File(System.getProperty("user.dir"));

		//set the window title to the graph title.
		if (graph.title.equals("Untitled")) {
			RenameGraph myRe = new RenameGraph();
			graph.title = myRe.getData();
			refreshTitle();
		}

		//refresh the graph so it shown on the myCanvas.
		graph.refresh(myCanvas);
	}

	/**
	 * The initialize method of the FXML file.
	 * It initialize the values of the editorNameSwitcher.
	 */
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

	/**
	 * Manage the selectedTool and the toolButtons.
	 * The last pushed toolButton is disabled, and the tool, which belongs the toolButton, be set as activeTool.
	 *
	 * @param actionEvent The actionEvent data.
	 */
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

	/**
	 * It call the activeTool click method.
	 * It is called, when a click happen on the myCanvas.
	 *
	 * @param mouseEvent The mouseEvent data.
	 */
	@FXML
	public void canvasMouseClick(MouseEvent mouseEvent) {
		if (activeTool != null)
			activeTool.click(mouseEvent, myCanvas);
	}

	/**
	 * It call the activeTool move method.
	 * It is called, when a mouseMove happen on the myCanvas.
	 *
	 * @param mouseEvent The mouseEvent data.
	 */
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

	/**
	 * It call the activeTool pushed method.
	 * It is called, when a mouseButton have been pushed down on the myCanvas.
	 *
	 * @param mouseEvent The mouseEvent data.
	 */
	@FXML
	public void canvasMousePress(MouseEvent mouseEvent) {
		if (activeTool != null)
			activeTool.pushed(mouseEvent);
	}

	/**
	 * It call the activeTool released method.
	 * It is called, when a mouseButton have been released up on the myCanvas.
	 *
	 * @param mouseEvent The mouseEvent data.
	 */
	@FXML
	public void canvasMouseRelease(MouseEvent mouseEvent) {
		if (activeTool != null)
			activeTool.released();
	}

	/**
	 * It call the activeTool drag method.
	 * It is called, when a drag happen on the myCanvas.
	 *
	 * @param mouseEvent The mouseEvent data.
	 */
	@FXML
	public void canvasMouseDrag(MouseEvent mouseEvent) {
		if (activeTool != null)
			activeTool.drag(mouseEvent, myCanvas);
	}

	/**
	 * It switch the data, which show in the middle of the nodes.
	 * It is called, when the Value of the editorNameSwitcher changed.
	 */
	@FXML
	public void nameSwitched() {
		graph.setChooser((String) editorNameSwitcher.getValue());
		graph.refresh(myCanvas);
	}

	/**
	 * It sort the Nodes in a circle on the graph.
	 * It is called, when the sort button clicked.
	 */
	@FXML
	public void sort() {
		//The circle radius.
		double r = 300;

		//The circle center point
		double cx = myCanvas.getWidth() / 2;
		double cy = myCanvas.getHeight() / 2;

		//Calculate the new positions.
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

	/**
	 * It call the activeTool scroll method.
	 * It is called, when a scroll happen on the myCanvas.
	 *
	 * @param scrollEvent The mouseEvent data.
	 */
	@FXML
	public void canvasScroll(ScrollEvent scrollEvent) {
		if (activeTool != null)
			activeTool.scroll(scrollEvent, myCanvas);
	}

	/**
	 * Closes the stage, than open the mainMenu.
	 * Called, when the menubar/file/quit menuItem clicked.
	 *
	 * @throws IOException The MainMenu's constructor throw it.
	 */
	@FXML
	public void quit() throws IOException {
		stage.close();
		new MainMenu();
	}

	/**
	 * Exports the graph to a .svg (Vector graphic) format.
	 * If the final folder is not exist, it will create it.
	 * Called, when the menubar/file/export menuItem clicked.
	 *
	 * @throws IOException The MainMenu's constructor throw it.
	 */
	@FXML
	public void export() throws IOException {
		File export = new File(wd, "final");
		if (!export.isDirectory())
			export.mkdir();

		graph.export(new File(export, graph.title + ".svg"));
	}

	/**
	 * Open a rename pop up window, which data contains the new Title. The Editor window will wait until the pop up,
	 * not closes. If it does the
	 *
	 * @throws IOException
	 */
	@FXML
	public void rename() throws IOException {
		RenameGraph myRe = new RenameGraph();
		String newTitle = myRe.getData();
		if (newTitle != null && !newTitle.equals("Untitled"))
			graph.title = newTitle;
		refreshTitle();
	}

	/**
	 * It refresh the windowTitle.
	 */
	public void refreshTitle() {
		stage.setTitle("Editor - " + graph.title);
	}

	/**
	 * It's save the graph to a .ges format (binary format).
	 *
	 * @throws IOException FileOutputStream/ObjectOutputStream's constructor throws it.
	 */
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

	/**
	 * Open a new Editor window with an empty graph.
	 * Close the current.
	 *
	 * @throws IOException The Editor's constructor throws it.
	 */
	@FXML
	public void makeNew() throws IOException {
		new Editor(new Graph(30));
		stage.close();
	}

	/**
	 * Open an existing graph.
	 * Open a pop up window, which get the existing graph from the user.
	 *
	 * @throws IOException The Editor's constructor throws it.
	 */
	@FXML
	public void open() throws IOException {
		OpenGraphTable gt = new OpenGraphTable();
		Graph load = gt.getData();
		if (load != null) {
			new Editor(load);
			stage.close();
		}
	}

	/**
	 * Open a simulator window, with the current graph, while its save the graph.
	 * If a tool is active, call its deselect method.
	 * Close the editor window.
	 *
	 * @throws IOException The Simulator's constructor throws it.
	 */
	@FXML
	public void goSimulator() throws IOException {
		if (activeTool != null)
			activeTool.deselectNode();
		save();
		stage.close();
		new Simulator(graph);
	}

	/**
	 * Undo the last step, by call te stepTracker's undo method.
	 * It is called, when the menubar/editor/undo was clicked.
	 */
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

	/**
	 * Redo the last step, by call te stepTracker's undo method.
	 * It is called, when the menubar/editor/redo was clicked.
	 */
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

	/**
	 * It call the undo, if the CTRL+Z was pressed.
	 * It call the redo, if the SHIFT+CTRL+Z was pressed.
	 * It is called, when a keyPressed down on the window.
	 *
	 * @param keyEvent keyEvent data.
	 */
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
