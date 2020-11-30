package ges.simulator;

import ges.editor.Editor;
import ges.graph.Graph;
import ges.graph.Node;
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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class Simulator {
	@FXML
	public Canvas myCanvas;
	@FXML
	public Slider timeChangerSlider;
	@FXML
	public Label timeChangerText;
	@FXML
	public ComboBox algoSwitch;
	@FXML
	public Button startButton;
	@FXML
	public Button stepButton;

	Graph graph;
	Stage stage;
	Button selectedButton;
	File wd;
	Node startNode;
	Algorithm activeAlgo;
	Timeline myTime;

	long startTime;
	boolean first;

	double mx;
	double my;
	double sx;
	double sy;

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

		selectedButton = null;
		wd = new File(System.getProperty("user.dir"));


		graph.refresh(myCanvas);
	}

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

	@FXML
	public void canvasMouseMove(MouseEvent mouseEvent) {
		Node hoover = graph.getNode(new Position(mouseEvent));
		if (hoover != null && activeAlgo.getState().equals(AlgoState.notStarted)) {
			hoover.hoover(myCanvas);
		} else {
			graph.refresh(myCanvas);
		}
		mx = mouseEvent.getX();
		my = mouseEvent.getY();
	}

	@FXML
	public void canvasScroll(ScrollEvent scrollEvent) {
		if (scrollEvent.isControlDown()) {
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

	@FXML
	public void open() throws IOException {
		OpenGraphTable gt = new OpenGraphTable();
		Graph load = gt.getData();
		if (load != null) {
			new Simulator(load);
			stage.close();
		}
	}

	@FXML
	public void quit() throws IOException {
		stage.close();
		new MainMenu();
	}

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

	@FXML
	public void sliderDrag() {
		timeChangerSlider.setValue(Math.floor(timeChangerSlider.getValue() * 100) / 100);
		timeChangerText.setText(String.valueOf(timeChangerSlider.getValue()));
	}

	@FXML
	public void canvasDrag(MouseEvent mouseEvent) {
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

	public void canvasRelease() {
		for (Node node : graph.getNodes()) {
			node.verifyPos();
		}
	}

	@FXML
	public void canvasPress(MouseEvent mouseEvent) {
		sx = mouseEvent.getX();
		sy = mouseEvent.getY();
	}

	@FXML
	public void simulateGoToEditor() throws IOException {
		stage.close();
		new Editor(graph);
	}

	@FXML
	public void algoSwitch() {
		if ("BFS".equals(algoSwitch.getValue())) {
			activeAlgo = new BFS(graph);
		} else if ("DFS".equals(algoSwitch.getValue())) {
			activeAlgo = new DFS(graph);
		}
	}

	@FXML
	public void simulateStart() {
		if (activeAlgo.getState().equals(AlgoState.notStarted)) {
			if (startNode != null) {
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

	@FXML
	public void simulateStep() {
		if (activeAlgo.getState().equals(AlgoState.done)) {
			activeAlgo.reset();
			if (myTime != null)
				myTime.stop();
			startButton.setText("Start");
			stepButton.setDisable(false);
		} else if (activeAlgo.getState().equals(AlgoState.onProgress))
			activeAlgo.step();
		else if (activeAlgo.getState().equals(AlgoState.notStarted))
			if (startNode != null) {
				activeAlgo.start(myCanvas, startNode);
				startButton.setText("Reset");
			}
	}
}
