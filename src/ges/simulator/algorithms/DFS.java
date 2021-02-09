package ges.simulator.algorithms;

import ges.graph.Graph;
import ges.graph.edges.Edge;
import ges.graph.edges.skins.DoneEdgeSkin;
import ges.graph.node.*;
import ges.graph.node.skins.DFS.DFSDoneNodeSkin;
import ges.graph.node.skins.DFS.DFSFocusedNodeSkin;
import ges.graph.node.skins.DFS.DFSOnProgressNodeSkin;
import ges.graph.node.skins.DFS.DFSReachableNodeSkin;
import ges.simulator.diary.Step;
import ges.simulator.diary.actions.*;
import javafx.scene.canvas.Canvas;

import java.util.HashMap;

/**
 * The Depth First Algorithm
 */
public class DFS extends Algorithm {
	public DFS(Graph g) {
		super(g);
	}

	/**
	 * The next node, which should be process,
	 */
	private Node next;

	/**
	 * the previous what was processed.
	 */
	private Node previous;

	/**
	 * The previous Node's state.
	 */
	AlgoState prevState;

	public Node getNext() {
		return next;
	}

	public void setNext(Node next) {
		this.next = next;
	}

	public Node getPrevious() {
		return previous;
	}

	public void setPrevious(Node previous) {
		this.previous = previous;
	}

	public int getD() {
		return D;
	}

	public int getF() {
		return F;
	}

	public void setD(int d) {
		D = d;
	}

	public void setF(int f) {
		F = f;
	}

	class DFSStep {
		int d;
		int f;
		String m;

		DFSStep() {
			d = -1;
			f = -1;
			m = "";
		}

		DFSStep(DFSStep other) {
			d = other.d;
			f = other.f;
			m = other.m;
		}
	}

	HashMap<String, DFSStep> table;

	int D;
	int F;

	/**
	 * A dfs step.
	 */
	@Override
	public Step step() {
		Step step = new Step();

		step.addAction(new DFSValuesChangeAction(this));

		if (previous != null) {
			Node visualPrev = visualGraph.getNode(previous.getId());

			step.addAction(new SkinResetAction(visualPrev));
			visualPrev.reset();

			visualPrev.getNeighbours().forEach(node -> step.addAction(new SkinResetAction(node)));
			visualPrev.getNeighbours().forEach(Node::reset);
		}

		visualGraph.getNode(next.getId()).getNeighbours().forEach(node -> {
			DFSStep tmp = table.get(node.getId());

			step.addAction(new SkinSwitchAction(node));
			node.switchSkin(new DFSReachableNodeSkin(node, tmp.d, tmp.m, tmp.f));
		});
		if (state == AlgoState.waitForReset) {
			Node visualNext = visualGraph.getNode(next.getId());

			step.addAction(new SkinResetAction(visualNext));
			visualNext.reset();

			step.addAction(new StateChangeAction(this));
			state = AlgoState.done;
		} else {
			boolean hasNext = false;
			Node switchNode = visualGraph.getNode(next.getId());

			DFSStep progress = table.get(switchNode.getId());

			step.addAction(new SkinSetAction(switchNode));
			switchNode.setMySkin(new DFSOnProgressNodeSkin(switchNode, progress.d, progress.m, progress.f));

			if (previous != null) {
				Edge switchEdge = visualGraph.getEdge(switchNode, visualGraph.getNode(previous.getId()));
				if (switchEdge != null) {
					step.addAction(new SkinSetAction(switchEdge));
					switchEdge.setMySkin(new DoneEdgeSkin(switchEdge));
				}
			}
			for (Node nei : next.getNeighbours()) {
				DFSStep tmpStep = table.get(nei.getId());
				if (tmpStep.d == -1) {
					step.addAction(new HashMapValueChange<String, DFSStep>(table, nei.getId(), new DFSStep(tmpStep)));
					DFSStep prevStep = new DFSStep(tmpStep);

					tmpStep.d = D++;
					tmpStep.m = next.getId();

					previous = next;

					switchNode = visualGraph.getNode(next.getId());

					step.addAction(new SkinSwitchAction(switchNode));
					switchNode.switchSkin(new DFSFocusedNodeSkin(switchNode, prevStep.d, prevStep.m, prevStep.f));

					next = nei;
					hasNext = true;
					break;
				}
			}
			if (!hasNext) {
				DFSStep tmpStep = table.get(next.getId());
				step.addAction(new HashMapValueChange<String, DFSStep>(table, next.getId(), new DFSStep(tmpStep)));

				tmpStep.f = F++;

				switchNode = visualGraph.getNode(next.getId());
				step.addAction(new SkinSetAction(switchNode));
				switchNode.setMySkin(new DFSDoneNodeSkin(switchNode, tmpStep.d, tmpStep.m, tmpStep.f));

				switchNode = visualGraph.getNode(next.getId());
				step.addAction(new SkinSwitchAction(switchNode));
				switchNode.switchSkin(new DFSFocusedNodeSkin(switchNode, tmpStep.d, tmpStep.m, tmpStep.f));

				if (!"".equals(tmpStep.m) && tmpStep.m != null) {
					previous = next;
					next = graph.getNode(tmpStep.m);
				} else {
					boolean noNext = true;
					for (Node node : graph.getNodes())
						if (table.get(node.getId()).d == -1) {
							previous = next;
							next = node;

							DFSStep nextStep = table.get(node.getId());

							step.addAction(new HashMapValueChange<String, DFSStep>(table, node.getId(), new DFSStep(nextStep)));
							nextStep.d = D++;
							noNext = false;
							break;
						}

					if (noNext) {
						step.addAction(new StateChangeAction(this));
						state = AlgoState.waitForReset;
					}
				}
			}
		}
		visualGraph.refresh(canvas);

		return step;
	}

	@Override
	public void start(Canvas canvas, Node start) {
		D = 1;
		F = 1;
		table = new HashMap<>();
		for (Node node : graph.getNodes())
			table.put(node.getId(), new DFSStep());

		next = start;
		previous = null;
		table.get(start.getId()).d = D++;
		this.canvas = canvas;
		state = AlgoState.onProgress;
		step();
	}

	@Override
	public int getCycle() {
		return (graph.getNodes().size() * 2) + 1;
	}
}
