package ges.editor.tools;

import ges.editor.diary.EditorDiary;
import ges.editor.diary.logs.NewDirectedEdgeLog;
import ges.editor.diary.logs.NewEdgeLog;
import ges.graph.Graph;
import ges.graph.Position;
import ges.graph.edges.Edge;
import ges.graph.edges.skins.DirectedEdgeSkin;
import ges.graph.node.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;

public class NewDirectedEdgeTool extends Tool {
	/**
	 * It contains a Node, which was selected by clicking it.
	 */
	private Node selectedNode;

	/**
	 * Constructor of the Tool.
	 *
	 * @param g     The value of the graph field.
	 * @param diary The value of the tracker field.
	 */
	public NewDirectedEdgeTool(Graph g, EditorDiary diary) {
		super(g, diary);
	}

	//TODO update docs

	/**
	 * It get a Node from the graph by the mouse position, if it isn't null:
	 * If the selectedNode not null, so the user have already chosen a Node it will check if the user selected the same
	 * Node for the second time, if it happened the whole selection will be cancelled. Otherwise this function, will
	 * add an Edge to the graph (addEdge method of the graph) between the two selected Node.
	 * If the user click to nothing after he choose the first Node, the same will happen, as he select the same Node
	 * for the second time, so the process will be canceled.
	 * It's called, when a mouseClick happens on a Canvas.
	 *
	 * @param mouseEvent The Event data.
	 * @param canvas     The Canvas, on which the click happened.
	 */
	@Override
	public void click(MouseEvent mouseEvent, Canvas canvas) {
		Node select = graph.getNode(new Position(mouseEvent));
		if (select != null) {
			if (selectedNode == null) {
				selectedNode = select;
				selectedNode.selected(true);
			} else {
				if (selectedNode == select) {
					selectedNode.selected(false);
					selectedNode = null;
				} else {
					graph.addDirectedEdge(selectedNode, select);
					diary.addLog(new NewDirectedEdgeLog(graph, selectedNode, select));
					selectedNode.selected(false);
					selectedNode = null;
				}
			}
		} else if (selectedNode != null) {
			selectedNode.selected(false);
			selectedNode = null;
		}
		graph.refresh(canvas);
	}

	/**
	 * If the first Node have selected, this method will create a Edge from the selected Node to the mouseCursor, than
	 * it refresh the graph so the Edge wil appear, after that it immediately delete this Edge.
	 * And along with that it will control the hover animation, it get a Node from the graph by the position of
	 * the mouseCursor, if it not null apply the hover to it.
	 * It's called, when the mouse moved on a Canvas.
	 *
	 * @param mouseEvent The Event data.
	 * @param canvas     The Canvas, on which the move happened.
	 */
	@Override
	public void move(MouseEvent mouseEvent, Canvas canvas) {
		if (selectedNode != null) {
			Edge tmpEdge = new Edge(selectedNode, new Position(mouseEvent));
			tmpEdge.setMySkin(new DirectedEdgeSkin(tmpEdge));
			graph.addEdge(tmpEdge);
			graph.refresh(canvas);
			Node hoover = graph.getNode(new Position(mouseEvent));
			if (hoover != null) {
				hoover.hoover(canvas);
			}
			graph.rmEdge(tmpEdge);
		}
	}

	/**
	 * If have selected node, it deselect it.
	 */
	@Override
	public void deselectNode() {
		if (selectedNode != null)
			selectedNode.selected(false);
	}
}
