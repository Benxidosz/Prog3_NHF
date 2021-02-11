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
	HashMap<String, DFSStep> table;
	int D;
	int F;
	/**
	 * The next node, which should be process,
	 */
	private Node next;
	/**
	 * the previous what was processed.
	 */
	private Node previous;

	public DFS(Graph g) {
		super(g);
	}

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

	public void setD(int d) {
		D = d;
	}

	public int getF() {
		return F;
	}

	public void setF(int f) {
		F = f;
	}

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

			previous.getNeighbours().forEach(node -> {
				Node visualNode = visualGraph.getNode(node.getId());

				step.addAction(new SkinResetAction(visualNode));
				visualNode.reset();
			});
		}

		next.getNeighbours().forEach(node -> {
			DFSStep tmp = table.get(node.getId());
			Node visualNode = visualGraph.getNode(node.getId());

			step.addAction(new SkinSwitchAction(visualNode));
			visualNode.switchSkin(new DFSReachableNodeSkin(visualNode, tmp.d, tmp.m, tmp.f));
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

			if (previous != null && progress.m.equals(previous.getId())) {
				Edge switchEdge = visualGraph.getEdge(switchNode, visualGraph.getNode(previous.getId()));
				if (switchEdge != null) {
					step.addAction(new SkinMakeDoneAction(switchEdge.getMySkin()));
					switchEdge.getMySkin().makeDone();
				}
			}
			for (Node nei : next.getNeighbours()) {
				DFSStep tmpStep = table.get(nei.getId());
				if (tmpStep.d == -1) {
					step.addAction(new HashMapValueChange<>(table, nei.getId(), new DFSStep(tmpStep)));
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
				step.addAction(new HashMapValueChange<>(table, next.getId(), new DFSStep(tmpStep)));

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

							step.addAction(new HashMapValueChange<>(table, node.getId(), new DFSStep(nextStep)));
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

	private static class DFSStep {
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
}
