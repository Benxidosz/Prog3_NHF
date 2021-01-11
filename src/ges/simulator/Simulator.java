package ges.simulator;

import ges.editor.Editor;
import ges.graph.Graph;
import ges.graph.nodes.Node;
import ges.graph.Position;
import ges.menu.MainMenu;
import ges.menu.OpenGraphTable;
import ges.simulator.algorithms.AlgoState;
import ges.simulator.algorithms.Algorithm;
import ges.simulator.algorithms.BFS;
import ges.simulator.algorithms.DFS;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

/**
 * The simulator window Controller class.
 */
public class Simulator {
	/**
	 * The canvas where the graph shows up.
	 */
	@FXML
	public Canvas myCanvas;

	/**
	 * The time slider. User can choose the algo timer.
	 */
	@FXML
	public Slider timeChangerSlider;

	/**
	 * Indicate the chosen time.
	 */
	@FXML
	public Label timeChangerText;

	/**
	 * User can choose an algorithm.
	 */
	@FXML
	public ComboBox algoSwitch;

	/**
	 * The start button.
	 */
	@FXML
	public Button startButton;

	/**
	 * Step by step button
	 */
	@FXML
	public Button stepButton;

	/**
	 * The graph, which on the algo run.
	 */
	final Graph graph;

	/**
	 * The window's stage.
	 */
	Stage stage;

	/**
	 * The Starting Node/
	 */
	Node startNode;

	/**
	 * The active algo
	 */
	Algorithm activeAlgo;
	Timeline myTime;

	/**
	 * mouse pos
	 */
	double mx;
	double my;

	/**
	 * MouseButton first pushed down.
	 */
	double sx;
	double sy;

	/**
	 * Constructor, it load the fxml, and set the stage up.
	 *
	 * @param load
	 * @throws IOException
	 */
	public Simulator(Graph load) throws IOException {
		graph = load;
		startNode = null;
		activeAlgo = new BFS(graph);

		FXMLLoader loader = new FXMLLoader(getClass().getResource("simulator.fxml"));
		loader.setControllerFactory(c -> this);

		Parent main = loader.load();
		Scene mainScene = new Scene(main);

		stage = new Stage();
		stage.setScene(mainScene);
		stage.setResizable(false);
		stage.setTitle("Simulator - " + graph.title);
		stage.show();


		graph.refresh(myCanvas);
	}

	/**
	 * Initialize the algoSwitch.
	 */
	@FXML
	void initialize() {
		ObservableList<String> options =
				FXCollections.observableArrayList(
						"BFS",
						"DFS"
				);
		algoSwitch.getItems().setAll(options);
		algoSwitch.setValue("BFS");
	}

	/**
	 * Canvas's click event. Choose the Starting Node.
	 *
	 * @param mouseEvent Event data.
	 */
	@FXML
	public void canvasMouseClick(MouseEvent mouseEvent) {
		Node click = graph.getNode(new Position(mouseEvent));
		if (click != null) {
			if (startNode != null)
				startNode.selected(false);
			if (click == startNode) {
				startNode = null;
			} else {
				startNode = click;
				startNode.selected(true);
			}
		}
	}

	/**
	 * Canvas's mouseMove event. For Node hoover and the Node can be moved (only all in once,)
	 *
	 * @param mouseEvent Event data.
	 */
	@FXML
	public void canvasMouseMove(MouseEvent mouseEvent) {
		if (activeAlgo.getState() == AlgoState.notStarted) {
			Node hoover = graph.getNode(new Position(mouseEvent));
			if (hoover != null && activeAlgo.getState().equals(AlgoState.notStarted)) {
				hoover.hoover(myCanvas);
			} else {
				graph.refresh(myCanvas);
			}
			mx = mouseEvent.getX();
			my = mouseEvent.getY();
		}
	}

	/**
	 * Scroll even, for a zooming.
	 *
	 * @param scrollEvent
	 */
	@FXML
	public void canvasScroll(ScrollEvent scrollEvent) {
		if (scrollEvent.isControlDown() && activeAlgo.getState() == AlgoState.notStarted) {
			if (scrollEvent.getDeltaY() != 0 && (graph.nodeRadius > 15 || scrollEvent.getDeltaY() > 0)) {
				double scroll = (scrollEvent.getDeltaY() / Math.abs(scrollEvent.getDeltaY()));
				graph.nodeRadius += scroll;
				for (Node node : graph.getNodes()) {
					double x = node.getPosition().x;
					double y = node.getPosition().y;

					node.setPosition(new Position(x - scroll * (mx - x) / graph.nodeRadius, y - scroll * (my - y) / graph.nodeRadius));
				}
				graph.refresh(myCanvas);
			}
		}
	}

