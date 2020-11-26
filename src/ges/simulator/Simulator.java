package ges.simulator;

import ges.editor.Editor;
import ges.graph.Graph;
import ges.graph.Node;
import ges.graph.Position;
import ges.menu.MainMenu;
import ges.menu.OpenGraphTable;
import ges.simulator.algorithms.Algorithm;
import ges.simulator.algorithms.BFS;
import ges.simulator.algorithms.DFS;
import ges.simulator.algorithms.NodeQueue;
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
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class Simulator {
	@FXML
	public Canvas myCanvas;
	@FXML
	public Slider timeChangerSlider;
	@FXML
	public TextField timeChangerText;
	@FXML
	public ComboBox algoSwitch;

	Graph graph;
	Stage stage;
	Button selectedButton;
	File wd;
	Node startNode;
	Algorithm activeAlgo;

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
		algoSwitch.setItems(options);
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
		if (hoover != null) {
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

		timeChangerText.setText(String.valueOf(timeChangerSlider.getValue()));
	}

	@FXML
	public void sliderDrag() {
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

	public void algoSwitch() {
		if ("BFS".equals(algoSwitch.getValue())) {
			activeAlgo = new BFS(graph);
		} else if ("DFS".equals(algoSwitch.getValue())) {
			activeAlgo = new DFS(graph);
		}
	}

	public void simulateStart() {
		if (startNode != null) {
			if ("BFS".equals(algoSwitch.getValue())) {
				long ms = (long) (timeChangerSlider.getValue() * 1000);
				startTime = System.currentTimeMillis();
				first = true;

				NodeQueue queue = new NodeQueue();
				NodeQueue processed = new NodeQueue();
				queue.push(startNode);
				while (!queue.isEmpty()) {
					if (first || System.currentTimeMillis() - startTime > ms) {
						first = false;
						Node active = queue.front();
						processed.push(active);
						active.drawProcessed(myCanvas);
						for (Node neighbour : active.getNeighbours()) {
							if (!processed.contains(neighbour) && !queue.contains(neighbour)) {
								queue.push(neighbour);
								neighbour.drawUnderProcess(myCanvas);
							}
						}
						startTime = System.currentTimeMillis();
					}
				}
				first = true;
			} else if ("DFS".equals(algoSwitch.getValue())) {
				activeAlgo = new DFS(graph);
			}
		}
	}
}
