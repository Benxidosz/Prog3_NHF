package ges.editor.tools;

import ges.editor.diary.EditorDiary;
import ges.editor.diary.logs.RmNodeLog;
import ges.graph.Graph;
import ges.graph.node.Node;
import ges.graph.Position;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;

/**
 * A subclass of Tool. If this tool is selected, you can remove a Node from the graph.
 */
public class RmNodeTool extends Tool {

	/**
	 * The constructor of the NewNodeTool.
	 * It set the selected to null.
	 *
	 * @param g     The value of the graph field.
	 * @param diary The value of the stepTracker field.
	 */
	public RmNodeTool(Graph g, EditorDiary diary) {
		super(g, diary);
	}

	//TODO update docs

	/**
	 * It get a Node from the graph by the position of the mouseCursor.
	 * if the gotten Node not null, the method remove it from the graph.
	 *
	 * @param mouseEvent The Event data.
	 * @param canvas     The Canvas, on which the click happened.
	 */
	@Override
	public void click(MouseEvent mouseEvent, Canvas canvas) {
		Node hoover = graph.getNode(new Position(mouseEvent));
		if (hoover != null) {
			if (graph.rmNode(hoover)) {
				graph.refresh(canvas);
				diary.addLog(new RmNodeLog(graph, hoover));
			}
		}
	}
}