	/**
	 * Open an other graph like in Editor.
	 *
	 * @throws IOException
	 */
	@FXML
	public void open() throws IOException {
		OpenGraphTable gt = new OpenGraphTable();
		Graph load = gt.getData();
		if (load != null) {
			new Simulator(load);
			stage.close();
		}
	}

	/**
	 * return to the main menu.
	 *
	 * @throws IOException
	 */
	@FXML
	public void quit() throws IOException {
		stage.close();
		new MainMenu();
	}

	/**
	 * Manege the scroll.
	 *
	 * @param scrollEvent
	 */
	@FXML
	public void sliderScroll(ScrollEvent scrollEvent) {
		double scroll = scrollEvent.getDeltaY();
		double value = timeChangerSlider.getValue();
		double tick = timeChangerSlider.getMajorTickUnit();
		if (scroll != 0) {
			scroll = scroll / Math.abs(scroll);
			value = value + scroll * tick;
			timeChangerSlider.adjustValue(value);
		}

		timeChangerSlider.setValue(Math.floor(timeChangerSlider.getValue() * 100) / 100);
		timeChangerText.setText(String.valueOf(timeChangerSlider.getValue()));
	}

	/**
	 * The slider's drag event.
	 */
	@FXML
	public void sliderDrag() {
		timeChangerSlider.setValue(Math.floor(timeChangerSlider.getValue() * 100) / 100);
		timeChangerText.setText(String.valueOf(timeChangerSlider.getValue()));
	}

	/**
	 * The Canvas's event.
	 *
	 * @param mouseEvent
	 */
	@FXML
	public void canvasDrag(MouseEvent mouseEvent) {
		if (activeAlgo.getState() == AlgoState.notStarted) {
			Node click = graph.getNode(new Position(mouseEvent));
			if (click == null) {
				for (Node node : graph.getNodes()) {
					double x = node.getPosition().x;
					double y = node.getPosition().y;
					double mx = mouseEvent.getX();
					double my = mouseEvent.getY();
					node.setTmpPosition(new Position(x - (sx - mx), y - (sy - my)));
				}
				graph.refresh(myCanvas);
			}
		}
	}

	/**
	 * The canvas's release event.
	 */
	public void canvasRelease() {
		for (Node node : graph.getNodes()) {
			node.verifyPos();
		}
	}

	/**
	 * The canvas's Press event.
	 */
	@FXML
	public void canvasPress(MouseEvent mouseEvent) {
		sx = mouseEvent.getX();
		sy = mouseEvent.getY();
	}

	/**
	 * Go to the Editor with the current graph.
	 */
	@FXML
	public void simulateGoToEditor() throws IOException {
		stage.close();
		if (startNode != null)
			startNode.selected(false);
		if (activeAlgo != null)
			activeAlgo.reset();
		new Editor(graph);
	}

	/**
	 * Manege the algoSwitch action.
	 */
	@FXML
	public void algoSwitch() {
		if ("BFS".equals(algoSwitch.getValue())) {
			activeAlgo = new BFS(graph);
		} else if ("DFS".equals(algoSwitch.getValue())) {
			activeAlgo = new DFS(graph);
		}
	}

	/**
	 * Start the simulation, with a timer.
	 */
	@FXML
	public void simulateStart() {
		if (activeAlgo.getState().equals(AlgoState.notStarted)) {
			if (startNode != null) {
				activeAlgo.graphRefresh();
				startButton.setText("Reset");
				stepButton.setDisable(true);
				activeAlgo.start(myCanvas, startNode);
				myTime = new Timeline();
				myTime.setCycleCount(activeAlgo.getCycle());
				myTime.getKeyFrames().add(new KeyFrame(Duration.seconds(timeChangerSlider.getValue()), (ActionEvent) -> simulateStep()));
				myTime.playFromStart();
			}
		} else {
			stepButton.setDisable(false);
			activeAlgo.reset();
			if (myTime != null)
				myTime.stop();
			startButton.setText("Start");
		}
	}

	/**
	 * Animate the algo step by step.
	 */
	@FXML
	public void simulateStep() {
		if (activeAlgo.getState().equals(AlgoState.done)) {
			if (myTime != null)
				myTime.stop();
			if (myTime == null) {
				activeAlgo.reset();
				startButton.setText("Start");
				stepButton.setDisable(false);
			}
		} else if (activeAlgo.getState().equals(AlgoState.onProgress) || activeAlgo.getState().equals(AlgoState.waitForReset))
			activeAlgo.step();
		else if (activeAlgo.getState().equals(AlgoState.notStarted))
			if (startNode != null) {
				activeAlgo.graphRefresh();
				activeAlgo.start(myCanvas, startNode);
				startButton.setText("Reset");
			}
	}
}
